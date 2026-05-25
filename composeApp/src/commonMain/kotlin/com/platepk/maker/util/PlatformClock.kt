package com.platepk.maker.util

expect class PlatformClock() {
    fun getCurrentMillis(): Long
}