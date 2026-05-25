package com.platepk.maker.domain.repo

import com.platepk.maker.domain.models.ExportFormat

interface PlateExportRepository {
    suspend fun export(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat,
        registrationNumber: String,
        vehicleType: String
    ): Result<String>

    suspend fun savePdf(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        registrationNumber: String,
        vehicleType: String
    ): String

    suspend fun savePlateFile(bitmapData: ByteArray, fileName: String): String

}