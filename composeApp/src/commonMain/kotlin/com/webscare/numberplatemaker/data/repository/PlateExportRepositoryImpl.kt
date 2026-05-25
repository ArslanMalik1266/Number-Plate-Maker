package com.webscare.numberplatemaker.data.repository

import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.util.PlatformExportHelper
import com.webscare.numberplatemaker.util.saveBitmapToInternalStorage

class PlateExportRepositoryImpl(
    private val helper: PlatformExportHelper
) : PlateExportRepository {

    override suspend fun export(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat,
        registrationNumber: String,
        vehicleType: String

    ): Result<String> = runCatching {
        helper.saveImage(
            frontData = frontImageData,
            backData = backImageData,
            format = format
        )
    }

    override suspend fun savePdf(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        registrationNumber: String,
        vehicleType: String
    ): String {
        return helper.savePdf(
            frontData = frontImageData,
            backData = backImageData,
            registrationNumber = registrationNumber,
            vehicleType = vehicleType
        )
    }
    override suspend fun savePlateFile(bitmapData: ByteArray, fileName: String): String {
        val nameWithExt = if (fileName.endsWith(".png")) fileName else "$fileName.png"
        return saveBitmapToInternalStorage(bitmapData, nameWithExt)
    }


}