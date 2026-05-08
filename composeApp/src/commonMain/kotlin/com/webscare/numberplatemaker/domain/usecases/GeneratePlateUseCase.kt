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
        val dimensions = repository.getDimensions(vehicleType, side)

        // 2. Get Styling (Colors) from Repository logic
        val (bgColor, textColor) = repository.getPlateColors(vehicleType, province)

        // 3. Determine if this specific province/vehicle needs the Modern Blue Strip
        // Senior Tip: Islamabad and New Punjab plates usually have this.
        val hasStrip = (province == Province.ISLAMABAD || province == Province.PUNJAB) &&
                (vehicleType != VehicleType.MOTORBIKE && vehicleType != VehicleType.MOTORBIKE)

        // 4. Map everything to our Ultra-Senior PlateModel
        return PlateModel(
            vehicleType = vehicleType,
            province = province,
            side = side,
            registrationNumber = registrationNumber.uppercase().trim(),
            dimensions = dimensions,
            bgColor = bgColor,
            textColor = textColor,
            borderColor = 0xFF1A1A1A, 
            borderWidth = 2.0f,
            bottomLabel = getBottomLabelText(province),
            formatHint = getFormatHint(province, vehicleType)
        )
    }

    // Helper functions inside UseCase to keep the mapping clean
    private fun getBottomLabelText(province: Province): String {
        return when (province) {
            Province.ISLAMABAD -> "ICT-ISLAMABAD"
            Province.PUNJAB -> "PUNJAB"
            Province.SINDH -> "SINDH"
            Province.KPK -> "KPK"
            Province.BALOCHISTAN -> "BALOCHISTAN"
            Province.AJK -> "AZAD KASHMIR"
            Province.GB -> "GILGIT BALTISTAN"
        }
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