package com.webscare.numberplatemaker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.splash.CustomSplashScreen
import com.webscare.numberplatemaker.ui.theme.NumberPlateMakerTheme
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

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