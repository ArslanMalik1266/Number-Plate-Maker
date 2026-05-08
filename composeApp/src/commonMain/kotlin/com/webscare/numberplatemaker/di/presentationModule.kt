package com.webscare.numberplatemaker.di

import com.webscare.numberplatemaker.ui.PlateViewModel
import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf


val presentationModule = module {
    viewModelOf(::PlateViewModel)
}