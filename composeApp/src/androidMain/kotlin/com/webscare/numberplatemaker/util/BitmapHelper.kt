package com.webscare.numberplatemaker.util

import android.graphics.Bitmap
import android.graphics.Canvas
import com.webscare.numberplatemaker.domain.models.PlateConfig
import java.io.ByteArrayOutputStream

actual fun PlateConfig.toBitmapByteArray(): ByteArray {
    val bitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)


    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}