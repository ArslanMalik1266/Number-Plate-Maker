package com.webscare.numberplatemaker.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.editor.PreviewScreen
import com.webscare.numberplatemaker.ui.editor.ProvinceSelectionScreen
import com.webscare.numberplatemaker.ui.editor.RegistrationScreen
import com.webscare.numberplatemaker.ui.editor.VehicleTypeScreen
import com.webscare.numberplatemaker.ui.history.HistoryScreen
import com.webscare.numberplatemaker.ui.home.HomeScreen
import com.webscare.numberplatemaker.ui.settings.SettingsScreen

fun NavGraphBuilder.appNavigation(
    navController: NavController,
    viewModel: PlateViewModel
) {
    composable(Screen.Home.route) {
        HomeScreen(
            onNavigateToSettings = {
                navController.navigate(Screen.Settings.route)
            },
            onViewAllRecentClick = {
                navController.navigate(Screen.History.route)
            },
            onGeneratePlateClick = {
                navController.navigate(Screen.VehicleType.route)
            },
            onPlateItemClick = { plate ->
                navController.navigate(Screen.Details.createRoute(plate.id))
            },
            viewModel = viewModel
        )
    }
    composable(Screen.History.route) {
        HistoryScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onPlateItemClick = { plate ->
                navController.navigate(Screen.Details.createRoute(plate.id))
            },
            viewModel = viewModel
        )
    }
    composable(Screen.VehicleType.route) {
        VehicleTypeScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onVehicleTypeSelected = { vehicleType ->
                viewModel.onVehicleSelected(vehicleType)
                navController.navigate(Screen.ProvinceSelection.route)
            },
            viewModel = viewModel


        )
    }
    composable(Screen.ProvinceSelection.route) {
        ProvinceSelectionScreen(
            onBackClick = { navController.popBackStack() },
            onProvinceSelected = { province ->
                viewModel.onProvinceSelected(province)
                navController.navigate(Screen.Registration.route)
            },
            viewModel = viewModel
        )
    }
    composable(Screen.Registration.route) {
        RegistrationScreen(
            viewModel = viewModel,
            onBackClick = { navController.popBackStack() },
            onGenerateClick = { plateNumber ->
                viewModel.generatePlatePreview(plateNumber) {
                    navController.navigate(Screen.Preview.route)
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
            onBackClick = { navController.popBackStack() },
            navigateToHome = { navController.navigate(Screen.Home.route) }
        )
    }
    composable(Screen.Settings.route) {
        SettingsScreen(onBackClick = { navController.popBackStack() }, viewModel = viewModel)
    }


}