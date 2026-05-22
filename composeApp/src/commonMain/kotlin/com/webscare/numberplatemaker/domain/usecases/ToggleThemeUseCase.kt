package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.repo.SettingsRepository

class ToggleThemeUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(isDark: Boolean) {
        repository.setDarkMode(isDark)
    }
}