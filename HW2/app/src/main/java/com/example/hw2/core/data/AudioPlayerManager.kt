package com.example.hw2.core.data

import android.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioPlayerManager {
    private var player: MediaPlayer? = null

    suspend fun play(filePath: String) {
        withContext(Dispatchers.Main) {
            stop()
            kotlinx.coroutines.suspendCancellableCoroutine<Unit> { continuation ->
                try {
                    player = MediaPlayer().apply {
                        java.io.FileInputStream(filePath).use { fis ->
                            setDataSource(fis.fd)
                        }
                        prepare()
                        start()
                        setOnCompletionListener { mp ->
                            mp.release()
                            player = null
                            if (continuation.isActive) continuation.resumeWith(Result.success(Unit))
                        }
                        setOnErrorListener { mp, _, _ ->
                            mp.release()
                            player = null
                            if (continuation.isActive) continuation.resumeWith(Result.failure(RuntimeException("MediaPlayer error")))
                            true
                        }
                    }
                } catch (e: Exception) {
                    if (continuation.isActive) continuation.resumeWith(Result.failure(e))
                }
                
                continuation.invokeOnCancellation {
                    stop()
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
