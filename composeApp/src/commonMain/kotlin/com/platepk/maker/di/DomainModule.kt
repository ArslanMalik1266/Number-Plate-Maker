package com.platepk.maker.di

import com.platepk.maker.domain.usecases.DeleteAllPlatesUseCase
import com.platepk.maker.domain.usecases.DeletePlateUseCase
import com.platepk.maker.domain.usecases.EnforcePlateInputUseCase
import com.platepk.maker.domain.usecases.ExportPlateUseCase
import com.platepk.maker.domain.usecases.GetOrderDataUseCase
import com.platepk.maker.domain.usecases.GetPlateConfigUseCase
import com.platepk.maker.domain.usecases.GetPlateInputConfigUseCase
import com.platepk.maker.domain.usecases.GetPlatesUseCase
import com.platepk.maker.domain.usecases.GetThemeSettingsUseCase
import com.platepk.maker.domain.usecases.SavePlateUseCase
import com.platepk.maker.domain.usecases.SubmitOrderUseCase
import com.platepk.maker.domain.usecases.ToggleThemeUseCase
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
    factory { GetOrderDataUseCase(get()) }
    factory { SubmitOrderUseCase(get()) }

}