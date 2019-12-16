package vecharia.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import org.lwjgl.openal.AL
import vecharia.util.SimpleQueue

/**
 * A thread to handle all sounds in the game.
 *
 * @author Jonathan Metcalf
 * @since 1.0
 */
object SoundSystem : Thread() {

    private lateinit var canvas: Canvas
    private val playing: SimpleQueue<Song> =
        SimpleQueue()
    private var isOn: Boolean = false
    private var wasOff: Boolean = false
    private var playingName: String = ""

    var musicEnabled: Boolean = true

    /**
     * Initializes the sound system.
     *
     * @author Matt Worzala
     * @since 1.0
     *
     * @param c the Canvas object
     */
    fun init(c: Canvas) {
        canvas = c
        start()
    }

    /**
     * Adds a song to the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @param path the file path of the music
     * @param looping whether the music loops or not
     */
    fun add(path: String, looping: Boolean, volume: Float = 1f) {
        val music = Gdx.audio.newMusic(Gdx.files.absolute(path))
        music.volume = volume
        music.isLooping = looping
        playing.push(Song(music, path.substring(path.indexOf('/') + 1)))
    }

    /**
     * Clears all songs to the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun clear() {
        playing.clear()
    }

    /**
     * Begins playing a song.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun playM() {
        isOn = true
    }

    /**
     * Stops playing a song.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun stopM() {
        isOn = false
        wasOff = true
    }

    /**
     * Toggles the sound. Currently, if turned on, it plays
     * the main menu sound looping.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun toggleVolume() {
        musicEnabled = !musicEnabled
        if (musicEnabled) {
            add("assets/introscreen.mp3", true)
            playM()
        } else {
            stopM()
        }
    }



    /**
     * Starts the Sound thread.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     *
     * @see Thread
     */
    override fun run() {
        while (true) {
            if (isOn) {
                wasOff = false
                playNext()
            } else {
                sleep()
            }
        }
    }

    /**
     * Plays the next song in the queue.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     *
     * @see com.badlogic.gdx.audio.Music
     */
    private fun playNext() {
        val song: Song? = playing.pop()
        val music: Music? = song?.music
        playingName = song?.name.toString()

        if (music == null) {
            isOn = false
            return
        }

        music.volume = 0.5f
        music.play()

        while (music.isPlaying) {
            if (wasOff) {
                break
            }

            sleep()
        }

        playingName = ""

        if (wasOff) {
            music.stop()
        }
    }

    /**
     * Sleeps the music thread
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    private fun sleep() {
        try {
            sleep(10)
        } catch (ignored: Exception) {
        }
    }

    /**
     * Shuts down the audio library before the game is closed.
     *
     * @author Jonathan Metcalf
     * @since 1.1
     */
    fun end() {
        AL.destroy()
    }

    /**
     * A song that has music and a name.
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    private class Song(val music: Music, val name: String)
}