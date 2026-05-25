package com.platepk.maker.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.platepk.maker.ui.navigation.Screen
import com.platepk.maker.ui.navigation.appNavigation
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainAppScreen() {
    // 🚀 Controller initialization point
    val navController = rememberNavController()
    val viewModel: PlateViewModel = koinViewModel()
    Scaffold {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            appNavigation(
                navController = navController , viewModel = viewModel
            )
        }
    }
}