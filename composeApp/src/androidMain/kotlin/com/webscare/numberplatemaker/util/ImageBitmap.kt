package com.webscare.numberplatemaker.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

actual suspend fun ImageBitmap.toByteArray(): ByteArray {
    val bitmap = this.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    // PNG format use karein taake quality kharab na ho
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}