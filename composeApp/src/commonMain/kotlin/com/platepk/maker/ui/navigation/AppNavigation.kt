package com.platepk.maker.ui.navigation

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.editor.PreviewScreen
import com.platepk.maker.ui.editor.ProvinceSelectionScreen
import com.platepk.maker.ui.editor.RegistrationScreen
import com.platepk.maker.ui.editor.VehicleTypeScreen
import com.platepk.maker.ui.history.HistoryScreen
import com.platepk.maker.ui.home.HomeScreen
import com.platepk.maker.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.appNavigation(
    navController: NavController,
    viewModel: PlateViewModel
) {
    composable(Screen.Home.route) {
        val scope = rememberCoroutineScope()
        HomeScreen(
            onNavigateToSettings = {
                navController.navigate(Screen.Settings.route)
            },
            onViewAllRecentClick = {
                navController.navigate(Screen.History.route)
            },
            onGeneratePlateClick = {
                scope.launch(kotlinx.coroutines.Dispatchers.Main) {
                    kotlinx.coroutines.delay(50)
                    navController.navigate(Screen.VehicleType.route)
                }
            },
            onPlateItemClick = { plate ->
                scope.launch {
                    kotlinx.coroutines.delay(50)
                    navController.navigate(Screen.Details.createRoute(plate.id))
                }
            },
            viewModel = viewModel
        )
    }
    composable(Screen.History.route) {
        val scope = rememberCoroutineScope()

        HistoryScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onPlateItemClick = { plate ->
                scope.launch {
                    kotlinx.coroutines.delay(50)
                    navController.navigate(Screen.Details.createRoute(plate.id))
                }
            },
            viewModel = viewModel
        )
    }
    composable(Screen.VehicleType.route) {
        val scope = rememberCoroutineScope()
        VehicleTypeScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onVehicleTypeSelected = { vehicleType ->
                scope.launch {
                    kotlinx.coroutines.delay(50)
                    viewModel.clearRegistrationFields()
                    viewModel.onVehicleSelected(vehicleType)
                    navController.navigate(Screen.ProvinceSelection.route)
                }
            },
            viewModel = viewModel


        )
    }
    composable(Screen.ProvinceSelection.route) {
        val scope = rememberCoroutineScope()
        ProvinceSelectionScreen(
            onBackClick = { navController.popBackStack() },
            onProvinceSelected = { province ->
                scope.launch {
                    kotlinx.coroutines.delay(50)
                    viewModel.onProvinceSelected(province)
                    navController.navigate(Screen.Registration.route)
                }
            },
            viewModel = viewModel
        )
    }
    composable(Screen.Registration.route) {
        val scope = rememberCoroutineScope()

        RegistrationScreen(
            viewModel = viewModel,
            onBackClick = { navController.popBackStack() },
            onGenerateClick = { plateNumber ->
                scope.launch {
                    kotlinx.coroutines.delay(50)
                    viewModel.generatePlatePreview(plateNumber) {
                        navController.navigate(Screen.Preview.route)
                    }
                }
            },
            registrationNumber = viewModel.uiState.value.registrationNumber,
            onNumberChange = { number ->
                viewModel.onRegistrationNumberChanged(number)
            }
        )
    }
    composable(Screen.Preview.route) {
        PreviewScreen(
            viewModel = viewModel,
            isFromRegistration = true,
            onBackClick = {
                viewModel.navigateBack()
                navController.popBackStack()
            },
            navigateToHome = { navController.navigate(Screen.Home.route) }

        )
    }
    composable(
        route = Screen.Details.route,
        arguments = listOf(navArgument("plateId") { type = NavType.StringType })
    ) { backStackEntry ->
        val id = backStackEntry.arguments?.getString("plateId")
        val plateData = viewModel.uiState.value.savedPlates.find { it.id == id }

        println("DEBUG_DETAIL: plateId = $id")
        println("DEBUG_DETAIL: plateData = $plateData")
        println("DEBUG_DETAIL: frontImageRes = ${plateData?.plateImageRes}")
        println("DEBUG_DETAIL: backImageRes = ${plateData?.plateImageBackRes}")
        PreviewScreen(
            viewModel = viewModel,
            plateId = id,
            isFromRegistration = false,
            onBackClick = { navController.popBackStack() },
            navigateToHome = { navController.navigate(Screen.Home.route) }
        )
    }
    composable(Screen.Settings.route) {
        SettingsScreen(onBackClick = { navController.popBackStack() }, viewModel = viewModel)
    }


}