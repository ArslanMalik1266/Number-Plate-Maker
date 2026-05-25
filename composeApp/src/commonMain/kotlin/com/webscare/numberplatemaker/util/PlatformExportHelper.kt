package com.webscare.numberplatemaker.util

import coil3.annotation.InternalCoilApi
import coil3.util.MimeTypeMap
import com.webscare.numberplatemaker.domain.models.ExportFormat

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