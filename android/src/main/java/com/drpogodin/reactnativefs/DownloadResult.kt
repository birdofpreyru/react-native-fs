package com.drpogodin.reactnativefs

import com.facebook.react.bridge.WritableMap

class DownloadResult {
    @JvmField
    var statusCode = 0
    @JvmField
    var bytesWritten: Long = 0
    @JvmField
    var headers: WritableMap? = null
    @JvmField
    var body: String? = null
    @JvmField
    var exception: Exception? = null
}
