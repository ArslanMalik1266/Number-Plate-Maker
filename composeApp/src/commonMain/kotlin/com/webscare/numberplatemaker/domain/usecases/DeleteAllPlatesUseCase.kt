package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.repo.PlateDataRepository

class DeleteAllPlatesUseCase(private val repository: PlateDataRepository) {
    suspend operator fun invoke() = repository.deleteAllPlates()
}