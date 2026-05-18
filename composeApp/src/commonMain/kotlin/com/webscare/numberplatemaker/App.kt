package com.webscare.numberplatemaker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.webscare.numberplatemaker.ui.PlateViewModel
import org.jetbrains.compose.resources.painterResource

import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.viewmodel.koinViewModel

import androidx.compose.runtime.Composable
import com.webscare.numberplatemaker.di.appModules
import com.webscare.numberplatemaker.ui.home.HomeScreen
import com.webscare.numberplatemaker.ui.onboarding.OnboardingScreen
import org.koin.compose.KoinContext
import org.koin.compose.KoinApplication

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModules)
    }) {
        KoinContext {
            HomeScreen( onNavigateToSettings = {}, onGeneratePlateClick = {})
        }
    }
}