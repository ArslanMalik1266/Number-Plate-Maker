    package com.webscare.numberplatemaker.domain.usecases

    import androidx.compose.ui.text.font.FontFamily
    import com.webscare.numberplatemaker.domain.models.PlateConfig
    import com.webscare.numberplatemaker.domain.models.PlateSide
    import com.webscare.numberplatemaker.domain.models.Province
    import com.webscare.numberplatemaker.domain.models.VehicleType
    import com.webscare.numberplatemaker.domain.repo.PlateRepository

    class GetPlateConfigUseCase(private val repository: PlateRepository) {

        operator fun invoke(
            vehicleType: VehicleType,
            province: Province,
            regNumber: String
        ): PlatePreviewData {

            val front = repository.getPlateConfig(vehicleType, province, regNumber, PlateSide.FRONT, FontFamily.Default)
            val back = repository.getPlateConfig(vehicleType, province, regNumber, PlateSide.BACK , FontFamily.Default)

            return PlatePreviewData(
                frontPlate = front,
                backPlate = back
            )
        }
    }

    data class PlatePreviewData(
        val frontPlate: PlateConfig,
        val backPlate: PlateConfig
    )