package com.example.hw2.core.data

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.net.Socket

class TtsClient {

    suspend fun synthesize(text: String, speaker: String = "10"): String? {
        return withContext(Dispatchers.IO) {
            try {
                val socket = Socket(SERVER, PORT)
                socket.soTimeout = 30_000

                // Send message with protocol format:
                // apiId@@@token@@@language@@@speaker@@@dataEOT
                val message = "$API_ID@@@$TOKEN@@@tw@@@$speaker@@@$text$END_OF_TRANSMISSION"
                socket.getOutputStream().write(message.toByteArray(Charsets.UTF_8))
                socket.getOutputStream().flush()

                // Receive response
                val bytes = socket.getInputStream().readBytes()
                socket.close()

                val responseStr = String(bytes, Charsets.UTF_8)
                if (responseStr.isEmpty()) return@withContext null

                val response = JSONObject(responseStr)
                if (response.optBoolean("status", false)) {
                    val wavBase64 = response.optString("bytes", "")
                    if (wavBase64.isEmpty()) return@withContext null

                    val cleanBase64 = wavBase64.replace("-", "+").replace("_", "/").replace(Regex("\\s+"), "")
                    val wavBytes = try {
                        Base64.decode(cleanBase64, Base64.DEFAULT)
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                        println("TTS Server returned bad base-64: ${wavBase64.take(200)}")
                        null
                    }
                    if (wavBytes == null) return@withContext null

                    val outputFile = File.createTempFile("tts_out", ".wav")
                    outputFile.writeBytes(wavBytes)

                    outputFile.absolutePath
                } else {
                    val error = response.optString("message",
                        response.optString("Message", "Unknown Error"))
                    println("TTS Server Error: $error")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    companion object {
        private const val SERVER = "140.116.245.146"
        private const val PORT = 9993
        private const val END_OF_TRANSMISSION = "EOT"
        private const val TOKEN = "mi2stts"
        private const val API_ID = "10012"
    }
}
