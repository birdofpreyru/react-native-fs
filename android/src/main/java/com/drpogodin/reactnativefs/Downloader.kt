package com.drpogodin.reactnativefs

import android.os.AsyncTask
import android.util.Log
import com.facebook.react.bridge.Arguments
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.GZIPInputStream

class Downloader : AsyncTask<DownloadParams?, LongArray?, DownloadResult>() {
    private var mParam: DownloadParams? = null
    private val mAbort = AtomicBoolean(false)
    @Volatile private var activeConnection: HttpURLConnection? = null
    var res: DownloadResult? = null

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: DownloadParams?): DownloadResult {
        mParam = params[0]
        res = DownloadResult()
        Thread {
            try {
                download(mParam, res!!)
                mParam!!.onTaskCompleted?.onTaskCompleted(res)
            } catch (ex: Exception) {
                res!!.exception = ex
                mParam!!.onTaskCompleted?.onTaskCompleted(res)
            }
        }.start()
        return res!!
    }

    @Throws(Exception::class)
    private fun download(param: DownloadParams?, res: DownloadResult) {
        var input: InputStream? = null
        var output: OutputStream? = null
        var connection: HttpURLConnection? = null
        try {
            ensureActive()
            connection = param!!.src!!.openConnection() as HttpURLConnection
            activeConnection = connection
            val iterator = param.headers!!.keySetIterator()
            while (iterator.hasNextKey()) {
                val key = iterator.nextKey()
                val value = param.headers!!.getString(key)
                connection.setRequestProperty(key, value)
            }
            connection.connectTimeout = param.connectionTimeout
            connection.readTimeout = param.readTimeout
            ensureActive()
            connection.connect()
            var statusCode = connection.responseCode
            var lengthOfFile = getContentLength(connection)
            val isRedirect = statusCode != HttpURLConnection.HTTP_OK &&
                    (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == 307 || statusCode == 308)
            if (isRedirect) {
                val redirectURL = connection.getHeaderField("Location")
                connection.disconnect()
                connection = URL(redirectURL).openConnection() as HttpURLConnection
                activeConnection = connection
                connection.connectTimeout = 5000
                ensureActive()
                connection.connect()
                statusCode = connection.responseCode
                lengthOfFile = getContentLength(connection)
            }

            val responseHeadersBegin = Arguments.createMap()
            val responseHeaders = Arguments.createMap()
            val map = connection.headerFields
            for ((key, value) in map) {
              // NOTE: Although the type of key is evaluated as non-nullable by the compiler,
              // somehow it may become `null` after the upgrade to RN@0.75, thus this guard for now.
              if (key !== null) {
                val count = 0
                responseHeadersBegin.putString(key, value[count])
                responseHeaders.putString(key, value[count])
              }
            }

            val gzip = "gzip".equals(connection.getHeaderField("Content-Encoding"), ignoreCase = true)
            // Content-Length describes compressed bytes, not the bytes written below.
            if (gzip) lengthOfFile = -1
            mParam!!.onDownloadBegin?.onDownloadBegin(statusCode, lengthOfFile, responseHeadersBegin)

            if (statusCode in 200..299) {
                ensureActive()
                input = if (gzip) {
                    Log.d("Downloader", "File compress with GZIP. Decompress...")
                    GZIPInputStream(connection.inputStream)
                } else {
                    BufferedInputStream(connection.inputStream, 8 * 1024)
                }

                ensureActive()
                output = FileOutputStream(param.dest)
                val data = ByteArray(8 * 1024)
                var total: Long = 0
                var count: Int
                var lastProgressBucket = 0L
                var lastProgressEmitTimestamp = 0L
                var lastReportedBytes = -1L
                val hasProgressCallback = mParam!!.onDownloadProgress != null
                while (input.read(data).also { count = it } != -1) {
                    ensureActive()
                    output.write(data, 0, count)
                    total += count.toLong()
                    if (hasProgressCallback) {
                        var reportProgress = false
                        if (param.progressInterval > 0) {
                            val timestamp = System.nanoTime() / 1_000_000
                            if (lastReportedBytes < 0 || timestamp - lastProgressEmitTimestamp >= param.progressInterval) {
                                lastProgressEmitTimestamp = timestamp
                                reportProgress = true
                            }
                        } else if (param.progressDivider <= 0 || lengthOfFile <= 0) {
                            reportProgress = true
                        } else {
                            val bucket = (total.toDouble() * 100 / lengthOfFile / param.progressDivider).toLong()
                            if (bucket > lastProgressBucket) {
                                lastProgressBucket = bucket
                                reportProgress = true
                            }
                        }
                        if (reportProgress) {
                            publishProgress(longArrayOf(lengthOfFile, total))
                            lastReportedBytes = total
                        }
                    }
                }
                ensureActive()
                output.flush()
                if (hasProgressCallback && lastReportedBytes != total) {
                    publishProgress(longArrayOf(lengthOfFile, total))
                }
                res.bytesWritten = total
            } else {
                val errorStream = connection.errorStream
                if (errorStream != null) {
                    res.body = errorStream.bufferedReader().use { reader ->
                        val stringBuilder = StringBuilder()
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            ensureActive()
                            stringBuilder.append(line).append("\n")
                        }
                        stringBuilder.toString()
                    }
                } else {
                    res.body = ""
                }
            }
            res.statusCode = statusCode
            res!!.headers = responseHeaders
        } finally {
            try {
                output?.close()
            } finally {
                try {
                    input?.close()
                } finally {
                    connection?.disconnect()
                    activeConnection = null
                }
            }
        }
    }

    private fun getContentLength(connection: HttpURLConnection?): Long {
        return connection!!.contentLengthLong
    }

    fun stop() {
        mAbort.set(true)
        activeConnection?.disconnect()
    }

    private fun ensureActive() {
        if (mAbort.get()) throw IOException("Download has been aborted")
    }

    @Deprecated("Deprecated in Java")
    override fun onProgressUpdate(vararg args: LongArray?) {
        val values = args[0]
        super.onProgressUpdate(values)
        if (values != null) {
          mParam!!.onDownloadProgress?.onDownloadProgress(values[0], values[1])
        }
    }
}
