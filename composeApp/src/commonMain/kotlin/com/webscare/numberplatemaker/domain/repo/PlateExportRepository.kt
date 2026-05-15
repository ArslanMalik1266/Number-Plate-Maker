package com.webscare.numberplatemaker.domain.repo

import com.webscare.numberplatemaker.domain.models.ExportFormat

interface PlateExportRepository {
    suspend fun export(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ): Result<String>
}