package com.platepk.maker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.splash.CustomSplashScreen
import com.platepk.maker.ui.theme.NumberPlateMakerTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val viewModel: PlateViewModel by inject()
        setContent {
            val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
            val settingsState by viewModel.settingsState.collectAsStateWithLifecycle()

            if (isLoading) {
                CustomSplashScreen()
            } else {
                NumberPlateMakerTheme(darkTheme = settingsState.isDarkMode) {
                    App(viewModel = viewModel)
                }
            }
        }
    }
}