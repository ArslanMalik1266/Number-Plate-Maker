package com.webscare.numberplatemaker.domain.models

import org.jetbrains.compose.resources.DrawableResource


data class PlateUiState(
    val currentStep: PlateStep = PlateStep.VehicleSelection,
    val selectedVehicle: VehicleType? = null,
    val selectedProvince: Province? = null,
    val registrationNumber: String = "",
    val finalPlate: PlateModel? = null,
)

data class PlateModel(
    val vehicleType: VehicleType,
    val province: Province,
    val side: PlateSide,
    val registrationNumber: String,
    val formatHint: String,
    val config: PlateConfig
)

data class PlateConfig(
    val bgColor: Long,
    val textColor: Long,
    val stripColor: Long,
    val borderColor: Long = 0xFF1A1A1A,
    val borderWidth: Float = 2f,
    val stripOrientation: StripOrientation,
    val stripSizeFraction: Float = 0.20f,
    val stripContent: StripContent = StripContent(),
    val dimensions: PlateDimensions,
)

data class StripContent(
    // Logo
    val logoRes: DrawableResource? = null,
    val logoAlignment: LogoAlignment = LogoAlignment.NONE,
    val logoSizeFraction: Float = 0.75f,

    // Province full text (e.g. "PUNJAB", "SINDH")
    val provinceText: String? = null,
    val provinceTextAlignment: TextAlignment = TextAlignment.NONE,
    val provinceTextSizeFraction: Float = 0.20f,
    val provinceTextColor: Long = 0xFF000000,

    // Province short code (e.g. "PB", "SND") — kuch plates pe hota hai
    val provinceCodeText: String? = null,
    val provinceCodeAlignment: TextAlignment = TextAlignment.NONE,
    val provinceCodeSizeFraction: Float = 0.15f,
    val provinceCodeColor: Long = 0xFF000000,

    // City text (e.g. "LHR", "KHI")
    val cityText: String? = null,
    val cityTextAlignment: TextAlignment = TextAlignment.NONE,
    val cityTextSizeFraction: Float = 0.15f,
    val cityTextColor: Long = 0xFF000000,

    // Strip background drawable (Sindh special case)
    val stripBgDrawable: DrawableResource? = null,
    val stripBgAlignment: StripBackgroundAlignment = StripBackgroundAlignment.NONE,

)

data class PlateDimensions(
    val width: Float,
    val height: Float
)

enum class PlateSide {
    FRONT,
    BACK
}

enum class Province {
    PUNJAB,
    SINDH,
    KPK,
    BALOCHISTAN,
    ISLAMABAD,
    AJK,
    GB
}

enum class VehicleType {
    MOTORBIKE,
    PRIVATE_CAR,
    HEAVY_TRANSPORT,
    RICKSHAW,
    DIPLOMATIC,
    GOVERNMENT,
    COMMERCIAL,
    ELECTRIC_VEHICLE
}

enum class StripOrientation {
    HORIZONTAL_TOP,
    VERTICAL_LEFT,
    NONE
}

enum class LogoAlignment {
    STRIP_TOP_CENTRE,
    TOP_LEFT,
    CENTRE,
    CENTRE_LEFT,
    NONE

}

enum class TextAlignment {
    CENTRE_TOP,
    BOTTOM_LEFT,
    CENTRE_LEFT,
    BOTTOM_CENTRE,
    NONE
}
enum class StripBackgroundAlignment {
    NONE,
    TOP_LEFT,
    TOP_HORIZONTAL

}



sealed class PlateStep {
    data object VehicleSelection : PlateStep()
    data object ProvinceSelection : PlateStep()
    data object InputNumber : PlateStep()
    data object Preview : PlateStep()
}

