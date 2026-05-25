    package com.platepk.maker.domain.usecases

    import androidx.compose.ui.text.font.FontFamily
    import com.platepk.maker.domain.models.PlateConfig
    import com.platepk.maker.domain.models.PlateSide
    import com.platepk.maker.domain.models.Province
    import com.platepk.maker.domain.models.VehicleType
    import com.platepk.maker.domain.repo.PlateRepository

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