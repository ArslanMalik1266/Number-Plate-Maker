package com.platepk.maker.util

actual fun readFileBytes(path: String): ByteArray {
    return java.io.File(path).readBytes()
}