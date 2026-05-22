package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkMode
}