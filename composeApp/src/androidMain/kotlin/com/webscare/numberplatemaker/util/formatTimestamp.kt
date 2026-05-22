package com.webscare.numberplatemaker.util

actual fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd MMM yy · HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}