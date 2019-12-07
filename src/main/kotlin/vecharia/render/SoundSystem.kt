package vecharia.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music

/**
 * A thread to handle all sounds in the game
 *
 * @author Jonathan Metcalf
 * @since 1.0
 */
object SoundSystem : Thread() {

    private lateinit var canvas: Canvas
    private val playing: SimpleQueue<Song> = SimpleQueue()
    private var isOn: Boolean = false
    private var wasOff: Boolean = false
    private var playingName: String = ""

    /**
     * Initializes the sound system
     *
     * @author Matt Worzala
     * @since 1.0
     */
    fun init(c: Canvas) {
        canvas = c
        start()
    }

    /**
     * Adds a song to the queue
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun add(path: String, looping: Boolean) {
        val music = Gdx.audio.newMusic(Gdx.files.absolute(path))
        music.isLooping = looping
        playing.push(Song(music, path.substring(path.indexOf('/') + 1)))
    }

    /**
     * Clears all songs to the queue
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun clear() {
        playing.clear()
    }

    /**
     * Begins playing a song
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun playM() {
        isOn = true
    }

    /**
     * Stops playing a song
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    fun stopM() {
        isOn = false
        wasOff = true
    }

    /**
     * A song that has music and a name
     *
     * @author Jonathan Metcalf
     * @since 1.0
     */
    private class Song(val music: Music, val name: String) {}
}