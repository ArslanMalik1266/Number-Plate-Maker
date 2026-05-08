package com.webscare.numberplatemaker.data.repository

import com.webscare.numberplatemaker.domain.models.PlateDimensions
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.repo.PlateRepository

/**
 * Ultra-Senior Implementation: Centralized logic for all Pakistan vehicle categories.
 * Using Long hex directly for KMP compatibility.
 */
class PlateRepositoryImpl : PlateRepository {

    override fun getDimensions(vehicleType: VehicleType, side: PlateSide): PlateDimensions {
        return when (vehicleType) {
            VehicleType.MOTORBIKE -> {
                // Front is very slim, Back is more square for registration
                if (side == PlateSide.FRONT) PlateDimensions(250f, 40f)
                else PlateDimensions(180f, 160f)
            }

            VehicleType.PRIVATE_CAR, VehicleType.ELECTRIC_VEHICLE, VehicleType.GOVERNMENT -> {
                // Front standard long, Back can be slightly taller if needed
                if (side == PlateSide.FRONT) PlateDimensions(520f, 110f)
                else PlateDimensions(440f, 120f)
            }

            VehicleType.COMMERCIAL, VehicleType.HEAVY_TRANSPORT -> {
                // Larger and taller for visibility
                if (side == PlateSide.FRONT) PlateDimensions(520f, 120f)
                else PlateDimensions(440f, 150f)
            }

            VehicleType.RICKSHAW -> {
                // Rickshaws usually have same square-ish plates
                PlateDimensions(250f, 150f)
            }

            VehicleType.DIPLOMATIC -> {
                // Often slightly smaller/custom European style
                if (side == PlateSide.FRONT) PlateDimensions(400f, 100f)
                else PlateDimensions(340f, 140f)
            }
            VehicleType.COMMERCIAL_VEHICLE, VehicleType.COMMERCIAL -> {
                // Commercial plates are usually taller for better visibility of "Load/Capacity" info
                if (side == PlateSide.FRONT) {
                    PlateDimensions(width = 520f, height = 120f)
                } else {
                    PlateDimensions(width = 440f, height = 160f) // Taller rear plate
                }
            }
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

    override fun getLogoPath(province: Province): String? {
        return when (province) {
            Province.PUNJAB -> "logos/punjab_logo.xml"
            Province.SINDH -> "logos/sindh_logo.xml"
            Province.KPK -> "logos/kpk_logo.xml"
            Province.BALOCHISTAN -> "logos/balochistan_logo.xml"
            Province.ISLAMABAD -> "logos/ict_logo.xml"
            Province.AJK -> "logos/ajk_logo.xml"
            Province.GB -> "logos/gb_logo.xml"
        }
    }
}