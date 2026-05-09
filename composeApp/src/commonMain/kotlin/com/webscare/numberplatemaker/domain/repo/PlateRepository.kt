package com.webscare.numberplatemaker.domain.repo

import com.webscare.numberplatemaker.domain.models.PlateDimensions
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.VehicleType

interface PlateRepository {
    fun getDimensions(vehicleType: VehicleType, side: PlateSide, province: Province): PlateDimensions

    fun getPlateColors(vehicleType: VehicleType, province: Province): Pair<Long, Long>

    fun getStripOrientation(vehicleType: VehicleType): StripOrientation
}