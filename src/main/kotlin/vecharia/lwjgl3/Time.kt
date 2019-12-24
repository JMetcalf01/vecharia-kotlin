package vecharia.lwjgl3

import kotlin.math.max
import kotlin.math.min

//todo this is basically Clock, and should be merged, perhaps making clock and it's frames more advanced.
// some from http://forum.lwjgl.org/index.php?topic=5653.0
private const val TARGET_FPS = 60
private const val NANO_SECONDS = 1_000_000_000L

object Time {
    private var variableYieldTime: Long = 0
    private var lastTime: Long = 0
    private var frameStart: Long = 0

    var deltaTime: Float = 0f
        private set
    var vsync: Boolean = false

    fun update() {
        if (!vsync) sync(TARGET_FPS)
        deltaTime = (System.nanoTime() - frameStart) / NANO_SECONDS.toFloat()
        frameStart = System.nanoTime()
    }

    private fun sync(fps: Int) {
        if (fps <= 0) return

        val sleepTime: Long = NANO_SECONDS / fps // nanoseconds to sleep this frame
        // yieldTime + Remainder micro and nano seconds if smaller than sleepTime
        val yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000 * 1000))

        var overSleep = 0L
        try {
            while (true) {
                val delta = System.nanoTime() - lastTime
                if (delta < sleepTime - yieldTime)
                    Thread.sleep(1)
                else if (delta < sleepTime)
                    Thread.yield()
                else {
                    overSleep = delta - sleepTime
                    break
                }
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            lastTime = System.nanoTime() - min(overSleep, sleepTime)

            // auto tune the time sync should yield
            if (overSleep > variableYieldTime)
                // increase by 200 microseconds (1/5 a ms)
                variableYieldTime = min(variableYieldTime + 200 * 1000, sleepTime)
            else if (overSleep < variableYieldTime - 200 * 1000)
                // decrease by 2 microseconds
                variableYieldTime = max(variableYieldTime - 2 * 1000, 0)
        }
    }
}