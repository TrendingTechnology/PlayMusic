package stanic.playmusic.controller

import android.app.Activity
import android.media.MediaPlayer
import kotlinx.coroutines.*
import stanic.playmusic.adapter.model.MusicModel

class MusicController(val activity: Activity) {

    val player = MediaPlayer()
    var musics = ArrayList<MusicModel>()

    var queue = ArrayList<MusicModel>()

    fun play(music: MusicModel) {
        player.reset()
        player.setDataSource(music.location)
        player.prepareAsync()
        player.setOnPreparedListener {
            it.start()
            GlobalScope.launch { thread() }
        }
    }

    fun stop() {
        player.stop()
        player.reset()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.start()
        GlobalScope.launch { thread() }
    }

    fun addToQueue() {

    }

    fun removeFromQueue() {

    }

    fun playNext(playing: MusicModel) {
        val next = queue.getOrNull(queue.indexOf(playing) + 1) ?: queue[0]

        stop()
        play(next)
    }

    private suspend fun thread() = coroutineScope {
        while (player.isPlaying) {
            delay(1000)
            println(player.duration)
        }

        cancel()
    }

    companion object {
        lateinit var INSTANCE: MusicController
    }

}

fun getMusicController() = MusicController.INSTANCE