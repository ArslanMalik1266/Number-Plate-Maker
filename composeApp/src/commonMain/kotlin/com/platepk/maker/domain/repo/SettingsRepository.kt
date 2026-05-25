package com.platepk.maker.domain.repo

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val isDarkMode: Flow<Boolean>
    suspend fun setDarkMode(isDark: Boolean)
}