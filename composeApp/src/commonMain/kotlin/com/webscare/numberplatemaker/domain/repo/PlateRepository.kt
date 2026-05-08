package com.webscare.numberplatemaker.domain.repo

import com.webscare.numberplatemaker.domain.models.PlateDimensions
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType

interface PlateRepository {
    fun getDimensions(vehicleType: VehicleType, side: PlateSide): PlateDimensions

    fun getPlateColors(vehicleType: VehicleType, province: Province): Pair<Long, Long>

    fun getLogoPath(province: Province): String?
}