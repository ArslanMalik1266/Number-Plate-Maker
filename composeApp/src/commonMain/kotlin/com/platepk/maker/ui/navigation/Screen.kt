    package com.platepk.maker.ui.navigation

    sealed class Screen(val route: String) {
        object Home : Screen("home")
        object History : Screen("history")
        object VehicleType : Screen("vehicle_type")
        object ProvinceSelection : Screen("province_selection")
        object Registration : Screen("registration")
        object Preview : Screen("preview")

        object Details : Screen("details_screen/{plateId}") {
            fun createRoute(plateId: String) = "details_screen/$plateId"
        }
        object Settings : Screen("settings")
        object PlateType : Screen("plate_type/{plateId}") {
            fun createRoute(plateId: String?) = "plate_type/${plateId ?: "null"}"
        }
        object AddressDetail : Screen("address_detail")
        object OrderSummary : Screen("order_summary")
    }

