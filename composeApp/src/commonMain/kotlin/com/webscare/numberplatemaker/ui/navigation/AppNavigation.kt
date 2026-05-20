package com.webscare.numberplatemaker.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.editor.PreviewScreen
import com.webscare.numberplatemaker.ui.editor.ProvinceSelectionScreen
import com.webscare.numberplatemaker.ui.editor.RegistrationScreen
import com.webscare.numberplatemaker.ui.editor.VehicleTypeScreen
import com.webscare.numberplatemaker.ui.history.HistoryScreen
import com.webscare.numberplatemaker.ui.home.HomeScreen

fun NavGraphBuilder.appNavigation(
    navController: NavController,
    viewModel: PlateViewModel
) {
    composable(Screen.Home.route) {
        HomeScreen(
            onNavigateToSettings = { },
            onViewAllRecentClick = {
                navController.navigate(Screen.History.route)
            },
            onGeneratePlateClick = {
                navController.navigate(Screen.VehicleType.route)
            },
            onPlateItemClick = { },
        )
    }
    composable(Screen.History.route) {
        HistoryScreen(
            onBackClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            onPlateItemClick = { },
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
            }
        )
    }
    composable(Screen.ProvinceSelection.route) {
        ProvinceSelectionScreen(
            onBackClick = { navController.popBackStack() },
            onProvinceSelected = { province ->
                viewModel.onProvinceSelected(province)
                navController.navigate(Screen.Registration.route)
            }
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
            onBackClick = { navController.popBackStack() },
            onDownloadClick = {
//                navController.navigate(Screen.Download.route)
            }
        )
    }

}