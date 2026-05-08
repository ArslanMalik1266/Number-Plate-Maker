package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.domain.usecases.GeneratePlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateStylingUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GeneratePlateUseCase)
    factoryOf(::GetPlateStylingUseCase)
}