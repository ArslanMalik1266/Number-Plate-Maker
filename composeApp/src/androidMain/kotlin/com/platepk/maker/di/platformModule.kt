package com.platepk.maker.di

import com.platepk.maker.data.repository.PlateExportRepositoryImpl
import com.platepk.maker.domain.repo.PlateExportRepository
import com.platepk.maker.util.PlatformClock
import com.platepk.maker.util.PlatformExportHelper
import org.koin.dsl.module

actual val platformModule = module {
    factory { PlatformClock() }
    single { PlatformExportHelper(get()) }
    single<PlateExportRepository> { PlateExportRepositoryImpl(get()) }
}