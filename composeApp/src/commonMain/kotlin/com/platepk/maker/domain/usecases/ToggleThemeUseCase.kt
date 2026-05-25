package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.repo.SettingsRepository

class ToggleThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(isDark: Boolean) {
        repository.setDarkMode(isDark)
    }
}