package vecharia.lwjgl3

import glm_.vec2.Vec2i
import imgui.DEBUG
import imgui.ImGui
import imgui.classes.Context
import imgui.impl.gl.ImplGL3
import imgui.impl.glfw.ImplGlfw
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import uno.glfw.GlfwWindow
import uno.glfw.VSync
import uno.glfw.glfw


class Window(title: String, width: Int, height: Int, vsync: Boolean) : Tickable, Disposable {
    private val windowCallbacks: MutableList<(Int, Int) -> Unit> = mutableListOf()

    private val window: GlfwWindow

    //PP.IF production
    // ImGUI
    private val imguiContext: Context
    private val imguiGlfw: ImplGlfw
    private val imguiGl3: ImplGL3
    //PP.ENDIF

    // Window parameters
    var width: Int = width
        private set
    var height: Int = height
        private set
    val ratio: Float
        get() = width.toFloat() / height.toFloat()
    var title: String = title
        set(value) {
            window.title = value
            field = value
        }
    var fullscreen: Boolean = false
        set(value) {
            field = setFullscreen(value)
        }

    // Target window size (when exiting fullscreen)
    private var tWidth: Int = width
    private var tHeight: Int = height

    // Update viewport (when screen size changes)
    private var update: Boolean = false

    init {
        glfw {
            // Change to some error handling system in the future.
            errorCallback = { error, description -> println("GLFW has encountered an error: $error\n$description") }

            init()
            windowHint {
                debug = DEBUG
                visible = false
                resizable = true

//                context.version = "3.0"
//                profile = uno.glfw.windowHint.Profile.core
//                forwardComp = true //todo convert font to 3.0 standards
            }
        }

        // GLFW
        window = GlfwWindow(width, height, title)
        window.makeContextCurrent()

        // OpenGL
        GL.createCapabilities()

        //PP.IF production
        //ImGUI
        imguiContext = Context()
        ImGui.styleColorsDark()
        imguiGlfw = ImplGlfw(window, true)
        imguiGl3 = ImplGL3()
        //PP.ENDIF

        // Window resize event and viewport update
        window.framebufferSizeCallback = { size ->
            this.width = size.x
            this.height = size.y
            glViewport(0, 0, size.x, size.y)
            windowCallbacks.forEach { it(size.x, size.y) }
        }

        // Key callback, ignoring imgui input when acceptable
        window.keyCallback = { key, _, action, _ ->
            // Ignore input when ImGui is capturing mouse.
            if (!ImGui.io.wantCaptureMouse) {
                if (key == GLFW_KEY_F && action == GLFW_RELEASE) fullscreen = !fullscreen
            }
        }

        // Center window in screen
        val mode = glfw.videoMode(glfw.primaryMonitor)
        window.pos = Vec2i(((mode?.width() ?: width) - width) / 2, ((mode?.height() ?: height) - height) / 2)

        // vsync
        glfw.swapInterval = if (vsync) VSync.ON else VSync.OFF

        window.show()
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        Garbage.dispose(this)
    }

    override fun tick() {
        if (update) MemoryStack.stackPush().use { stack ->
            val size = window.framebufferSize
            glViewport(0, 0, size.x, size.y)
        }

        window.swapBuffers()
        glfw.pollEvents()
    }

    fun onResize(callback: (Int, Int) -> Unit) = windowCallbacks.add(callback)

    fun close() = run { window.shouldClose = true }

    fun shouldClose() = window.shouldClose

    private fun setFullscreen(value: Boolean): Boolean {
        val mode = glfw.videoMode(glfw.primaryMonitor) ?: return fullscreen

        if (!fullscreen) {
            window.monitor = GlfwWindow.Monitor(
                glfw.primaryMonitor, 0, 0,
                mode.width(), mode.height(), mode.refreshRate()
            )
        } else
            window.monitor = GlfwWindow.Monitor(
                NULL, (mode.width() - tWidth) / 2,
                (mode.height() - tHeight) / 2, tWidth, tHeight, mode.refreshRate()
            )

        update = true
        return value
    }

    //PP.IF production
    fun imGuiRender() {
        val data = ImGui.drawData
        if (data != null)
            imguiGl3.renderDrawData(data)
    }

    fun imGuiNewFrame() {
        imguiGl3.newFrame()
        imguiGlfw.newFrame()
    }
    //PP.ENDIF

    override fun dispose() {
        //PP.IF production
        imguiGl3.shutdown()
        imguiGlfw.shutdown()
        imguiContext.destroy()
        //PP.ENDIF

        window.destroy()
        glfw.terminate() //todo this might need to be done in garbage, specifically last
    }

}