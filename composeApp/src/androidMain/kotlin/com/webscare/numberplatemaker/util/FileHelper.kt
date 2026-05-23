package com.webscare.numberplatemaker.util

import com.webscare.numberplatemaker.data.local.DatabaseBuilder.appContext
import java.io.File

actual suspend fun saveBitmapToInternalStorage(bitmapData: ByteArray, fileName: String): String {
    val file = File(appContext.filesDir, "$fileName.png")

    return try {
        file.writeBytes(bitmapData)
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}