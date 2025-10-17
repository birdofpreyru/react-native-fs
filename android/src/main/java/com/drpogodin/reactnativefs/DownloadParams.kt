package com.drpogodin.reactnativefs

import com.facebook.react.bridge.ReadableMap
import java.io.File
import java.net.URL

class DownloadParams {
    interface OnTaskCompleted {
        fun onTaskCompleted(res: DownloadResult?)
    }

    interface OnDownloadBegin {
        fun onDownloadBegin(statusCode: Int, contentLength: Long, headers: ReadableMap)
    }

    interface OnDownloadProgress {
        fun onDownloadProgress(contentLength: Long, bytesWritten: Long)
    }

    @JvmField
    var src: URL? = null
    @JvmField
    var dest: File? = null
    @JvmField
    var headers: ReadableMap? = null
    @JvmField
    var progressInterval = 0
    @JvmField
    var progressDivider = 0f
    @JvmField
    var readTimeout = 0
    @JvmField
    var connectionTimeout = 0
    @JvmField
    var onTaskCompleted: OnTaskCompleted? = null
    @JvmField
    var onDownloadBegin: OnDownloadBegin? = null
    @JvmField
    var onDownloadProgress: OnDownloadProgress? = null
}
