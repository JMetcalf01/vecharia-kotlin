package vecharia.lwjgl3

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL


class Window(title: String, width: Int, height: Int, vsync: Boolean) : Tickable {
    private val windowCallbacks: MutableList<(Int, Int) -> Unit> = mutableListOf()

    private val handle: Long

    var width: Int = width
        private set
    var height: Int = height
        private set
    val ratio: Float
        get() = width.toFloat() / height.toFloat()

    private var tWidth: Int = width
    private var tHeight: Int = height

    var title: String = title
        set(value) { field = updateTitle(value) }

    private var update: Boolean = false
    var fullscreen: Boolean = false
        set(value) { field = setFullscreen(value) }

    init {
        GLFWErrorCallback.createPrint(System.err) //todo use logger here.
        check(glfwInit()) { "Failed to initialize GLFW." }

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)

        handle = glfwCreateWindow(width, height, title, NULL, NULL)
        check(handle == NULL) { "Failed to create GLFW window." }

        glfwSetFramebufferSizeCallback(handle) { _, newWidth, newHeight ->
            this.width = newWidth
            this.height = newHeight
            glViewport(0, 0, newWidth, newHeight)
            windowCallbacks.forEach { it(newWidth, newHeight) }
        }

        glfwSetKeyCallback(handle) { _, key, _, action, _ ->
            if (key == GLFW_KEY_F && action == GLFW_RELEASE) fullscreen = !fullscreen
        }

        val mode = glfwGetVideoMode(glfwGetPrimaryMonitor())
        glfwSetWindowPos(handle, ((mode?.width() ?: width) - width) / 2, ((mode?.height() ?: height) - height) / 2)

        glfwMakeContextCurrent(handle)
        GL.createCapabilities()

        if (vsync) glfwSwapInterval(1); // 1 = 60 fps, 2 = 30 fps, etc

        glfwShowWindow(handle)

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
    }

    override fun tick() {
        if (update) MemoryStack.stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)
            glfwGetFramebufferSize(handle, pWidth, pHeight)
            glViewport(0, 0, pWidth[0], pHeight[0])
        }

        glfwSwapBuffers(handle)
        glfwPollEvents()
    }

    fun onResize(callback: (Int, Int) -> Unit) = windowCallbacks.add(callback)

    fun close() = glfwSetWindowShouldClose(handle, true)

    fun shouldClose() = glfwWindowShouldClose(handle)

    private fun setFullscreen(value: Boolean): Boolean {
        val mode = glfwGetVideoMode(glfwGetPrimaryMonitor()) ?: return fullscreen

        if (!fullscreen) {
            MemoryStack.stackPush().use { stack ->
                val pWidth = stack.mallocInt(1)
                val pHeight = stack.mallocInt(1)
                glfwGetWindowSize(handle, pWidth, pHeight)
                glfwSetWindowMonitor(
                    handle, glfwGetPrimaryMonitor(), 0, 0,
                    mode.width(), mode.height(), mode.refreshRate()
                )
            }
        } else glfwSetWindowMonitor(
            handle, NULL,
            (mode.width() - tWidth) / 2,
            (mode.height() - tHeight) / 2,
            tWidth, tHeight, mode.refreshRate()
        )

        update = true
        return value
    }

    private fun updateTitle(title: String): String {
        glfwSetWindowTitle(handle, title)
        return title
    }
}