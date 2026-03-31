package com.example.hw2.core.data

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile

class AudioRecorderManager(
    private val context: Context
) {
    private var audioRecord: AudioRecord? = null
    private var recordingThread: Thread? = null
    @Volatile
    private var isCurrentlyRecording = false
    private var outputPath: String? = null

    @SuppressLint("MissingPermission")
    fun startRecording(): String {
        val dir = File(context.filesDir, "tempAudio")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "audio_record.wav")
        outputPath = file.absolutePath

        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        Log.d(TAG, "Buffer size: $bufferSize")

        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            Log.e(TAG, "Invalid buffer size: $bufferSize")
            return file.absolutePath
        }

        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord failed to initialize, state: ${audioRecord?.state}")
            audioRecord?.release()
            audioRecord = null
            return file.absolutePath
        }

        Log.d(TAG, "AudioRecord initialized, starting recording to: ${file.absolutePath}")
        isCurrentlyRecording = true
        audioRecord?.startRecording()

        // Write PCM data in a background thread
        recordingThread = Thread {
            writeWavFile(file, bufferSize)
        }.also { it.start() }

        return file.absolutePath
    }

    fun stopRecording(): String? {
        Log.d(TAG, "stopRecording called, isCurrentlyRecording=$isCurrentlyRecording")
        isCurrentlyRecording = false
        try {
            recordingThread?.join(3000)
            audioRecord?.stop()
            audioRecord?.release()
            Log.d(TAG, "Recording stopped. File: $outputPath, size: ${File(outputPath ?: "").length()} bytes")
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping recording", e)
        }
        audioRecord = null
        recordingThread = null
        return outputPath
    }

    fun isRecording(): Boolean = isCurrentlyRecording

    private fun writeWavFile(file: File, bufferSize: Int) {
        val fos = FileOutputStream(file)
        val buffer = ByteArray(bufferSize)

        // Write placeholder WAV header (44 bytes)
        val header = ByteArray(44)
        fos.write(header)

        var totalDataSize = 0L
        Log.d(TAG, "Recording thread started, writing PCM data...")

        while (isCurrentlyRecording) {
            val read = audioRecord?.read(buffer, 0, buffer.size) ?: -1
            if (read > 0) {
                fos.write(buffer, 0, read)
                totalDataSize += read
            } else if (read < 0) {
                Log.e(TAG, "AudioRecord.read returned error: $read")
                break
            }
        }
        fos.close()
        Log.d(TAG, "Recording thread done. Total PCM data: $totalDataSize bytes")

        // Go back and fill in the WAV header with correct sizes
        val raf = RandomAccessFile(file, "rw")
        writeWavHeader(raf, totalDataSize)
        raf.close()
        Log.d(TAG, "WAV header written. Final file size: ${file.length()} bytes")
    }

    private fun writeWavHeader(raf: RandomAccessFile, totalDataSize: Long) {
        val totalFileSize = totalDataSize + 36
        val byteRate = (SAMPLE_RATE * NUM_CHANNELS * BITS_PER_SAMPLE / 8).toLong()
        val blockAlign = (NUM_CHANNELS * BITS_PER_SAMPLE / 8).toShort()

        raf.seek(0)
        // RIFF header
        raf.writeBytes("RIFF")
        raf.write(intToByteArrayLE(totalFileSize.toInt()))
        raf.writeBytes("WAVE")

        // fmt sub-chunk
        raf.writeBytes("fmt ")
        raf.write(intToByteArrayLE(16)) // PCM sub-chunk size
        raf.write(shortToByteArrayLE(1)) // AudioFormat = PCM
        raf.write(shortToByteArrayLE(NUM_CHANNELS.toShort()))
        raf.write(intToByteArrayLE(SAMPLE_RATE))
        raf.write(intToByteArrayLE(byteRate.toInt()))
        raf.write(shortToByteArrayLE(blockAlign))
        raf.write(shortToByteArrayLE(BITS_PER_SAMPLE.toShort()))

        // data sub-chunk
        raf.writeBytes("data")
        raf.write(intToByteArrayLE(totalDataSize.toInt()))
    }

    private fun intToByteArrayLE(value: Int): ByteArray {
        return byteArrayOf(
            (value and 0xFF).toByte(),
            (value shr 8 and 0xFF).toByte(),
            (value shr 16 and 0xFF).toByte(),
            (value shr 24 and 0xFF).toByte()
        )
    }

    private fun shortToByteArrayLE(value: Short): ByteArray {
        return byteArrayOf(
            (value.toInt() and 0xFF).toByte(),
            (value.toInt() shr 8 and 0xFF).toByte()
        )
    }

    companion object {
        private const val TAG = "AudioRecorderManager"
        private const val SAMPLE_RATE = 16000
        private const val NUM_CHANNELS = 1
        private const val BITS_PER_SAMPLE = 16
    }
}
