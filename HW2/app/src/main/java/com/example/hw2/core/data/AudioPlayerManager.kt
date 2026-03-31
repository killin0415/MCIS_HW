package com.example.hw2.core.data

import android.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioPlayerManager {
    private var player: MediaPlayer? = null

    suspend fun play(filePath: String) {
        withContext(Dispatchers.Main) {
            stop()
            player = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()
                setOnCompletionListener {
                    it.release()
                    player = null
                }
            }
        }
    }

    fun stop() {
        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        player = null
    }

    fun isPlaying(): Boolean = player?.isPlaying == true
}
