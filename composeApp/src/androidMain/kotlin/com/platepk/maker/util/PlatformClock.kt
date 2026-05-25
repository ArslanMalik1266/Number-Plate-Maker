package com.platepk.maker.util

actual class PlatformClock {
    actual fun getCurrentMillis(): Long {
        return System.currentTimeMillis()
    }
}