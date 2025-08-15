package com.drpogodin.reactnativefs

import android.os.AsyncTask
import android.webkit.MimeTypeMap
import com.facebook.react.bridge.Arguments
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.nio.channels.Channels
import java.util.Locale
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil
import java.util.UUID

class Uploader : AsyncTask<UploadParams?, IntArray?, UploadResult>() {
    private var mParams: UploadParams? = null
    private var res: UploadResult? = null
    private val mAbort = AtomicBoolean(false)

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg uploadParams: UploadParams?): UploadResult {
        mParams = uploadParams[0]
        res = UploadResult()
        Thread {
            try {
                upload(mParams)
                mParams!!.onUploadComplete?.onUploadComplete(res!!)
            } catch (e: Exception) {
                res!!.exception = e
                mParams!!.onUploadComplete?.onUploadComplete(res!!)
            }
        }.start()
        return res!!
    }

    @Throws(Exception::class)
    private fun upload(params: UploadParams?) {
        var connection: HttpURLConnection? = null
        var request: DataOutputStream? = null
        val crlf = "\r\n"
        val twoHyphens = "--"
        val boundary = "----" + UUID.randomUUID().toString().replace("-", "")
        val tail = twoHyphens + boundary + twoHyphens + crlf
        var metaData = ""
        var stringData = ""
        val fileHeader: Array<String?>
        val statusCode: Int
        var byteSentTotal: Int
        var fileCount = 0
        var totalFileLength: Long = 0
        var responseStream: BufferedInputStream? = null
        var responseStreamReader: BufferedReader? = null
        var name: String
        var filename: String
        var filetype: String
        try {
          val files: Array<Any> = params!!.files!!.toTypedArray()
          val binaryStreamOnly = params.binaryStreamOnly
          connection = params.src!!.openConnection() as HttpURLConnection
          connection.doOutput = true
          val headerIterator = params.headers!!.keySetIterator()
          connection.requestMethod = params.method
          if (!binaryStreamOnly) {
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
          }
          while (headerIterator.hasNextKey()) {
            val key = headerIterator.nextKey()
            val value = params.headers!!.getString(key)
            connection.setRequestProperty(key, value)
          }
          val fieldsIterator = params.fields!!.keySetIterator()
          while (fieldsIterator.hasNextKey()) {
            val key = fieldsIterator.nextKey()
            val value = params.fields!!.getString(key)
            metaData += twoHyphens + boundary + crlf + "Content-Disposition: form-data; name=\"" + key + "\"" + crlf + crlf + value + crlf
          }
          stringData += metaData
          fileHeader = arrayOfNulls(files.size)
          for (map in params.files!!) {
            name = map.getString("name")!!
            filename = map.getString("filename")!!
            filetype = map.getString("filetype") ?: getMimeType(map.getString("filepath"))
            val file = File(map.getString("filepath")!!)
            val fileLength = file.length()
            totalFileLength += fileLength
            if (!binaryStreamOnly) {
              val fileHeaderType = twoHyphens + boundary + crlf +
                "Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + crlf +
                "Content-Type: " + filetype + crlf
              if (files.size - 1 == fileCount) {
                totalFileLength += tail.toByteArray().size
              }
              val fileLengthHeader = "Content-length: $fileLength$crlf"
              fileHeader[fileCount] = fileHeaderType + fileLengthHeader + crlf
              stringData += fileHeaderType + fileLengthHeader + crlf
            }
            fileCount++
          }
          fileCount = 0
          mParams!!.onUploadBegin?.onUploadBegin()
          if (!binaryStreamOnly) {
            var requestLength = totalFileLength
            requestLength += stringData.toByteArray().size + files.size * crlf.toByteArray().size
            connection.setRequestProperty("Content-length", "" + requestLength.toInt())
            connection.setFixedLengthStreamingMode(requestLength.toInt())
          }
          connection.connect()
          request = DataOutputStream(connection.outputStream)
          val requestChannel = Channels.newChannel(request)
          if (!binaryStreamOnly) {
            request.writeBytes(metaData)
          }
          byteSentTotal = 0
          for (map in params.files!!) {
            if (!binaryStreamOnly) {
              request.writeBytes(fileHeader[fileCount])
            }
            val file = File(map.getString("filepath")!!)
            val fileLength = file.length()
            val bufferSize = ceil((fileLength / 100f).toDouble()).toLong()
            var bytesRead: Long = 0
            val fileStream = FileInputStream(file)
            val fileChannel = fileStream.channel
            while (bytesRead < fileLength) {
              val transferredBytes = fileChannel.transferTo(bytesRead, bufferSize, requestChannel)
              bytesRead += transferredBytes
              byteSentTotal += transferredBytes.toInt()
              mParams!!.onUploadProgress?.onUploadProgress(totalFileLength.toInt(), byteSentTotal)
            }
            if (!binaryStreamOnly) {
              request.writeBytes(crlf)
            }
            fileCount++
            fileStream.close()
          }
          if (!binaryStreamOnly) {
            request.writeBytes(tail)
          }
          request.flush()
          request.close()
          responseStream = if (connection.errorStream != null) {
            BufferedInputStream(connection.errorStream)
          } else {
            BufferedInputStream(connection.inputStream)
          }
          responseStreamReader = BufferedReader(InputStreamReader(responseStream))
          val responseHeaders = Arguments.createMap()
          val map = connection.headerFields
          for ((key, value) in map) {
            // NOTE: Although the type of key is evaluated as non-nullable by the compiler,
            // somehow it may become `null` after the upgrade to RN@0.75, thus this guard for now.
            if (key !== null) {
              val count = 0
              responseHeaders.putString(key, value[count])
            }
          }
          val stringBuilder = StringBuilder()
          var line: String?
          while (responseStreamReader.readLine().also { line = it } != null) {
            stringBuilder.append(line).append("\n")
          }
          val response = stringBuilder.toString()
          statusCode = connection.responseCode
          res!!.headers = responseHeaders
          res!!.body = response
          res!!.statusCode = statusCode
        } catch (e: Exception) {
          e.printStackTrace()
          throw e
        } finally {
          connection?.disconnect()
          request?.close()
          responseStream?.close()
          responseStreamReader?.close()
        }
    }

    private fun getMimeType(path: String?): String {
        var type: String? = null
        var extension = path?.substringAfterLast('.', "")
        if (extension != null) {
            extension = extension.lowercase(Locale.getDefault())
            if (extension.isNotEmpty()) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            }
        }
        if (type == null) {
            type = "*/*"
        }
        return type
    }

    fun stop() {
        mAbort.set(true)
    }
}
