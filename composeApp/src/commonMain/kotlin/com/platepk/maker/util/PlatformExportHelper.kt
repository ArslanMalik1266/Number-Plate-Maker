package com.platepk.maker.util

import com.platepk.maker.domain.models.ExportFormat

expect class PlatformExportHelper {
    suspend fun saveImage(
        frontData: ByteArray,
        backData: ByteArray,
        format: ExportFormat,
    ): String

    suspend fun savePdf(
        frontData: ByteArray,
        backData: ByteArray,
        registrationNumber: String,
        vehicleType: String
    ): String
}
expect object MimeTypeHelper {
    fun getMimeType(filePath: String): String?
}