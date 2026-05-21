package com.webscare.numberplatemaker.domain.models

import androidx.compose.ui.text.font.FontFamily
import com.webscare.numberplatemaker.data.local.entity.PlateEntity
import com.webscare.numberplatemaker.util.ExportResult
import org.jetbrains.compose.resources.DrawableResource


data class PlateUiState(
    val currentStep: PlateStep = PlateStep.VehicleSelection,
    val savedPlates: List<RecentPlateItem> = emptyList(),
    val exportStatus: ExportResult? = null,
    val selectedVehicle: VehicleType? = null,
    val selectedProvince: Province? = null,
    val registrationNumber: String = "",
    val finalPlate: PlateModel? = null,
    val letterInput: String = "",
    val numberInput: String = "",
    val formatHint: String = "",
    val frontPlate: PlateModel? = null,
    val backPlate: PlateModel? = null,
    val plateInputConfig: PlateInputConfig? = null,
    val exporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val exportError: String? = null,
    val loading : Boolean = true
)

data class PlateModel(
    val side: PlateSide,
    val config: PlateConfig
)

data class PlateConfig(
    val registrationFont: FontFamily = FontFamily.Default,
    val registrationNumber: String,
    val provinceLogo: String,
    val vehicleType: VehicleType,
    val side: PlateSide,
    val provinceName: String,
    val provinceCode : String,
    val logoColor : Long,
    val cityName : String,
    val Strip : Boolean,
    val bgColor: Long,
    val textColor: Long,
    val stripColor: Long,
    val borderColor: Long,
    val borderWidth: Float = 10f,
    val stripOrientation: StripOrientation = StripOrientation.NONE,
    val stripSizeFraction: Float,
    val stripBgDrawable: DrawableResource? = null,
    val dimensions: PlateDimensions,
    val logoAlignment : LogoAlignment = LogoAlignment.NONE,
    val ProvinceTextAlignment : TextAlignment = TextAlignment.NONE,
    val CityTextAlignment : TextAlignment = TextAlignment.NONE,
    val provinceCodeAlignment : TextAlignment = TextAlignment.NONE,
    val RegistrationTextAlignment : TextAlignment = TextAlignment.NONE,

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
    ELECTRIC_CAR,
    ELECTRIC_BIKE
}

enum class StripOrientation {
    Horizontal_TOP,
    VERTICAL_LEFT,
    NONE
}

enum class LogoAlignment {
    Sindh_logo,
    Punjab_logo,
    Balochistan_logo,
    Kpk_logo,
    GB_logo,
    AJK_logo,
    ICT_logo,
    NONE

}

enum class TextAlignment {
    TOP_CENTRE,
    BOTTOM_LEFT,
    LEFT_CENTRE,
    BOTTOM_CENTRE,
    TOP_RIGHT,
    CENTRE,
    NONE
}

sealed class PlateStep {
    data object VehicleSelection : PlateStep()
    data object ProvinceSelection : PlateStep()
    data object InputNumber : PlateStep()
    data object Preview : PlateStep()
}

data class PlateInputConfig(
    val minLetterCount: Int,
    val maxLetterCount: Int,
    val minNumberCount: Int,
    val maxNumberCount: Int,
    val formatHint: String
)