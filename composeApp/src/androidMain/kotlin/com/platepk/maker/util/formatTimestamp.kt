package com.platepk.maker.util

actual fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yy · HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}