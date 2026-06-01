package com.platepk.maker.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun ImageBitmap.toSoftwareBitmap(): ImageBitmap {
    return this.asAndroidBitmap()
        .copy(android.graphics.Bitmap.Config.ARGB_8888, false)
        .asImageBitmap()
}