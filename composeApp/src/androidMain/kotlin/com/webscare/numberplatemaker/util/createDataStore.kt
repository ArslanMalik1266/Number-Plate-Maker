package com.webscare.numberplatemaker.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun createDataStore(): DataStore<Preferences> {
    val koinComponent = object : KoinComponent {}
    val context: Context by koinComponent.inject()

    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            "${context.filesDir.path}/settings.preferences_pb".toPath()
        }
    )
}