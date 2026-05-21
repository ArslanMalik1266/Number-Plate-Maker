package com.webscare.numberplatemaker.util

expect class PlatformClock() {
    fun getCurrentMillis(): Long
}