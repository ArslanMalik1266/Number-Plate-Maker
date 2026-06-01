package com.platepk.maker.util

import androidx.compose.ui.graphics.ImageBitmap

actual fun ImageBitmap.toSoftwareBitmap(): ImageBitmap {
    // iOS mein hardware/software distinction nahi hoti
    // ImageBitmap already software rendering use karta hai
    return this
}