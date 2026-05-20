package com.webscare.numberplatemaker


import androidx.compose.runtime.Composable
import com.webscare.numberplatemaker.di.appModules
import com.webscare.numberplatemaker.ui.MainAppScreen
import org.koin.compose.KoinContext
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules)
    }) {
        KoinContext {
            MainAppScreen()
        }
    }
}