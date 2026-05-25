package com.platepk.maker.domain.repo

import androidx.compose.ui.text.font.FontFamily
import com.platepk.maker.domain.models.PlateConfig
import com.platepk.maker.domain.models.PlateSide
import com.platepk.maker.domain.models.Province
import com.platepk.maker.domain.models.VehicleType

interface PlateRepository {

    fun getPlateConfig(
        vehicleType: VehicleType,
        province: Province,
        registrationNumber: String,
        side: PlateSide,
        fontFamily: FontFamily
    ): PlateConfig
}