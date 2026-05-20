package com.webscare.numberplatemaker.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object VehicleType : Screen("vehicle_type")
    object ProvinceSelection : Screen("province_selection")
    object Registration : Screen("registration")
    object Preview : Screen("preview")
}

