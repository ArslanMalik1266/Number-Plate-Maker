package com.webscare.numberplatemaker.domain.repo

import com.webscare.numberplatemaker.domain.models.ExportFormat

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
}