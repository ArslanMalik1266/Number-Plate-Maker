package com.platepk.maker.domain.models

data class SettingsUiState(
    val savedCount: Int = 0,
    val isDarkMode: Boolean = false,
    val selectedThemeIndex: Int = 0
)