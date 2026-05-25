package com.platepk.maker.domain.usecases

import com.platepk.maker.data.local.entity.PlateEntity
import com.platepk.maker.domain.repo.PlateDataRepository

class DeletePlateUseCase(private val repository: PlateDataRepository) {
    suspend operator fun invoke(plate: PlateEntity) = repository.deletePlate(plate)
}