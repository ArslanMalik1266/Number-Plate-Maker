package com.webscare.numberplatemaker.util

import com.webscare.numberplatemaker.data.local.DatabaseBuilder.appContext
import java.io.File

actual suspend fun saveBitmapToInternalStorage(bitmapData: ByteArray, fileName: String): String {
    val file = File(appContext.filesDir, fileName)
    file.writeBytes(bitmapData)
    return file.absolutePath
}