package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.domain.usecases.DeleteAllPlatesUseCase
import com.webscare.numberplatemaker.domain.usecases.DeletePlateUseCase
import com.webscare.numberplatemaker.domain.usecases.EnforcePlateInputUseCase
import com.webscare.numberplatemaker.domain.usecases.ExportPlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateInputConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlatesUseCase
import com.webscare.numberplatemaker.domain.usecases.GetThemeSettingsUseCase
import com.webscare.numberplatemaker.domain.usecases.SavePlateUseCase
import com.webscare.numberplatemaker.domain.usecases.ToggleThemeUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetPlateConfigUseCase)
    factoryOf(::ExportPlateUseCase)
    factoryOf(::EnforcePlateInputUseCase)
    factoryOf(::GetPlateInputConfigUseCase)

    factory { GetPlatesUseCase(get()) }
    factory { SavePlateUseCase(get()) }
    factory { DeletePlateUseCase(get()) }
    factory { DeleteAllPlatesUseCase(get()) }
    factory { ToggleThemeUseCase(get()) }
    factory { GetThemeSettingsUseCase(get()) }

}