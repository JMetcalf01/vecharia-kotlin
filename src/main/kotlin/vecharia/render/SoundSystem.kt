package vecharia.render

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color

object SoundSystem : Thread() {

    private val playing: SimpleQueue<Music> = SimpleQueue()
    private val tracksNames: SimpleQueue<String> = SimpleQueue()
    private var isOn: Boolean = false
    private var wasOff: Boolean = false
    private var playingName: String = ""

    fun init() {
        start()
    }

    fun add(path: String, looping: Boolean) {
        var music = Gdx.audio.newMusic(Gdx.files.absolute(path))
        music.isLooping = looping
        playing.push(music)
        tracksNames.push(path.substring(path.indexOf('/') + 1))
    }

    fun clear() {
        playing.clear()
        tracksNames.clear()
    }

    fun playM() {
        isOn = true
    }

    fun stopM() {
        isOn = false
        wasOff = true
    }


    fun listTracks() {
        var tr = SimpleQueue<String>()
        var track = tracksNames.pop()
        while(track != null) {
            Canvas.println(track, Color.CYAN)
            tr.push(track)
            track = tracksNames.pop()
        }

        track = tr.pop()
        while(track != null) {
            tracksNames.push(track)
            track = tr.pop()
        }
    }
}