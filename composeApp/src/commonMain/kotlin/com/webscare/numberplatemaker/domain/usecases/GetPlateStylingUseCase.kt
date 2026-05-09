package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.repo.PlateRepository

class GetPlateStylingUseCase(private val repository: PlateRepository) {

    data class PlateStyle(
        val backgroundColor: Long,
        val textColor: Long,
    )

    operator fun invoke(vehicleType: VehicleType, province: Province): PlateStyle {
        val colors = repository.getPlateColors(vehicleType, province)

        return PlateStyle(
            backgroundColor = colors.first,
            textColor = colors.second,
        )
    }
}