package com.platepk.maker.di

import com.platepk.maker.ui.PlateViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf


val presentationModule = module {
    viewModelOf(::PlateViewModel)
}