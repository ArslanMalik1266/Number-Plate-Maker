package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.domain.usecases.EnforcePlateInputUseCase
import com.webscare.numberplatemaker.domain.usecases.ExportPlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateInputConfigUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetPlateConfigUseCase)
    factoryOf(::ExportPlateUseCase)
    factoryOf(::EnforcePlateInputUseCase)
    factoryOf(::GetPlateInputConfigUseCase)

}