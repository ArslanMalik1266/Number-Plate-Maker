package com.platepk.maker.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.platepk.maker.data.local.AppDatabase.AppDatabase
import com.platepk.maker.data.local.DatabaseBuilder.getDatabaseBuilder
import com.platepk.maker.data.local.DatabaseBuilder.getRoomDatabase
import com.platepk.maker.data.repository.OrderRepositoryImpl
import com.platepk.maker.data.repository.PlateDataRepositoryImpl
import com.platepk.maker.data.repository.PlateExportRepositoryImpl
import com.platepk.maker.data.repository.PlateRepositoryImpl
import com.platepk.maker.data.repository.SettingsRepositoryImpl
import com.platepk.maker.domain.repo.OrderRepository
import com.platepk.maker.domain.repo.PlateDataRepository
import com.platepk.maker.domain.repo.PlateExportRepository
import com.platepk.maker.domain.repo.PlateRepository
import com.platepk.maker.domain.repo.SettingsRepository
import com.platepk.maker.util.createDataStore
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
    singleOf(::OrderRepositoryImpl) bind OrderRepository::class
}
