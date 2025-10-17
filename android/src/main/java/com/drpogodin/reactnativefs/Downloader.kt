package com.drpogodin.reactnativefs

import android.os.AsyncTask
import android.util.Log
import com.facebook.react.bridge.Arguments
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.GZIPInputStream

class Downloader : AsyncTask<DownloadParams?, LongArray?, DownloadResult>() {
    private var mParam: DownloadParams? = null
    private val mAbort = AtomicBoolean(false)
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
        var responseStream: BufferedInputStream? = null
        var responseStreamReader: BufferedReader? = null
        try {
            connection = param!!.src!!.openConnection() as HttpURLConnection
            val iterator = param.headers!!.keySetIterator()
            while (iterator.hasNextKey()) {
                val key = iterator.nextKey()
                val value = param.headers!!.getString(key)
                connection.setRequestProperty(key, value)
            }
            connection.connectTimeout = param.connectionTimeout
            connection.readTimeout = param.readTimeout
            connection.connect()
            var statusCode = connection.responseCode
            var lengthOfFile = getContentLength(connection)
            val isRedirect = statusCode != HttpURLConnection.HTTP_OK &&
                    (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == 307 || statusCode == 308)
            if (isRedirect) {
                val redirectURL = connection.getHeaderField("Location")
                connection.disconnect()
                connection = URL(redirectURL).openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
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

            mParam!!.onDownloadBegin?.onDownloadBegin(statusCode, lengthOfFile, responseHeadersBegin)

            if (statusCode in 200..299) {
                val contentEncoding = connection.getHeaderField("Content-Encoding")

                input = if ("gzip".equals(contentEncoding, ignoreCase = true)) {
                    Log.d("Downloader", "File compress with GZIP. Decompress...")
                    GZIPInputStream(connection.inputStream)
                } else {
                    BufferedInputStream(connection.inputStream, 8 * 1024)
                }

                output = FileOutputStream(param.dest)
                val data = ByteArray(8 * 1024)
                var total: Long = 0
                var count: Int
                var lastProgressValue = 0.0
                var lastProgressEmitTimestamp: Long = 0
                val hasProgressCallback = mParam!!.onDownloadProgress != null
                while (input.read(data).also { count = it } != -1) {
                    if (mAbort.get()) throw Exception("Download has been aborted")
                    total += count.toLong()
                    if (hasProgressCallback) {
                        if (param.progressInterval > 0) {
                            val timestamp = System.currentTimeMillis()
                            if (timestamp - lastProgressEmitTimestamp > param.progressInterval) {
                                lastProgressEmitTimestamp = timestamp
                                publishProgress(longArrayOf(lengthOfFile, total))
                            }
                        } else if (param.progressDivider <= 0) {
                            publishProgress(longArrayOf(lengthOfFile, total))
                        } else {
                            val progress = Math.round(total.toDouble() * 100 / lengthOfFile).toDouble()
                            if (progress % param.progressDivider == 0.0) {
                                if (progress != lastProgressValue || total == lengthOfFile) {
                                    Log.d("Downloader", "EMIT: $progress, TOTAL:$total")
                                    lastProgressValue = progress
                                    publishProgress(longArrayOf(lengthOfFile, total))
                                }
                            }
                        }
                    }
                    output.write(data, 0, count)
                }
                output.flush()
                res.bytesWritten = total
            } else {
                responseStream = BufferedInputStream(connection.errorStream)
                responseStreamReader = BufferedReader(InputStreamReader(responseStream))
                val stringBuilder = StringBuilder()
                var line: String?
                while (responseStreamReader.readLine().also { line = it } != null) {
                    stringBuilder.append(line).append("\n")
                }
                val response = stringBuilder.toString()

                res!!.body = response
            }
            res.statusCode = statusCode
            res!!.headers = responseHeaders
        } finally {
            output?.close()
            input?.close()
            connection?.disconnect()
            responseStream?.close()
            responseStreamReader?.close()
        }
    }

    private fun getContentLength(connection: HttpURLConnection?): Long {
        return connection!!.contentLengthLong
    }

    fun stop() {
        mAbort.set(true)
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
