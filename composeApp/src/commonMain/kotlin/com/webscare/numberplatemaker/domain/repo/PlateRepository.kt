package com.webscare.numberplatemaker.domain.repo

import androidx.compose.ui.text.font.FontFamily
import com.webscare.numberplatemaker.domain.models.PlateConfig
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType

interface PlateRepository {

    fun getPlateConfig(
        vehicleType: VehicleType,
        province: Province,
        registrationNumber: String,
        side: PlateSide,
        fontFamily: FontFamily
    ): PlateConfig
}