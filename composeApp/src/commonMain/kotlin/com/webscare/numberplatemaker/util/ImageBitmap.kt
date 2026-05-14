package com.webscare.numberplatemaker.util

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun ImageBitmap.toByteArray(): ByteArray