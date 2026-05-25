package com.platepk.maker.util

import android.content.pm.PackageInfo
import android.os.Build
import com.platepk.maker.data.local.DatabaseBuilder.appContext

actual fun getAppVersion(): String {
    return try {
        val context = appContext  // Ya Koin se inject karo
        val packageInfo: PackageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                android.content.pm.PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }

        val versionName = packageInfo.versionName ?: "Unknown"
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            packageInfo.versionCode.toLong()
        }

        "$versionName"
    } catch (e: Exception) {
        "Unknown"
    }
}