package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeSettingsUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<Boolean> = repository.isDarkMode
}