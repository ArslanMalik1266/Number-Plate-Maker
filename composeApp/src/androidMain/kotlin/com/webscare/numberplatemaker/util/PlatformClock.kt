package com.webscare.numberplatemaker.util

actual class PlatformClock {
    actual fun getCurrentMillis(): Long {
        return System.currentTimeMillis()
    }
}