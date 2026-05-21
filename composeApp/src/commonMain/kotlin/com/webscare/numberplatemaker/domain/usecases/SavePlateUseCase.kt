package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.data.local.entity.PlateEntity
import com.webscare.numberplatemaker.domain.repo.PlateDataRepository

class SavePlateUseCase(private val repository: PlateDataRepository) {
    suspend operator fun invoke(plate: PlateEntity) = repository.insertPlate(plate)
}