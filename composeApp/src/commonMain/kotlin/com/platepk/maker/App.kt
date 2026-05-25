package com.platepk.maker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.platepk.maker.ui.MainAppScreen
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.theme.NumberPlateMakerTheme

@Composable
fun App(viewModel: PlateViewModel) {
    val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

    NumberPlateMakerTheme(darkTheme = settingsState.isDarkMode) {
        MainAppScreen()
    }
}