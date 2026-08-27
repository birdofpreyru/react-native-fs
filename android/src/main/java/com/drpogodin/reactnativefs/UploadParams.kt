package com.drpogodin.reactnativefs

import com.facebook.react.bridge.ReadableMap
import java.net.URL

class UploadParams {
    interface OnUploadComplete {
        fun onUploadComplete(res: UploadResult)
    }

    interface OnUploadProgress {
        fun onUploadProgress(totalBytesExpectedToSend: Long, totalBytesSent: Long)
    }

    interface OnUploadBegin {
        fun onUploadBegin()
    }

    @JvmField
    var src: URL? = null
    @JvmField
    var files: ArrayList<ReadableMap>? = null
    @JvmField
    var binaryStreamOnly = false
    var name: String? = null
    @JvmField
    var headers: ReadableMap? = null
    @JvmField
    var fields: ReadableMap? = null
    @JvmField
    var method: String? = null
    @JvmField
    var onUploadComplete: OnUploadComplete? = null
    @JvmField
    var onUploadProgress: OnUploadProgress? = null
    @JvmField
    var onUploadBegin: OnUploadBegin? = null
}
