package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.repo.PlateDataRepository

class DeleteAllPlatesUseCase(private val repository: PlateDataRepository) {
    suspend operator fun invoke() = repository.deleteAllPlates()
}