package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository

class ExportPlateUseCase(
    private val repository: PlateExportRepository
) {
    suspend operator fun invoke(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat,
        registrationNumber: String,
        vehicleType: String
    ): Result<String> = repository.export(
        frontImageData = frontImageData,
        backImageData = backImageData,
        format = format,
        registrationNumber = registrationNumber,
        vehicleType = vehicleType
    )
}