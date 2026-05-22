package com.webscare.numberplatemaker.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.webscare.numberplatemaker.domain.repo.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {
    private val DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")

    override val isDarkMode: Flow<Boolean> = dataStore.data.map { it[DARK_MODE_KEY] ?: false }

    override suspend fun setDarkMode(isDark: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = isDark }
    }
}