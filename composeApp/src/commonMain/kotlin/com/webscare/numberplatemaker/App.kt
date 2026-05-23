package com.webscare.numberplatemaker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webscare.numberplatemaker.ui.MainAppScreen
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.theme.NumberPlateMakerTheme
import org.koin.compose.koinInject

@Composable
fun App(viewModel: PlateViewModel) {
    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    NumberPlateMakerTheme(darkTheme = settingsState.isDarkMode) {
        MainAppScreen()
    }
}