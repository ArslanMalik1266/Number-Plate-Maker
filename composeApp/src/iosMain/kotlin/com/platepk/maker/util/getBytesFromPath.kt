package com.platepk.maker.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class)
actual fun getBytesFromPath(path: String?): ByteArray? {
    if (path == null) return null

    val data = NSData.dataWithContentsOfFile(path) ?: return null

    // NSData ko ByteArray mein convert karna
    val bytes = ByteArray(data.length.toInt())
    if (bytes.isNotEmpty()) {
        data.bytes?.let { pointer ->
            bytes.usePinned { pinned ->
                platform.posix.memcpy(pinned.addressOf(0), pointer, data.length)
            }
        }
    }
    return bytes
}