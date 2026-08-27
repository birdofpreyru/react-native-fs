package com.drpogodin.reactnativefs

import android.content.res.AssetManager
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.system.Os
import android.system.OsConstants
import android.util.Base64
import android.util.Base64OutputStream
import android.util.SparseArray
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import com.drpogodin.reactnativefs.DownloadParams.OnDownloadBegin
import com.drpogodin.reactnativefs.DownloadParams.OnDownloadProgress
import com.drpogodin.reactnativefs.DownloadParams.OnTaskCompleted
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.annotations.ReactModule

import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.RandomAccessFile
import java.net.URL
import java.security.MessageDigest
import java.util.ArrayDeque

// TODO: The compilation produces warning:
//  Note: Some input files use or override a deprecated API.
//  Note: Recompile with -Xlint:deprecation for details.
// It should be taken care of later.
class ReactNativeFsModule(reactContext: ReactApplicationContext) :
  NativeReactNativeFsSpec(reactContext) {
    private val downloaders = SparseArray<Downloader>()
    private val uploaders = SparseArray<Uploader>()
    private val pendingPickFilePromises = ArrayDeque<Promise>()
    private var pickFileLauncher: ActivityResultLauncher<Array<String>>? = null
    private fun getPickFileLauncher(): ActivityResultLauncher<Array<String>> {
        if (pickFileLauncher == null) {
            val registry = (reactApplicationContext.currentActivity as ReactActivity).activityResultRegistry
            pickFileLauncher = registry.register<Array<String>, Uri?>(
                    "RNFS_pickFile",
                    OpenDocument()
            ) { uri ->
                val res = Arguments.createArray()
                if (uri != null) res.pushString(uri.toString())
                pendingPickFilePromises.pop().resolve(res)
            }
        }
        return pickFileLauncher!!
    }

    protected fun finalize() {
        if (pickFileLauncher != null) pickFileLauncher!!.unregister()
    }

    override fun getTypedExportedConstants(): Map<String, Any?> {
        val constants: MutableMap<String, Any?> = HashMap()
        constants["DocumentDirectory"] = 0
        constants["DocumentDirectoryPath"] = this.reactApplicationContext.filesDir.absolutePath
        constants["TemporaryDirectoryPath"] = this.reactApplicationContext.cacheDir.absolutePath
        constants["PicturesDirectoryPath"] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        constants["CachesDirectoryPath"] = this.reactApplicationContext.cacheDir.absolutePath
        constants["DownloadDirectoryPath"] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        constants["FileTypeRegular"] = "0"
        constants["FileTypeDirectory"] = "1"
        constants["ExternalStorageDirectoryPath"] = Environment.getExternalStorageDirectory()?.absolutePath
        constants["ExternalDirectoryPath"] = this.reactApplicationContext.getExternalFilesDir(null)?.absolutePath
        constants["ExternalCachesDirectoryPath"] = this.reactApplicationContext.externalCacheDir?.absolutePath
        return constants
    }

    override fun appendFile(filepath: String, base64Content: String?, promise: Promise) {
        try {
            getOutputStream(filepath, true).use { outputStream ->
                val bytes = Base64.decode(base64Content, Base64.DEFAULT)
                outputStream.write(bytes)
            }

            // BEWARE: Must be outside the above block, to resolve only after the output stream
            // has been closed (and thus flushed).
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun copyAssetsFileIOS(
            imageUri: String?,
            destPath: String?,
            width: Double,
            height: Double,
            scale: Double,
            compression: Double,
            resizeMode: String?,
            promise: Promise?
    ) {
        Errors.NOT_IMPLEMENTED.reject(promise, "copyAssetsFileIOS()")
    }

    override fun copyAssetsVideoIOS(imageUri: String?, destPath: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "copyAssetsVideoIOS()")
    }

    override fun completeHandlerIOS(jobId: Double) {
        // TODO: It is iOS-only. We need at least Promise here,
        // to reject.
    }

    override fun copyFile(filepath: String?, destPath: String?, options: ReadableMap?, promise: Promise) {
        object : CopyFileTask() {
            @Deprecated("Deprecated in Java")
            override fun onPostExecute(ex: Exception?) {
                if (ex == null) {
                    promise.resolve(null)
                } else {
                    ex.printStackTrace()
                    reject(promise, filepath, ex)
                }
            }
        }.execute(filepath, destPath)
    }

    override fun copyFileAssets(from: String, into: String, promise: Promise) {
      try {
        val manager: AssetManager = reactApplicationContext.assets

        var list = manager.list(from)

        // `from` is a regular file, we just copy it and exit early.
        if (list.isNullOrEmpty()) {
          copyInputStream(manager.open(from), into)
          return promise.resolve(null)
        }

        // `from` is a folder, we need to recursively walk and copy its content in an efficient way.
        // From this point on, `currentFrom` is the currently handled asset (file or folder),
        // `currentInto` is its copy destination, and `list` is the asset's content listing.
        var currentFrom = from
        var currentInto = into
        val queue = ArrayList<Pair<String,String>>()

        while (true) {
          // Current asset is a file, we copy it and pick up the next asset from the queue, if any.
          if (list.isNullOrEmpty()) {
            copyInputStream(manager.open(currentFrom), currentInto)

            // If the queue has drained, it is success, we are done.
            if (queue.isEmpty()) return promise.resolve(null)

            // NOTE: With SDK >= 35 it could be just queue.removeLast(), but as of 27.12.2024,
            // that triggers Google.Play as an issue during app publication, thus we want to avoid
            // it, at least for now. See: https://github.com/birdofpreyru/react-native-fs/issues/92
            val next = queue.removeAt(queue.lastIndex)

            currentFrom = next.first
            currentInto = next.second
          }

          // Current asset is a folder, we need to add its content to the queue.
          else {
            // If target folder does not exist, we create it here.
            File(currentInto).mkdir()

            // We'll handle the first (0-index) asset right after the following loop,
            // which adds other asses to the queue.
            for (i in 1 until list.size) {
              var itemFrom = list[i]
              val itemInto = currentInto + File.separator + itemFrom

              // `currentFrom` can be empty, as the `from` argument can be empty (pointing to
              // the root assets folder), and we should guard this case to keep the asset path
              // relative to the root (i.e. avoid the leading separator).
              if (currentFrom.isNotEmpty()) itemFrom = currentFrom + File.separator + itemFrom

              queue.add(Pair(itemFrom, itemInto))
            }

            // Here, again, we should guard against inserting a leading separator.
            if (currentFrom.isEmpty()) currentFrom = list[0]
            else currentFrom += File.separator + list[0]

            currentInto += File.separator + list[0]
          }

          list = manager.list(currentFrom)
        }
      } catch (e: Exception) {
        Errors.OPERATION_FAILED.reject(promise, e.toString())
      }
    }

    override fun copyFileRes(filename: String, destination: String, promise: Promise) {
        try {
            val res = getResIdentifier(filename)
            val `in`: InputStream = reactApplicationContext.resources.openRawResource(res)
            copyInputStream(`in`, filename, destination, promise)
        } catch (e: Exception) {
            reject(promise, filename, Exception(String.format("Res '%s' could not be opened", filename)))
        }
    }

    // TODO: As of now it is meant to be Windows-only.
    override fun copyFolder(from: String?, to: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "copyFolder()")
    }

    override fun downloadFile(options: ReadableMap, promise: Promise) {
        try {
            val file = File(options.getString("toFile")!!)
            val url = URL(options.getString("fromUrl"))
            val jobId = options.getInt("jobId")
            val headers = options.getMap("headers")
            val progressInterval = options.getInt("progressInterval")
            val progressDivider = options.getInt("progressDivider")
            val readTimeout = options.getInt("readTimeout")
            val connectionTimeout = options.getInt("connectionTimeout")
            val hasBeginCallback = options.getBoolean("hasBeginCallback")
            val hasProgressCallback = options.getBoolean("hasProgressCallback")
            val params = DownloadParams()
            params.src = url
            params.dest = file
            params.headers = headers
            params.progressInterval = progressInterval
            params.progressDivider = progressDivider.toFloat()
            params.readTimeout = readTimeout
            params.connectionTimeout = connectionTimeout
            params.onTaskCompleted = object : OnTaskCompleted {
                override fun onTaskCompleted(res: DownloadResult?) {
                    if (res!!.exception == null) {
                        val infoMap = Arguments.createMap()
                        infoMap.putInt("jobId", jobId)
                        infoMap.putInt("statusCode", res.statusCode)
                        infoMap.putDouble("bytesWritten", res.bytesWritten.toDouble())
                        infoMap.putMap("headers", res.headers)
                        infoMap.putString("body", res.body)
                        promise.resolve(infoMap)
                    } else {
                        reject(promise, options.getString("toFile"), res.exception)
                    }
                }
            }
            if (hasBeginCallback) {
                params.onDownloadBegin = object : OnDownloadBegin {
                    override fun onDownloadBegin(statusCode: Int, contentLength: Long, headers: ReadableMap) {
                        val data = Arguments.createMap().apply {
                            putInt("jobId", jobId)
                            putInt("statusCode", statusCode)
                            putDouble("contentLength", contentLength.toDouble())
                            putMap("headers", headers)
                        }
                        emitOnDownloadBegin(data)
                    }
                }
            }
            if (hasProgressCallback) {
                params.onDownloadProgress = object : OnDownloadProgress {
                    override fun onDownloadProgress(contentLength: Long, bytesWritten: Long) {
                        val data = Arguments.createMap().apply {
                            putInt("jobId", jobId)
                            putDouble("contentLength", contentLength.toDouble())
                            putDouble("bytesWritten", bytesWritten.toDouble())
                        }
                        emitOnDownloadProgress(data)
                    }
                }
            }
            val downloader = Downloader()
            downloader.execute(params)
            downloaders.put(jobId, downloader)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, options.getString("toFile"), ex)
        }
    }

    override fun exists(filepath: String, promise: Promise) {
        try {
            val file = File(filepath)
            promise.resolve(file.exists())
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun existsAssets(filepath: String, promise: Promise) {
        try {
            val assetManager: AssetManager = reactApplicationContext.assets
            try {
                val list = assetManager.list(filepath)
                if (!list.isNullOrEmpty()) {
                    promise.resolve(true)
                    return
                }
            } catch (ignored: Exception) {
                //.. probably not a directory then
            }

            // Attempt to open file (win = exists)
            try {
                assetManager.open(filepath).use { _ -> promise.resolve(true) }
            } catch (ex: Exception) {
                promise.resolve(false) // don't throw an error, resolve false
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun existsRes(filename: String, promise: Promise) {
        try {
            val res = getResIdentifier(filename)
            if (res > 0) {
                promise.resolve(true)
            } else {
                promise.resolve(false)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filename, ex)
        }
    }

    override fun getAllExternalFilesDirs(promise: Promise) {
        val allExternalFilesDirs = this.reactApplicationContext.getExternalFilesDirs(null)
        val fs = Arguments.createArray()
        for (f in allExternalFilesDirs) {
          if (f != null) fs.pushString(f.absolutePath)
        }
        promise.resolve(fs)
    }

    override fun getFSInfo(promise: Promise) {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val statEx = StatFs(Environment.getExternalStorageDirectory().path)
        val totalSpace: Long = stat.totalBytes
        val freeSpace: Long = stat.freeBytes
        val totalSpaceEx: Long = statEx.totalBytes
        val freeSpaceEx: Long = statEx.freeBytes
        val info = Arguments.createMap()
        info.putDouble("totalSpace", totalSpace.toDouble()) // Int32 too small, must use Double
        info.putDouble("freeSpace", freeSpace.toDouble())
        info.putDouble("totalSpaceEx", totalSpaceEx.toDouble())
        info.putDouble("freeSpaceEx", freeSpaceEx.toDouble())
        promise.resolve(info)
    }

    override fun hash(filepath: String, algorithm: String, promise: Promise) {
        var inputStream: FileInputStream? = null
        try {
            val algorithms: MutableMap<String, String> = HashMap()
            algorithms["md5"] = "MD5"
            algorithms["sha1"] = "SHA-1"
            algorithms["sha224"] = "SHA-224"
            algorithms["sha256"] = "SHA-256"
            algorithms["sha384"] = "SHA-384"
            algorithms["sha512"] = "SHA-512"
            if (!algorithms.containsKey(algorithm)) throw Exception("Invalid hash algorithm")
            val file = File(filepath)
            if (file.isDirectory) {
                rejectFileIsDirectory(promise)
                return
            }
            if (!file.exists()) {
                rejectFileNotFound(promise, filepath)
                return
            }
            val md = MessageDigest.getInstance(algorithms[algorithm]!!)
            inputStream = FileInputStream(filepath)
            val buffer = ByteArray(1024 * 10) // 10 KB Buffer
            var read: Int
            while (inputStream.read(buffer).also { read = it } != -1) {
                md.update(buffer, 0, read)
            }
            val hexString = StringBuilder()
            for (digestByte in md.digest()) hexString.append(String.format("%02x", digestByte))
            promise.resolve(hexString.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        } finally {
            inputStream?.close()
        }
    }

    override fun isResumable(jobId: Double, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "isResumable()")
    }

    override fun mkdir(filepath: String, options: ReadableMap?, promise: Promise) {
        try {
            val file = File(filepath)
            file.mkdirs()
            if (!file.isDirectory) throw IOException("Directory could not be created")
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun moveFile(filepath: String, destPath: String, options: ReadableMap?, promise: Promise) {
        try {
            val inFile = File(filepath)
            if (!inFile.renameTo(File(destPath))) {
                object : CopyFileTask() {
                    @Deprecated("Deprecated in Java")
                    override fun onPostExecute(ex: Exception?) {
                        if (ex == null) {
                            if (inFile.delete()) promise.resolve(true)
                            else reject(promise, filepath, IOException("Source file could not be deleted after copying"))
                        } else {
                            ex.printStackTrace()
                            reject(promise, filepath, ex)
                        }
                    }
                }.execute(filepath, destPath)
            } else {
                promise.resolve(true)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun pathForBundle(bundle: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "pathForBundle()")
    }

    override fun pathForGroup(group: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "pathForGroup()")
    }

    override fun pickFile(options: ReadableMap, promise: Promise) {
        val mimeTypesArray = options.getArray("mimeTypes")
        var mimeTypes = emptyArray<String>()
        if (mimeTypesArray != null) {
          for (i in 0 until mimeTypesArray.size()) {
            val type = mimeTypesArray.getString(i)
            if (type != null) mimeTypes += type
          }
        }

        // Note: Here we assume that if a new pickFile() call is done prior to
        // the previous one having been completed, effectively the new call with
        // open a new file picker on top of the view stack (thus, on top of
        // the one opened for the previous call), thus just keeping all pending
        // promises in FILO stack we should be able to resolve them in the correct
        // order.
        pendingPickFilePromises.push(promise)
        getPickFileLauncher().launch(mimeTypes)
    }

    override fun read(
            filepath: String,
            length: Double,
            position: Double,
            promise: Promise
    ) {
        try {
            val base64Content = getInputStream(filepath).use { inputStream ->
                val buffer = ByteArray(length.toInt())
                var remaining = position.toLong()
                while (remaining > 0) {
                    val skipped = inputStream.skip(remaining)
                    if (skipped > 0) remaining -= skipped
                    else if (inputStream.read() == -1) break
                    else remaining--
                }
                var bytesRead = 0
                while (bytesRead < buffer.size) {
                    val count = inputStream.read(buffer, bytesRead, buffer.size - bytesRead)
                    if (count == -1) break
                    bytesRead += count
                }
                Base64.encodeToString(buffer, 0, bytesRead, Base64.NO_WRAP)
            }
            promise.resolve(base64Content)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun readDir(directory: String, promise: Promise) {
        try {
            val file = File(directory)
            if (!file.exists()) throw Exception("Folder does not exist")
            val files = file.listFiles() ?: throw IOException("Directory could not be read")
            val fileMaps = Arguments.createArray()

            // TODO: Not sure, whether we should throw or avoid to throw if files are null?
            for (childFile in files) {
                val fileMap = Arguments.createMap()
                fileMap.putDouble("mtime", childFile.lastModified().toDouble() / 1000)
                fileMap.putString("name", childFile.name)
                fileMap.putString("path", childFile.absolutePath)
                fileMap.putDouble("size", childFile.length().toDouble())
                fileMap.putString("type", if (childFile.isDirectory) "1" else "0")
                fileMaps.pushMap(fileMap)
            }
            promise.resolve(fileMaps)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, directory, ex)
        }
    }

    override fun readDirAssets(directory: String, promise: Promise) {
        try {
            val assetManager: AssetManager = reactApplicationContext.assets
            val list = assetManager.list(directory)
            val fileMaps = Arguments.createArray()
            for (childFile in list!!) {
                val fileMap = Arguments.createMap()
                fileMap.putString("name", childFile)
                val path = if (directory.isEmpty()) childFile else String.format("%s/%s", directory, childFile) // don't allow / at the start when directory is ""
                fileMap.putString("path", path)
                var length = -1
                var isDirectory: Boolean
                try {
                    val assetFileDescriptor = assetManager.openFd(path!!)
                    length = assetFileDescriptor.length.toInt()
                    assetFileDescriptor.close()
                    isDirectory = false
                } catch (ex: IOException) {
                    //.. ah.. is a directory or a compressed file?
                    isDirectory = !ex.message!!.contains("compressed")
                }
                fileMap.putInt("size", length)
                fileMap.putString("type", if (isDirectory) "1" else "0") // if 0, probably a folder..
                fileMaps.pushMap(fileMap)
            }
            promise.resolve(fileMaps)
        } catch (e: IOException) {
            reject(promise, directory, e)
        }
    }

    override fun readFile(filepath: String, promise: Promise) {
        try {
            promise.resolve(readStreamBase64(getInputStream(filepath)))
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun readFileAssets(filepath: String?, promise: Promise) {
        try {
            val assetManager: AssetManager = reactApplicationContext.assets
            promise.resolve(readStreamBase64(assetManager.open(filepath!!, 0)))
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun readFileRes(filename: String, promise: Promise) {
        try {
            val res = getResIdentifier(filename)
            promise.resolve(readStreamBase64(reactApplicationContext.resources.openRawResource(res)))
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filename, ex)
        }
    }

    private fun readStreamBase64(stream: InputStream): String = stream.use { input ->
        ByteArrayOutputStream().use { output ->
            Base64OutputStream(output, Base64.NO_WRAP).use { encoded ->
                input.copyTo(encoded)
            }
            output.toString(Charsets.US_ASCII.name())
        }
    }

    override fun resumeDownload(jobId: Double) {
        // TODO: This is currently iOS-only method,
        // and worse it does not return a promise,
        // thus we even can't cleanly reject it here.
        // At least add the Promise here.
    }

    override fun scanFile(path: String, promise: Promise) {
        MediaScannerConnection.scanFile(this.reactApplicationContext, arrayOf(path),
                null,
                object : MediaScannerConnectionClient {
                    override fun onMediaScannerConnected() {}
                    override fun onScanCompleted(path: String, uri: Uri?) {
                        promise.resolve(uri.toString())
                    }
                }
        )
    }

    override fun setReadable(
            filepath: String,
            readable: Boolean,
            ownerOnly: Boolean,
            promise: Promise
    ) {
        try {
            val file = File(filepath)
            if (!file.exists()) throw Exception("File does not exist")
            if (!file.setReadable(readable, ownerOnly)) throw IOException("File permissions could not be changed")
            promise.resolve(true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun stat(filepath: String, promise: Promise) {
        try {
            val originalFilepath = getOriginalFilepath(filepath, true)
            val file = File(originalFilepath)
            if (!file.exists()) throw FileNotFoundException("File does not exist")
            val statMap = Arguments.createMap()
            statMap.putDouble("ctime", file.lastModified().toDouble() / 1000)
            statMap.putDouble("mtime", file.lastModified().toDouble() / 1000)
            statMap.putDouble("size", file.length().toDouble())
            statMap.putString("type", if (file.isDirectory) "1" else "0")
            statMap.putString("originalFilepath", originalFilepath)
            promise.resolve(statMap)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun stopDownload(jobId: Double) {
        val downloader = downloaders[jobId.toInt()]
        downloader?.stop()
    }

    override fun stopUpload(jobId: Double) {
        val uploader = uploaders[jobId.toInt()]
        uploader?.stop()
    }

    override fun touch(filepath: String, options: ReadableMap, promise: Promise) {
        try {
            val file = File(filepath)
            if (options.hasKey("mtime") && !options.isNull("mtime")) {
                if (!file.setLastModified(options.getDouble("mtime").toLong())) {
                    throw IOException("File modification time could not be changed")
                }
            } else if (!file.exists()) throw FileNotFoundException("File does not exist")
            promise.resolve(true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun unlink(filepath: String, promise: Promise) {
        try {
            val file = File(filepath)
            deleteRecursive(file)
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    override fun uploadFiles(options: ReadableMap, promise: Promise) {
        try {
            val files = options.getArray("files")
            val url = URL(options.getString("toUrl"))
            val jobId = options.getInt("jobId")
            val headers = options.getMap("headers")
            val fields = options.getMap("fields")
            val method = options.getString("method")
            val binaryStreamOnly = options.getBoolean("binaryStreamOnly")
            val hasBeginCallback = options.getBoolean("hasBeginCallback")
            val hasProgressCallback = options.getBoolean("hasProgressCallback")
            val fileList = ArrayList<ReadableMap>()
            val params = UploadParams()
            for (i in 0 until files!!.size()) {
              val map = files.getMap(i)
              if (map != null) fileList.add(map)
            }
            params.src = url
            params.files = fileList
            params.headers = headers
            params.method = method
            params.fields = fields
            params.binaryStreamOnly = binaryStreamOnly
            params.onUploadComplete = object : UploadParams.OnUploadComplete {
                override fun onUploadComplete(res: UploadResult) {
                    if (res.exception == null) {
                        val infoMap = Arguments.createMap()
                        infoMap.putInt("jobId", jobId)
                        infoMap.putInt("statusCode", res.statusCode)
                        infoMap.putMap("headers", res.headers)
                        infoMap.putString("body", res.body)
                        promise.resolve(infoMap)
                    } else {
                        reject(promise, options.getString("toUrl"), res.exception)
                    }
                }
            }
            if (hasBeginCallback) {
                params.onUploadBegin = object : UploadParams.OnUploadBegin {
                    override fun onUploadBegin() {
                        val data = Arguments.createMap().apply {
                            putInt("jobId", jobId)
                        }
                        emitOnUploadBegin(data)
                    }
                }
            }
            if (hasProgressCallback) {
                params.onUploadProgress = object : UploadParams.OnUploadProgress {
                    override fun onUploadProgress(totalBytesExpectedToSend: Int, totalBytesSent: Int) {
                        val data = Arguments.createMap().apply {
                            putInt("jobId", jobId)
                            putInt("totalBytesExpectedToSend", totalBytesExpectedToSend)
                            putInt("totalBytesSent", totalBytesSent)
                        }
                        emitOnUploadProgress(data)
                    }
                }
            }
            val uploader = Uploader()
            uploader.execute(params)
            uploaders.put(jobId, uploader)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, options.getString("toUrl"), ex)
        }
    }

    // TODO: position arg should be double.
    override fun write(
            filepath: String,
            base64Content: String?,
            position: Double,
            promise: Promise
    ) {
        var outputStream: OutputStream? = null
        var file: RandomAccessFile? = null
        try {
            val bytes = Base64.decode(base64Content, Base64.DEFAULT)
            if (position < 0) {
                outputStream = getOutputStream(filepath, true)
                outputStream.write(bytes)
            } else {
                file = RandomAccessFile(filepath, "rw")
                file.seek(position.toLong())
                file.write(bytes)
            }
            // BEWARE: Output stream must be closed before resolving the promise.
            outputStream?.close()
            promise.resolve(null)
        } catch (ex: Exception) {
            outputStream?.close()
            ex.printStackTrace()
            reject(promise, filepath, ex)
        } finally {
            file?.close()
        }
    }

    override fun writeFile(filepath: String, base64Content: String?, options: ReadableMap?, promise: Promise) {
        try {
            getOutputStream(filepath, false).use { outputStream ->
                val bytes = Base64.decode(base64Content, Base64.DEFAULT)
                outputStream.write(bytes)
            }
            // BEWARE: Must be outside the block above to be resolved after
            // the output stream is closed.
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    private open inner class CopyFileTask : AsyncTask<String?, Void?, Exception?>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg paths: String?): Exception? {
            return try {
                val filepath = paths[0]!!
                val destPath = paths[1]!!
                getInputStream(filepath).use { input ->
                    getOutputStream(destPath, false).use { output ->
                        input.copyTo(output)
                    }
                }
                null
            } catch (ex: Exception) {
                ex
            }
        }
    }

    private fun copyInputStream(input: InputStream, source: String, destination: String, promise: Promise) {
        try {
            copyInputStream(input, destination)
            promise.resolve(null)
        } catch (ex: Exception) {
            reject(promise, source, Exception(String.format("Failed to copy '%s' to %s (%s)", source, destination, ex.localizedMessage)))
        }
    }

  /**
   * Copies given InputStream to the specified destination.
   */
  private fun copyInputStream(stream: InputStream, destination: String) {
    stream.use { input ->
      getOutputStream(destination, false).use { output ->
        if (Build.VERSION.SDK_INT >= 33) input.transferTo(output)
        else input.copyTo(output, 1024 * 10)
      }
    }
  }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (OsConstants.S_ISDIR(Os.lstat(fileOrDirectory.path).st_mode)) {
            val children = fileOrDirectory.listFiles() ?: throw IOException("Directory could not be read")
            for (child in children) {
                deleteRecursive(child)
            }
        }
        if (!fileOrDirectory.delete()) throw IOException("File could not be deleted: $fileOrDirectory")
    }

    @Throws(IORejectionException::class)
    private fun getFileUri(filepath: String, isDirectoryAllowed: Boolean): Uri {
        var uri = Uri.parse(filepath)
        if (uri.scheme == null) {
            // No prefix, assuming that provided path is absolute path to file
            val file = File(filepath)
            if (!isDirectoryAllowed && file.isDirectory) {
                throw IORejectionException("EISDIR", "EISDIR: illegal operation on a directory, read '$filepath'")
            }
            uri = Uri.fromFile(file)
        }
        return uri
    }

    @Throws(IORejectionException::class)
    private fun getInputStream(filepath: String): InputStream {
        val uri = getFileUri(filepath, false)
        val stream: InputStream? = try {
            reactApplicationContext.contentResolver.openInputStream(uri)
        } catch (ex: FileNotFoundException) {
            throw IORejectionException("ENOENT", "ENOENT: " + ex.message + ", open '" + filepath + "'")
        }
        if (stream == null) {
            throw IORejectionException("ENOENT", "ENOENT: could not open an input stream for '$filepath'")
        }
        return stream
    }

    @Throws(IORejectionException::class)
    private fun getOriginalFilepath(filepath: String, isDirectoryAllowed: Boolean): String {
        val uri = getFileUri(filepath, isDirectoryAllowed)
        var originalFilepath = filepath
        if (uri.scheme == "content") {
            try {
                reactApplicationContext.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val column = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (column >= 0 && !cursor.isNull(column)) originalFilepath = cursor.getString(column)
                    }
                }
            } catch (ignored: IllegalArgumentException) {
            }
        }
        return originalFilepath
    }

    @Throws(IORejectionException::class)
    private fun getOutputStream(filepath: String, append: Boolean): OutputStream {
        val uri = getFileUri(filepath, false)
        val stream: OutputStream? = try {
            reactApplicationContext.contentResolver.openOutputStream(uri, if (append) "wa" else writeAccessByAPILevel)
        } catch (ex: FileNotFoundException) {
            throw IORejectionException("ENOENT", "ENOENT: " + ex.message + ", open '" + filepath + "'")
        }
        if (stream == null) {
            throw IORejectionException("ENOENT", "ENOENT: could not open an output stream for '$filepath'")
        }
        return stream
    }

    private fun getResIdentifier(filename: String): Int {
        val suffix = filename.substring(filename.lastIndexOf(".") + 1)
        val name = filename.substring(0, filename.lastIndexOf("."))
        val isImage = suffix == "png" || suffix == "jpg" || suffix == "jpeg" || suffix == "bmp" || suffix == "gif" || suffix == "webp" || suffix == "psd" || suffix == "svg" || suffix == "tiff"
        return reactApplicationContext.resources.getIdentifier(name, if (isImage) "drawable" else "raw", reactApplicationContext.packageName)
    }

    private val writeAccessByAPILevel: String
        get() = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "w" else "rwt"

    // TODO: These should be merged / replaced by the dedicated "Errors" module.
    private fun reject(promise: Promise, filepath: String?, ex: Exception?) {
        if (ex is FileNotFoundException) {
            rejectFileNotFound(promise, filepath)
            return
        }
        if (ex is IORejectionException) {
            promise.reject(ex.code, ex.message)
            return
        }
        promise.reject("RNFS", ex!!.message)
    }

    private fun rejectFileNotFound(promise: Promise, filepath: String?) {
        promise.reject("ENOENT", "ENOENT: no such file or directory, open '$filepath'")
    }

    private fun rejectFileIsDirectory(promise: Promise) {
        promise.reject("EISDIR", "EISDIR: illegal operation on a directory, read")
    }

    companion object {
        const val NAME = NativeReactNativeFsSpec.NAME

        @Throws(IOException::class)
        private fun getInputStreamBytes(inputStream: InputStream): ByteArray {
            var bytesResult: ByteArray
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            ByteArrayOutputStream().use { byteBuffer ->
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    byteBuffer.write(buffer, 0, len)
                }
                bytesResult = byteBuffer.toByteArray()
            }
            return bytesResult
        }
    }
}
