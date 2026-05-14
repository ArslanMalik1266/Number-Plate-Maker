package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.data.repository.PlateExportRepositoryImpl
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.util.PlatformExportHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformModule = module {
    single { PlatformExportHelper(androidContext()) }
    single<PlateExportRepository> { PlateExportRepositoryImpl(get()) }
}