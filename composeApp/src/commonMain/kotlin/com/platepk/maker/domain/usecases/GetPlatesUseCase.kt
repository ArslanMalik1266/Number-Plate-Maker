package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.repo.PlateDataRepository

class GetPlatesUseCase(private val repository: PlateDataRepository) {
    operator fun invoke() = repository.getAllPlates()
}