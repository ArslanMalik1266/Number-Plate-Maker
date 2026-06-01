package com.platepk.maker.util

import java.io.File

actual fun getBytesFromPath(path: String?): ByteArray? {
    return path?.let {
        val file = File(it)
        if (file.exists()) file.readBytes() else null
    }
}