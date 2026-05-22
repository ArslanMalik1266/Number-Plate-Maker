package com.webscare.numberplatemaker.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.webscare.numberplatemaker.data.local.AppDatabase.AppDatabase
import com.webscare.numberplatemaker.data.local.DatabaseBuilder.getDatabaseBuilder
import com.webscare.numberplatemaker.data.local.DatabaseBuilder.getRoomDatabase
import com.webscare.numberplatemaker.data.repository.PlateDataRepositoryImpl
import com.webscare.numberplatemaker.data.repository.PlateExportRepositoryImpl
import com.webscare.numberplatemaker.data.repository.PlateRepositoryImpl
import com.webscare.numberplatemaker.data.repository.SettingsRepositoryImpl
import com.webscare.numberplatemaker.domain.repo.PlateDataRepository
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.domain.repo.PlateRepository
import com.webscare.numberplatemaker.domain.repo.SettingsRepository
import com.webscare.numberplatemaker.util.createDataStore
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<DataStore<Preferences>> { createDataStore() }
    single { getRoomDatabase(getDatabaseBuilder()) }
    single { get<AppDatabase>().plateDao() }

    singleOf(::PlateRepositoryImpl) bind PlateRepository::class
    singleOf(::PlateExportRepositoryImpl ) bind PlateExportRepository::class
    singleOf(::PlateDataRepositoryImpl) bind PlateDataRepository::class
    singleOf(::SettingsRepositoryImpl) bind SettingsRepository::class
}
