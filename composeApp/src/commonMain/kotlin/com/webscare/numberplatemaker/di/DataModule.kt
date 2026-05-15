package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.data.repository.PlateExportRepositoryImpl
import com.webscare.numberplatemaker.data.repository.PlateRepositoryImpl
import com.webscare.numberplatemaker.domain.repo.PlateExportRepository
import com.webscare.numberplatemaker.domain.repo.PlateRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::PlateRepositoryImpl) bind PlateRepository::class
    singleOf(::PlateExportRepositoryImpl ) bind PlateExportRepository::class
}