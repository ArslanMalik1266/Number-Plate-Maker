package com.platepk.maker.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.platepk.maker.domain.models.ExportFormat

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