package com.webscare.numberplatemaker.util

actual fun readFileBytes(path: String): ByteArray {
    return java.io.File(path).readBytes()
}