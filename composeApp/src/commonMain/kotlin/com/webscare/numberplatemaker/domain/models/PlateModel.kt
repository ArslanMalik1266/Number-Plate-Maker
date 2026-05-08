package com.webscare.numberplatemaker.domain.models


data class PlateUiState(
    val currentStep: PlateStep = PlateStep.VehicleSelection,
    val selectedVehicle: VehicleType? = null,
    val selectedProvince: Province? = null,
    val registrationNumber: String = "",
    val finalPlate: PlateModel? = null,
)

data class PlateModel(
    // 1. Primary Identity
    val vehicleType: VehicleType,
    val province: Province,
    val side: PlateSide,
    val registrationNumber: String,

    // 2. Physical Layout (Data Layer se dynamic ayengi)
    val dimensions: PlateDimensions,

    // 3. Ultra-Level Styling Control
    val bgColor: Long,          // E.g., 0xFFFFFFFF
    val textColor: Long,        // E.g., 0xFF000000
    val borderColor: Long,      // E.g., 0xFF1A1A1A (Professional contrast)
    val borderWidth: Float,     // Control thickness of the plate edge

    // 4. Content Formatting
    val formatHint: String,     // User guide: "ABC-1234"
    val bottomLabel: String?    // E.g., "PUNJAB" or "ICT"
)

sealed class PlateStep {
    data object VehicleSelection : PlateStep()
    data object ProvinceSelection : PlateStep()
    data object InputNumber : PlateStep()
    data object Preview : PlateStep()
}

enum class VehicleType {
    MOTORBIKE,
    COMMERCIAL_VEHICLE,
    PRIVATE_CAR,
    HEAVY_TRANSPORT,
    RICKSHAW,
    DIPLOMATIC,
    GOVERNMENT,
    COMMERCIAL,
    ELECTRIC_VEHICLE
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
enum class PlateSide { FRONT, BACK }

data class PlateDimensions(val width: Float, val height: Float)

