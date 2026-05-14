package com.webscare.numberplatemaker.util

import com.webscare.numberplatemaker.domain.models.ExportFormat

expect class PlatformExportHelper {
    suspend fun saveImage(
        frontData: ByteArray,
        backData: ByteArray,
        format: ExportFormat
    ): String

    suspend fun savePdf(
        frontData: ByteArray,
        backData: ByteArray
    ): String
}