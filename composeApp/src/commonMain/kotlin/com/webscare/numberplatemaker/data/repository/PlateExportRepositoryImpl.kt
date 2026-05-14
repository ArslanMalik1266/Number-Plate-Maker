package com.webscare.numberplatemaker.data.repository

import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.util.PlatformExportHelper

class PlateExportRepositoryImpl(
    private val helper: PlatformExportHelper
) : PlateExportRepository {

    override suspend fun export(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ): Result<String> = runCatching {
        when (format) {
            ExportFormat.PDF -> helper.savePdf(frontImageData, backImageData)
            else -> helper.saveImage(frontImageData, backImageData, format)
        }
    }
}