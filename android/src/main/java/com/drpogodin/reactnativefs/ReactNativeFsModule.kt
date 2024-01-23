package com.drpogodin.reactnativefs

import android.content.res.AssetManager
import android.database.Cursor
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.util.Base64
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
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import java.io.ByteArrayOutputStream
import java.io.Closeable
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
class ReactNativeFsModule internal constructor(context: ReactApplicationContext) :
  ReactNativeFsSpec(context) {
    private val downloaders = SparseArray<Downloader>()
    private val uploaders = SparseArray<Uploader>()
    private val pendingPickFilePromises = ArrayDeque<Promise>()
    private var pickFileLauncher: ActivityResultLauncher<Array<String>>? = null
    private fun getPickFileLauncher(): ActivityResultLauncher<Array<String>> {
        if (pickFileLauncher == null) {
            val activity = getCurrentActivity() as ReactActivity
            val registry = activity.activityResultRegistry
            pickFileLauncher = registry.register<Array<String>, Uri>(
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
        constants["DocumentDirectoryPath"] = this.getReactApplicationContext().getFilesDir().getAbsolutePath()
        constants["TemporaryDirectoryPath"] = this.getReactApplicationContext().getCacheDir().getAbsolutePath()
        constants["PicturesDirectoryPath"] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        constants["CachesDirectoryPath"] = this.getReactApplicationContext().getCacheDir().getAbsolutePath()
        constants["DownloadDirectoryPath"] = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
        constants["FileTypeRegular"] = 0
        constants["FileTypeDirectory"] = 1
        constants["ExternalStorageDirectoryPath"] = Environment.getExternalStorageDirectory()?.absolutePath
        constants["ExternalDirectoryPath"] = this.getReactApplicationContext().getExternalFilesDir(null)?.absolutePath
        constants["ExternalCachesDirectoryPath"] = this.getReactApplicationContext().getExternalCacheDir()?.absolutePath
        return constants
    }

    @ReactMethod
    override fun addListener(eventName: String?) {
        // NOOP
    }

    @ReactMethod
    override fun appendFile(filepath: String, base64Content: String?, promise: Promise) {
        try {
            getOutputStream(filepath, true).use { outputStream ->
                val bytes = Base64.decode(base64Content, Base64.DEFAULT)
                outputStream!!.write(bytes)
                promise.resolve(null)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
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

    @ReactMethod
    override fun copyAssetsVideoIOS(imageUri: String?, destPath: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "copyAssetsVideoIOS()")
    }

    @ReactMethod
    override fun completeHandlerIOS(jobId: Double) {
        // TODO: It is iOS-only. We need at least Promise here,
        // to reject.
    }

    @ReactMethod
    override fun copyFile(filepath: String?, destPath: String?, options: ReadableMap?, promise: Promise) {
        object : CopyFileTask() {
            protected override fun onPostExecute(ex: Exception?) {
                if (ex == null) {
                    promise.resolve(null)
                } else {
                    ex.printStackTrace()
                    reject(promise, filepath, ex)
                }
            }
        }.execute(filepath, destPath)
    }

    @ReactMethod
    override fun copyFileAssets(assetPath: String, destination: String, promise: Promise) {
        val assetManager: AssetManager = getReactApplicationContext().getAssets()
        try {
            val `in` = assetManager.open(assetPath)
            copyInputStream(`in`, assetPath, destination, promise)
        } catch (e: IOException) {
            // Default error message is just asset name, so make a more helpful error here.
            reject(promise, assetPath, Exception(String.format("Asset '%s' could not be opened", assetPath)))
        }
    }

    @ReactMethod
    override fun copyFileRes(filename: String, destination: String, promise: Promise) {
        try {
            val res = getResIdentifier(filename)
            val `in`: InputStream = getReactApplicationContext().getResources().openRawResource(res)
            copyInputStream(`in`, filename, destination, promise)
        } catch (e: Exception) {
            reject(promise, filename, Exception(String.format("Res '%s' could not be opened", filename)))
        }
    }

    // TODO: As of now it is meant to be Windows-only.
    @ReactMethod
    override fun copyFolder(from: String?, to: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "copyFolder()")
    }

    @ReactMethod
    override fun downloadFile(options: ReadableMap, promise: Promise) {
        try {
            val file = File(options.getString("toFile"))
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
                        promise.resolve(infoMap)
                    } else {
                        reject(promise, options.getString("toFile"), res.exception)
                    }
                }
            }
            if (hasBeginCallback) {
                params.onDownloadBegin = object : OnDownloadBegin {
                    override fun onDownloadBegin(statusCode: Int, contentLength: Long, headers: Map<String, String?>?) {
                        val headersMap = Arguments.createMap()
                        for ((key, value) in headers!!) {
                            headersMap.putString(key!!, value)
                        }
                        val data = Arguments.createMap()
                        data.putInt("jobId", jobId)
                        data.putInt("statusCode", statusCode)
                        data.putDouble("contentLength", contentLength.toDouble())
                        data.putMap("headers", headersMap)
                        sendEvent(getReactApplicationContext(), "DownloadBegin", data)
                    }
                }
            }
            if (hasProgressCallback) {
                params.onDownloadProgress = object : OnDownloadProgress {
                    override fun onDownloadProgress(contentLength: Long, bytesWritten: Long) {
                        val data = Arguments.createMap()
                        data.putInt("jobId", jobId)
                        data.putDouble("contentLength", contentLength.toDouble())
                        data.putDouble("bytesWritten", bytesWritten.toDouble())
                        sendEvent(getReactApplicationContext(), "DownloadProgress", data)
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

    @ReactMethod
    override fun exists(filepath: String?, promise: Promise) {
        try {
            val file = File(filepath)
            promise.resolve(file.exists())
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun existsAssets(filepath: String?, promise: Promise) {
        try {
            val assetManager: AssetManager = getReactApplicationContext().getAssets()
            try {
                val list = assetManager.list(filepath!!)
                if (list != null && list.size > 0) {
                    promise.resolve(true)
                    return
                }
            } catch (ignored: Exception) {
                //.. probably not a directory then
            }

            // Attempt to open file (win = exists)
            try {
                assetManager.open(filepath!!).use { fileStream -> promise.resolve(true) }
            } catch (ex: Exception) {
                promise.resolve(false) // don't throw an error, resolve false
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
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

    @ReactMethod
    override fun getAllExternalFilesDirs(promise: Promise) {
        val allExternalFilesDirs: Array<File> = this.getReactApplicationContext().getExternalFilesDirs(null)
        val fs = Arguments.createArray()
        for (f in allExternalFilesDirs) {
            if (f != null) {
                fs.pushString(f.absolutePath)
            }
        }
        promise.resolve(fs)
    }

    @ReactMethod
    override fun getFSInfo(promise: Promise) {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val statEx = StatFs(Environment.getExternalStorageDirectory().path)
        val totalSpace: Long
        val freeSpace: Long
        var totalSpaceEx: Long = 0
        var freeSpaceEx: Long = 0
        if (Build.VERSION.SDK_INT >= 18) {
            totalSpace = stat.totalBytes
            freeSpace = stat.freeBytes
            totalSpaceEx = statEx.totalBytes
            freeSpaceEx = statEx.freeBytes
        } else {
            val blockSize = stat.blockSize.toLong()
            totalSpace = blockSize * stat.blockCount
            freeSpace = blockSize * stat.availableBlocks
        }
        val info = Arguments.createMap()
        info.putDouble("totalSpace", totalSpace.toDouble()) // Int32 too small, must use Double
        info.putDouble("freeSpace", freeSpace.toDouble())
        info.putDouble("totalSpaceEx", totalSpaceEx.toDouble())
        info.putDouble("freeSpaceEx", freeSpaceEx.toDouble())
        promise.resolve(info)
    }

    @ReactMethod
    override fun hash(filepath: String?, algorithm: String, promise: Promise) {
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
            val md = MessageDigest.getInstance(algorithms[algorithm])
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
            closeIgnoringException(inputStream)
        }
    }

    @ReactMethod
    override fun isResumable(jobId: Double, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "isResumable()")
    }

    @ReactMethod
    override fun mkdir(filepath: String?, options: ReadableMap?, promise: Promise) {
        try {
            val file = File(filepath)
            file.mkdirs()
            val exists = file.exists()
            if (!exists) throw Exception("Directory could not be created")
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun moveFile(filepath: String?, destPath: String?, options: ReadableMap?, promise: Promise) {
        try {
            val inFile = File(filepath)
            if (!inFile.renameTo(File(destPath))) {
                object : CopyFileTask() {
                    protected override fun onPostExecute(ex: Exception?) {
                        if (ex == null) {
                            inFile.delete()
                            promise.resolve(true)
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

    @ReactMethod
    override fun pathForBundle(bundle: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "pathForBundle()")
    }

    @ReactMethod
    override fun pathForGroup(group: String?, promise: Promise?) {
        Errors.NOT_IMPLEMENTED.reject(promise, "pathForGroup()")
    }

    @ReactMethod
    override fun pickFile(options: ReadableMap, promise: Promise) {
        val mimeTypesArray = options.getArray("mimeTypes")
        val mimeTypes = arrayOfNulls<String>(mimeTypesArray!!.size())
        for (i in 0 until mimeTypesArray.size()) {
            mimeTypes[i] = mimeTypesArray.getString(i)
        }

        // Note: Here we assume that if a new pickFile() call is done prior to
        // the previous one having been completed, effectivly the new call with
        // open a new file picker on top of the view stack (thus, on top of
        // the one opened for the previous call), thus just keeping all pending
        // promises in FILO stack we should be able to resolve them in the correct
        // order.
        pendingPickFilePromises.push(promise)
        getPickFileLauncher().launch(mimeTypes as Array<String>)
    }

    @ReactMethod
    override fun read(
            filepath: String,
            length: Double,
            position: Double,
            promise: Promise
    ) {
        try {
            getInputStream(filepath).use { inputStream ->
                val buffer = ByteArray(length.toInt())
                inputStream!!.skip(position.toInt().toLong())
                val bytesRead = inputStream.read(buffer, 0, length.toInt())
                val base64Content = Base64.encodeToString(buffer, 0, bytesRead, Base64.NO_WRAP)
                promise.resolve(base64Content)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun readDir(directory: String?, promise: Promise) {
        try {
            val file = File(directory)
            if (!file.exists()) throw Exception("Folder does not exist")
            val files = file.listFiles()
            val fileMaps = Arguments.createArray()
            for (childFile in files) {
                val fileMap = Arguments.createMap()
                fileMap.putDouble("mtime", childFile.lastModified().toDouble() / 1000)
                fileMap.putString("name", childFile.name)
                fileMap.putString("path", childFile.absolutePath)
                fileMap.putDouble("size", childFile.length().toDouble())
                fileMap.putInt("type", if (childFile.isDirectory) 1 else 0)
                fileMaps.pushMap(fileMap)
            }
            promise.resolve(fileMaps)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, directory, ex)
        }
    }

    @ReactMethod
    override fun readDirAssets(directory: String, promise: Promise) {
        try {
            val assetManager: AssetManager = getReactApplicationContext().getAssets()
            val list = assetManager.list(directory)
            val fileMaps = Arguments.createArray()
            for (childFile in list!!) {
                val fileMap = Arguments.createMap()
                fileMap.putString("name", childFile)
                val path = if (directory.isEmpty()) childFile else String.format("%s/%s", directory, childFile) // don't allow / at the start when directory is ""
                fileMap.putString("path", path)
                var length = -1
                var isDirectory = true
                try {
                    val assetFileDescriptor = assetManager.openFd(path!!)
                    if (assetFileDescriptor != null) {
                        length = assetFileDescriptor.length.toInt()
                        assetFileDescriptor.close()
                        isDirectory = false
                    }
                } catch (ex: IOException) {
                    //.. ah.. is a directory or a compressed file?
                    isDirectory = !ex.message!!.contains("compressed")
                }
                fileMap.putInt("size", length)
                fileMap.putInt("type", if (isDirectory) 1 else 0) // if 0, probably a folder..
                fileMaps.pushMap(fileMap)
            }
            promise.resolve(fileMaps)
        } catch (e: IOException) {
            reject(promise, directory, e)
        }
    }

    @ReactMethod
    override fun readFile(filepath: String, promise: Promise) {
        try {
            getInputStream(filepath).use { inputStream ->
                val inputData = getInputStreamBytes(inputStream!!)
                val base64Content = Base64.encodeToString(inputData, Base64.NO_WRAP)
                promise.resolve(base64Content)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun readFileAssets(filepath: String?, promise: Promise) {
        var stream: InputStream? = null
        try {
            // ensure isn't a directory
            val assetManager: AssetManager = getReactApplicationContext().getAssets()
            stream = assetManager.open(filepath!!, 0)
            if (stream == null) {
                reject(promise, filepath, Exception("Failed to open file"))
                return
            }
            val buffer = ByteArray(stream.available())
            stream.read(buffer)
            val base64Content = Base64.encodeToString(buffer, Base64.NO_WRAP)
            promise.resolve(base64Content)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        } finally {
            closeIgnoringException(stream)
        }
    }

    @ReactMethod
    override fun readFileRes(filename: String, promise: Promise) {
        var stream: InputStream? = null
        try {
            val res = getResIdentifier(filename)
            stream = getReactApplicationContext().getResources().openRawResource(res)
            if (stream == null) {
                reject(promise, filename, Exception("Failed to open file"))
                return
            }
            val buffer = ByteArray(stream.available())
            stream.read(buffer)
            val base64Content = Base64.encodeToString(buffer, Base64.NO_WRAP)
            promise.resolve(base64Content)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filename, ex)
        } finally {
            closeIgnoringException(stream)
        }
    }

    @ReactMethod
    override fun removeListeners(count: Double) {
        // NOOP
    }

    @ReactMethod
    override fun resumeDownload(jobId: Double) {
        // TODO: This is currently iOS-only method,
        // and worse it does not return a promise,
        // thus we even can't cleanly reject it here.
        // At least add the Promise here.
    }

    @ReactMethod
    override fun scanFile(path: String, promise: Promise) {
        MediaScannerConnection.scanFile(this.getReactApplicationContext(), arrayOf<String>(path),
                null,
                object : MediaScannerConnectionClient {
                    override fun onMediaScannerConnected() {}
                    override fun onScanCompleted(path: String, uri: Uri?) {
                        promise.resolve(uri.toString())
                    }
                }
        )
    }

    @ReactMethod
    override fun setReadable(
            filepath: String?,
            readable: Boolean,
            ownerOnly: Boolean,
            promise: Promise
    ) {
        try {
            val file = File(filepath)
            if (!file.exists()) throw Exception("File does not exist")
            file.setReadable(readable, ownerOnly)
            promise.resolve(true)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun stat(filepath: String, promise: Promise) {
        try {
            val originalFilepath = getOriginalFilepath(filepath, true)
            val file = File(originalFilepath)
            if (!file.exists()) throw Exception("File does not exist")
            val statMap = Arguments.createMap()
            statMap.putInt("ctime", (file.lastModified() / 1000).toInt())
            statMap.putInt("mtime", (file.lastModified() / 1000).toInt())
            statMap.putDouble("size", file.length().toDouble())
            statMap.putInt("type", if (file.isDirectory) 1 else 0)
            statMap.putString("originalFilepath", originalFilepath)
            promise.resolve(statMap)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun stopDownload(jobId: Double) {
        val downloader = downloaders[jobId.toInt()]
        downloader?.stop()
    }

    @ReactMethod
    override fun stopUpload(jobId: Double) {
        val uploader = uploaders[jobId.toInt()]
        uploader?.stop()
    }

    @ReactMethod
    override fun touch(filepath: String?, options: ReadableMap, promise: Promise) {
        try {
            val file = File(filepath)
            val mtime = options.getDouble("mtime").toLong()
            // TODO: setLastModified() returns "true" on success, "false" otherwise,
            // thus instead of resolving with its result, we should throw if result is
            // false.
            promise.resolve(file.setLastModified(mtime))
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
    override fun unlink(filepath: String?, promise: Promise) {
        try {
            val file = File(filepath)
            if (!file.exists()) throw Exception("File does not exist")
            DeleteRecursive(file)
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    @ReactMethod
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
                fileList.add(files.getMap(i))
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
                        val data = Arguments.createMap()
                        data.putInt("jobId", jobId)
                        sendEvent(getReactApplicationContext(), "UploadBegin", data)
                    }
                }
            }
            if (hasProgressCallback) {
                params.onUploadProgress = object : UploadParams.OnUploadProgress {
                    override fun onUploadProgress(totalBytesExpectedToSend: Int, totalBytesSent: Int) {
                        val data = Arguments.createMap()
                        data.putInt("jobId", jobId)
                        data.putInt("totalBytesExpectedToSend", totalBytesExpectedToSend)
                        data.putInt("totalBytesSent", totalBytesSent)
                        sendEvent(getReactApplicationContext(), "UploadProgress", data)
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
    @ReactMethod
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
                outputStream!!.write(bytes)
            } else {
                file = RandomAccessFile(filepath, "rw")
                file.seek(position.toLong())
                file.write(bytes)
            }
            promise.resolve(null)
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        } finally {
            closeIgnoringException(outputStream)
            closeIgnoringException(file)
        }
    }

    @ReactMethod
    override fun writeFile(filepath: String, base64Content: String?, options: ReadableMap?, promise: Promise) {
        try {
            getOutputStream(filepath, false).use { outputStream ->
                val bytes = Base64.decode(base64Content, Base64.DEFAULT)
                outputStream!!.write(bytes)
                promise.resolve(null)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            reject(promise, filepath, ex)
        }
    }

    private open inner class CopyFileTask : AsyncTask<String?, Void?, Exception?>() {
        protected override fun doInBackground(vararg paths: String?): Exception? {
            var `in`: InputStream? = null
            var out: OutputStream? = null
            return try {
                val filepath = paths[0]!!
                val destPath = paths[1]!!
                `in` = getInputStream(filepath)
                out = getOutputStream(destPath, false)
                val buffer = ByteArray(1024)
                var length: Int
                while (`in`!!.read(buffer).also { length = it } > 0) {
                    out!!.write(buffer, 0, length)
                    Thread.yield()
                }
                null
            } catch (ex: Exception) {
                ex
            } finally {
                closeIgnoringException(`in`)
                closeIgnoringException(out)
            }
        }
    }

    /**
     * Internal method for copying that works with any InputStream
     *
     * @param in          InputStream from assets or file
     * @param source      source path (only used for logging errors)
     * @param destination destination path
     * @param promise     React Callback
     */
    private fun copyInputStream(`in`: InputStream, source: String, destination: String, promise: Promise) {
        var out: OutputStream? = null
        try {
            out = getOutputStream(destination, false)
            val buffer = ByteArray(1024 * 10) // 10k buffer
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out!!.write(buffer, 0, read)
            }

            // Success!
            promise.resolve(null)
        } catch (ex: Exception) {
            reject(promise, source, Exception(String.format("Failed to copy '%s' to %s (%s)", source, destination, ex.localizedMessage)))
        } finally {
            closeIgnoringException(`in`)
            closeIgnoringException(out)
        }
    }

    private fun DeleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()) {
                DeleteRecursive(child)
            }
        }
        fileOrDirectory.delete()
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
    private fun getInputStream(filepath: String): InputStream? {
        val uri = getFileUri(filepath, false)
        val stream: InputStream?
        stream = try {
            getReactApplicationContext().getContentResolver().openInputStream(uri)
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
                val cursor: Cursor = getReactApplicationContext().getContentResolver().query(uri, null, null, null, null)!!
                if (cursor.moveToFirst()) {
                    originalFilepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                }
                cursor.close()
            } catch (ignored: IllegalArgumentException) {
            }
        }
        return originalFilepath
    }

    @Throws(IORejectionException::class)
    private fun getOutputStream(filepath: String, append: Boolean): OutputStream? {
        val uri = getFileUri(filepath, false)
        val stream: OutputStream?
        stream = try {
            getReactApplicationContext().getContentResolver().openOutputStream(uri, if (append) "wa" else writeAccessByAPILevel)
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
        return getReactApplicationContext().getResources().getIdentifier(name, if (isImage) "drawable" else "raw", getReactApplicationContext().getPackageName())
    }

    private val writeAccessByAPILevel: String
        private get() = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) "w" else "rwt"

    // TODO: These should be merged / replaced by the dedicated "Errors" module.
    private fun reject(promise: Promise, filepath: String?, ex: Exception?) {
        if (ex is FileNotFoundException) {
            rejectFileNotFound(promise, filepath)
            return
        }
        if (ex is IORejectionException) {
            val ioRejectionException = ex
            promise.reject(ioRejectionException.code, ioRejectionException.message)
            return
        }
        promise.reject(null, ex!!.message)
    }

    private fun rejectFileNotFound(promise: Promise, filepath: String?) {
        promise.reject("ENOENT", "ENOENT: no such file or directory, open '$filepath'")
    }

    private fun rejectFileIsDirectory(promise: Promise) {
        promise.reject("EISDIR", "EISDIR: illegal operation on a directory, read")
    }

    private fun sendEvent(reactContext: ReactContext, eventName: String, params: WritableMap) {
        val emitter: DeviceEventManagerModule.RCTDeviceEventEmitter = getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        emitter.emit(eventName, params)
    }

    companion object {
        const val NAME = "ReactNativeFs"

        /**
         * Attempts to close given object, discarting possible exception. Does nothing
         * if given argument is null.
         * @param closeable
         */
        private fun closeIgnoringException(closeable: Closeable?) {
            if (closeable != null) {
                try {
                    closeable.close()
                } catch (e: Exception) {
                }
            }
        }

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

  override fun getName(): String {
    return NAME
  }
}
