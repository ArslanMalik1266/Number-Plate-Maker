package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.data.repository.PlateExportRepositoryImpl
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.util.PlatformClock
import com.webscare.numberplatemaker.util.PlatformExportHelper
import org.koin.dsl.module

actual val platformModule = module {
    factory { PlatformClock() }
    single { PlatformExportHelper(get()) }
    single<PlateExportRepository> { PlateExportRepositoryImpl(get()) }
}