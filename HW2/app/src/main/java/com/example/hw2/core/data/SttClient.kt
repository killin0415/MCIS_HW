package com.example.hw2.core.data

import android.util.Base64
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.io.File

class SttClient(
    private val httpClient: HttpClient
) {
    suspend fun recognize(audioFilePath: String): String? {
        return try {
            val file = File(audioFilePath)
            if (!file.exists()) return null

            val fileBytes = file.readBytes()
            val base64Audio = Base64.encodeToString(fileBytes, Base64.NO_WRAP)
            println(audioFilePath)
            val requestBody = """
                {
                    "audio": "$base64Audio",
                    "lang": "TA and ZH Medical V1",
                    "source": "人本and多語",
                    "timestamp": false
                }
            """.trimIndent()

            val response = httpClient.post(STT_API_URL) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val body = response.bodyAsText()
            if (body == "<{silent}>" || body.isBlank()) {
                null
            } else {
                body
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val STT_API_URL = "http://140.116.245.154:9001/api/base64_recognition"
    }
}
