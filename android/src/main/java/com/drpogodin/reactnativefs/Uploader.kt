package com.drpogodin.reactnativefs

import android.os.AsyncTask
import android.webkit.MimeTypeMap
import com.facebook.react.bridge.Arguments
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.util.Locale
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class Uploader : AsyncTask<UploadParams?, IntArray?, UploadResult>() {
    private val mAbort = AtomicBoolean(false)
    @Volatile private var connection: HttpURLConnection? = null

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg uploadParams: UploadParams?): UploadResult {
        val params = uploadParams[0]!!
        val result = UploadResult()
        Thread {
            try {
                upload(params, result)
            } catch (e: Exception) {
                result.exception = e
            }
            params.onUploadComplete?.onUploadComplete(result)
        }.start()
        return result
    }

    @Throws(Exception::class)
    private fun upload(params: UploadParams, result: UploadResult) {
        val crlf = "\r\n".toByteArray(Charsets.UTF_8)
        val boundary = "----" + UUID.randomUUID().toString().replace("-", "")
        val binary = params.binaryStreamOnly
        val files = params.files!!.map { File(it.getString("filepath")!!) }
        val fields = buildString {
            if (!binary) {
                val keys = params.fields!!.keySetIterator()
                while (keys.hasNextKey()) {
                    val key = keys.nextKey()
                    append("--$boundary\r\nContent-Disposition: form-data; name=\"$key\"\r\n\r\n")
                    append(params.fields!!.getString(key))
                    append("\r\n")
                }
            }
        }.toByteArray(Charsets.UTF_8)
        val headers = params.files!!.mapIndexed { index, file ->
            if (binary) byteArrayOf()
            else {
                val name = file.getString("name")
                val filename = file.getString("filename")
                val filetype = file.getString("filetype") ?: getMimeType(file.getString("filepath"))
                ("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"; filename=\"$filename\"\r\n" +
                    "Content-Type: $filetype\r\nContent-length: ${files[index].length()}\r\n\r\n")
                    .toByteArray(Charsets.UTF_8)
            }
        }
        val tail = if (binary) byteArrayOf() else "--$boundary--\r\n".toByteArray(Charsets.UTF_8)
        val requestLength = files.sumOf { it.length() } + fields.size + tail.size +
            headers.sumOf { it.size.toLong() } + if (binary) 0 else files.size.toLong() * crlf.size
        val current = params.src!!.openConnection() as HttpURLConnection
        connection = current
        try {
            checkNotAborted()
            current.doOutput = true
            current.requestMethod = params.method
            if (!binary) current.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            val keys = params.headers!!.keySetIterator()
            while (keys.hasNextKey()) {
                val key = keys.nextKey()
                current.setRequestProperty(key, params.headers!!.getString(key))
            }
            current.setFixedLengthStreamingMode(requestLength)
            params.onUploadBegin?.onUploadBegin()
            checkNotAborted()
            current.outputStream.use { output ->
                var sent = 0L
                fun write(bytes: ByteArray, count: Int = bytes.size) {
                    checkNotAborted()
                    output.write(bytes, 0, count)
                    sent += count
                }
                write(fields)
                val buffer = ByteArray(64 * 1024)
                files.forEachIndexed { index, file ->
                    write(headers[index])
                    FileInputStream(file).use { input ->
                        var count: Int
                        while (input.read(buffer).also { count = it } != -1) {
                            write(buffer, count)
                            params.onUploadProgress?.onUploadProgress(requestLength, sent)
                        }
                    }
                    if (!binary) write(crlf)
                }
                write(tail)
                output.flush()
                params.onUploadProgress?.onUploadProgress(requestLength, sent)
            }
            checkNotAborted()
            result.statusCode = current.responseCode
            val responseHeaders = Arguments.createMap()
            for ((key, values) in current.headerFields) {
                if (key != null && values.isNotEmpty()) responseHeaders.putString(key, values[0])
            }
            result.headers = responseHeaders
            val response = if (result.statusCode >= 400) current.errorStream else current.inputStream
            result.body = response?.bufferedReader()?.use { reader ->
                buildString {
                    var line: String?
                    while (reader.readLine().also { line = it } != null) append(line).append("\n")
                }
            } ?: ""
        } finally {
            current.disconnect()
            connection = null
        }
    }

    private fun checkNotAborted() {
        if (mAbort.get()) throw IOException("Upload has been aborted")
    }

    private fun getMimeType(path: String?): String {
        val extension = path?.substringAfterLast('.', "")?.lowercase(Locale.ROOT)
        return extension?.let { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) } ?: "*/*"
    }

    fun stop() {
        mAbort.set(true)
        connection?.disconnect()
    }
}
