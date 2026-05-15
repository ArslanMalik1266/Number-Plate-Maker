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
        if (format == ExportFormat.PDF) {
            // PDF logic: Dono images ek hi document mein
            helper.savePdf(frontImageData, backImageData)
        } else {
            helper.saveImage(
                frontData = frontImageData,
                backData = backImageData,
                format = format
            )
        }
    }
}