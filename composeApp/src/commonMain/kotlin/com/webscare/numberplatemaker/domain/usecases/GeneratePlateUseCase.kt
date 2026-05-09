package com.webscare.numberplatemaker.domain.usecases

import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.repo.PlateRepository

class GeneratePlateUseCase(private val repository: PlateRepository) {

    operator fun invoke(
        vehicleType: VehicleType,
        province: Province,
        side: PlateSide,
        registrationNumber: String
    ): PlateModel {

        // 1. Fetch physical dimensions based on vehicle and side
        val dimensions = repository.getDimensions(vehicleType, side, province)

        // 2. Get Styling (Colors) from Repository logic
        val (bgColor, textColor) = repository.getPlateColors(vehicleType, province)
        val orientation = repository.getStripOrientation(vehicleType)


        // 4. Map everything to our Ultra-Senior PlateModel
        return PlateModel(
            vehicleType = vehicleType,
            province = province,
            side = side,
            registrationNumber = registrationNumber.uppercase().trim(),
            dimensions = dimensions,
            bgColor = bgColor,
            stripOrientation = orientation,
            textColor = textColor,
            borderColor = 0xFF1A1A1A, 
            borderWidth = 2.0f,
            formatHint = getFormatHint(province, vehicleType),
        )
    }



    private fun getFormatHint(province: Province, vehicle: VehicleType): String {
        // In real app, this should come from Repo's Regex logic
        return when (province) {
            Province.ISLAMABAD -> "ABC-123"
            Province.PUNJAB -> "LEA-24-1234"
            else -> "ABC-1234"
        }
    }
}