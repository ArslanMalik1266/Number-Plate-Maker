package com.platepk.maker.util

import platform.Foundation.dateWithTimeIntervalSince1970

actual fun formatTimestamp(timestamp: Long): String {
    val date = platform.Foundation.NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
    val formatter = platform.Foundation.NSDateFormatter()
    formatter.dateFormat = "dd MMM yy · HH:mm"
    return formatter.stringFromDate(date)
}