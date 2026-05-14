package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository

class ExportPlateUseCase(
    private val repository: PlateExportRepository
) {
    suspend operator fun invoke(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ): Result<String> = repository.export(frontImageData, backImageData, format)
}