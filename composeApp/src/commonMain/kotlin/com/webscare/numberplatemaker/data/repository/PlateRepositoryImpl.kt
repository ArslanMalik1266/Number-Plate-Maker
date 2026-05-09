package com.webscare.numberplatemaker.data.repository

import com.webscare.numberplatemaker.domain.models.PlateDimensions
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.repo.PlateRepository


class PlateRepositoryImpl : PlateRepository {

    override fun getDimensions(vehicleType: VehicleType, side: PlateSide, province: Province): PlateDimensions {
        return when (vehicleType) {
            VehicleType.PRIVATE_CAR,
            VehicleType.ELECTRIC_VEHICLE,
            VehicleType.GOVERNMENT,
            VehicleType.COMMERCIAL,
            VehicleType.HEAVY_TRANSPORT,
            VehicleType.DIPLOMATIC-> {
                when (province) {
                    Province.ISLAMABAD -> {
                        // ICT Islamabad (2016-present): 310mm x 150mm
                        PlateDimensions(310f, 150f)
                    }
                    Province.PUNJAB -> {
                        // Punjab Universal/ANPR: 325mm x 152mm
                        PlateDimensions(325f, 152f)
                    }
                    Province.SINDH, Province.KPK, Province.BALOCHISTAN -> {
                        // Sindh/KPK/Balochistan standard: 325mm x 152mm
                        PlateDimensions(325f, 152f)
                    }
                    else -> {
                        PlateDimensions(325f, 152f)
                    }
                }
            }

            VehicleType.MOTORBIKE -> {
                when (province) {
                    Province.PUNJAB -> {
                        if (side == PlateSide.FRONT) {
                            // Motorcycle front plate — Punjab: 202mm x 65mm
                            PlateDimensions(202f, 65f)
                        } else {
                            // Motorcycle rear plate — Punjab: 152mm x 130mm
                            PlateDimensions(152f, 130f)
                        }
                    }
                    else -> {
                        if (side == PlateSide.FRONT) {
                            // Motorcycle front plate — Punjab: 202mm x 65mm
                            PlateDimensions(202f, 65f)
                        } else {
                            // Motorcycle rear plate — Punjab: 152mm x 130mm
                            PlateDimensions(152f, 130f)
                        }
                    }
                }
            }
            VehicleType.RICKSHAW -> {
                when (province) {
                    Province.PUNJAB -> {
                        // Rickshaws — Punjab specific: 202mm x 152mm
                        PlateDimensions(202f, 152f)
                    }
                    else -> {
                        // Rickshaws / 3-wheelers (standard): 220mm x 150mm
                        PlateDimensions(220f, 150f)
                    }
                }
            }

            else -> PlateDimensions(325f, 152f) // Safe fallback
        }
    }

    override fun getPlateColors(vehicleType: VehicleType, province: Province): Pair<Long, Long> {
        return when (vehicleType) {
            VehicleType.GOVERNMENT -> 0xFF01411C to 0xFFFFFFFF // Green to White

            VehicleType.COMMERCIAL, VehicleType.HEAVY_TRANSPORT ->
                0xFFFFD54F to 0xFF000000 // Yellow to Black

            VehicleType.DIPLOMATIC ->
                0xFFB71C1C to 0xFFFFFFFF // Red to White (Standard Diplomatic)

            VehicleType.ELECTRIC_VEHICLE ->
                0xFFFFFFFF to 0xFF003399 // White to Blue (Modern EV look)

            VehicleType.MOTORBIKE, VehicleType.PRIVATE_CAR, VehicleType.RICKSHAW -> {
                // Province specific logic if needed, else standard White/Black
                0xFFF5F5F5 to 0xFF1A1A1A
            }

            else -> {}
        } as Pair<Long, Long>
    }



    override fun getStripOrientation(vehicleType: VehicleType): StripOrientation {
        return when (vehicleType) {
            VehicleType.PRIVATE_CAR,
            VehicleType.ELECTRIC_VEHICLE,
            VehicleType.GOVERNMENT,
            VehicleType.COMMERCIAL,
            VehicleType.HEAVY_TRANSPORT,
            VehicleType.DIPLOMATIC -> StripOrientation.HORIZONTAL_TOP

            VehicleType.MOTORBIKE,
            VehicleType.RICKSHAW -> StripOrientation.VERTICAL_LEFT

            else -> StripOrientation.NONE
        }
    }
}