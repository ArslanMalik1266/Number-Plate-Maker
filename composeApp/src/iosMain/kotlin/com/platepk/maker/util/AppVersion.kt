package com.platepk.maker.util

import platform.Foundation.NSBundle

actual fun getAppVersion(): String {
    return NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.0"
}