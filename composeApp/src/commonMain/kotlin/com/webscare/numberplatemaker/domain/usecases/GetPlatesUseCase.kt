package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.repo.PlateDataRepository

class GetPlatesUseCase(private val repository: PlateDataRepository) {
    operator fun invoke() = repository.getAllPlates()
}