package com.webscare.numberplatemaker.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
import com.webscare.numberplatemaker.domain.models.ExportFormat
import java.io.ByteArrayOutputStream

actual suspend fun ImageBitmap.toByteArray(format: ExportFormat): ByteArray {
    val stream = java.io.ByteArrayOutputStream()
    val androidBitmap = this.asAndroidBitmap()

    val compressFormat = when (format) {
        ExportFormat.PNG -> android.graphics.Bitmap.CompressFormat.PNG
        ExportFormat.JPEG -> android.graphics.Bitmap.CompressFormat.JPEG
        else -> android.graphics.Bitmap.CompressFormat.PNG
    }

    // Quality 100 aur PNG format transparency ko barkrar rakhta hai
    androidBitmap.compress(compressFormat, 100, stream)
    return stream.toByteArray()
}