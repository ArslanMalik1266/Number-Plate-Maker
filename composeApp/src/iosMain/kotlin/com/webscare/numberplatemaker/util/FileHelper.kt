package com.webscare.numberplatemaker.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual suspend fun saveBitmapToInternalStorage(bitmapData: ByteArray, fileName: String): String {
    val fileManager = NSFileManager.defaultManager
    val paths = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
    val documentDirectory = paths.first() as NSURL
    val fileURL = documentDirectory.URLByAppendingPathComponent("$fileName.png")

    val data = bitmapData.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), bitmapData.size.toULong())
    }

    return if (data.writeToURL(fileURL!!, true)) {
        fileURL.path ?: ""
    } else {
        ""
    }
}