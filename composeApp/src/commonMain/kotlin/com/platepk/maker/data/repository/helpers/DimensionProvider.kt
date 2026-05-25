package com.platepk.maker.data.repository.helpers

import com.platepk.maker.domain.models.PlateDimensions
import com.platepk.maker.domain.models.PlateSide
import com.platepk.maker.domain.models.Province
import com.platepk.maker.domain.models.VehicleType

object DimensionProvider {

    fun resolve(vehicleType: VehicleType, side: PlateSide, province: Province): PlateDimensions {
        return when (vehicleType) {
            // --- CARS & HEAVY VEHICLES ---
            VehicleType.PRIVATE_CAR,
            VehicleType.ELECTRIC_CAR,
            VehicleType.GOVERNMENT,
            VehicleType.COMMERCIAL,
            VehicleType.HEAVY_TRANSPORT,
            VehicleType.DIPLOMATIC -> {
                when (province) {
                    Province.ISLAMABAD -> PlateDimensions(310f, 150f)
                    else -> PlateDimensions(325f, 152f) // Punjab, Sindh, KPK, etc.
                }
            }

            // --- MOTORBIKES ---
            VehicleType.MOTORBIKE,
            VehicleType.ELECTRIC_BIKE -> {
                // Bikes ki front/back dimensions almost saare provinces mein same hain
                if (side == PlateSide.FRONT) {
                    PlateDimensions(202f, 65f)
                } else {
                    PlateDimensions(152f, 130f)
                }
            }

            // --- RICKSHAWS ---
            VehicleType.RICKSHAW -> {
                when (province) {
                    Province.PUNJAB -> PlateDimensions(202f, 152f)
                    else -> PlateDimensions(220f, 150f)
                }
            }

            // --- FALLBACK ---
            else -> PlateDimensions(325f, 152f)
        }
    }
}