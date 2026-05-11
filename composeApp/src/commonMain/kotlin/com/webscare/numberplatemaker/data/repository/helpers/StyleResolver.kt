package com.webscare.numberplatemaker.data.repository.helpers

import com.webscare.numberplatemaker.domain.models.LogoAlignment
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.TextAlignment
import com.webscare.numberplatemaker.domain.models.VehicleType

object StyleResolver {

    fun resolve(
        province: Province,
        vehicle: VehicleType,
        side: PlateSide
    ): StyleConfig {
        return when (province) {
            Province.PUNJAB -> getPunjabStyle(vehicle, side)
            Province.KPK -> getKpkStyle(vehicle, side)
            Province.BALOCHISTAN -> getBalochistanStyle(vehicle, side)
            Province.SINDH -> getSindhStyle(vehicle, side)
            else -> getDefaultStyle()
        }
    }



    private fun getPunjabStyle(vehicle: VehicleType, side: PlateSide): StyleConfig {
        val (background, text) = when (vehicle) {
            VehicleType.COMMERCIAL -> 0xFFFFD700L to 0xFF1A1A1AL // Yellow background, Black text
            VehicleType.GOVERNMENT -> 0xFF004B23 to 0xFFFFFFFFL // Green background, White text
            VehicleType.DIPLOMATIC -> 0xFFE33528 to 0xFFFFFFFFL // Red background, White text
            VehicleType.RICKSHAW -> 0xFFFFD700L to 0xFF1A1A1AL
            VehicleType.ELECTRIC_BIKE, VehicleType.ELECTRIC_CAR -> 0xFF0A4624 to 0xFFFFFFFFL
            else -> 0xFFFFFFFFL to 0xFF1A1A1AL // Default White background, Black text
        }
        return when (vehicle) {
            VehicleType.MOTORBIKE,
            VehicleType.MOTORBIKE-> {
                if (side == PlateSide.FRONT) {
                    // Bike Front
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Punjab_logo,
                        provinceName = "PUNJAB",
                        provinceCode = "ET&NC",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.LEFT_CENTRE,
                        provinceCodeAlignment = TextAlignment.BOTTOM_LEFT
                    )
                } else {
                    // Bike Rear
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Punjab_logo,
                        provinceName = "PUNJAB",
                        provinceCode = "ET&NC",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.TOP_CENTRE,
                        provinceCodeAlignment = TextAlignment.TOP_CENTRE
                    )
                }
            }
            else -> {
                // Standard Punjab Car Style
                StyleConfig(
                    bgColor = background,
                    textColor = text,
                    stripColor = 0,
                    hasStrip = false,
                    stripOrientation = StripOrientation.NONE,
                    logoAlignment = LogoAlignment.Punjab_logo,
                    provinceName = "PUNJAB",
                    regAlignment = TextAlignment.BOTTOM_CENTRE,
                    provinceAlignment = TextAlignment.TOP_CENTRE,
                    provinceCode = "ET&NC",
                    provinceCodeAlignment = TextAlignment.TOP_RIGHT
                )
            }
        }
    }

    private fun getKpkStyle(vehicle: VehicleType, side: PlateSide): StyleConfig {
        val (background, text) = when (vehicle) {
            VehicleType.COMMERCIAL -> 0xFFFFD700L to 0xFF1A1A1AL // Yellow background, Black text
            VehicleType.GOVERNMENT -> 0xFF004B23 to 0xFFFFFFFFL // Green background, White text
            VehicleType.DIPLOMATIC -> 0xFFE33528 to 0xFFFFFFFFL // Red background, White text
            VehicleType.RICKSHAW -> 0xFFFFD700L to 0xFF1A1A1AL
            VehicleType.ELECTRIC_BIKE, VehicleType.ELECTRIC_CAR -> 0xFF0A4624 to 0xFFFFFFFFL
            else -> 0xFFFFFFFFL to 0xFF1A1A1AL // Default White background, Black text
        }
        return when (vehicle) {
            VehicleType.MOTORBIKE -> {
                if (side == PlateSide.FRONT) {
                    // Bike Front
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Kpk_logo,
                        provinceName = "KHYBER PAKHTUNKHWA",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.LEFT_CENTRE,
                        provinceCodeAlignment = TextAlignment.BOTTOM_LEFT
                    )
                } else {
                    // Bike Rear
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Kpk_logo,
                        provinceName = "KHYBER PAKHTUNKHWA",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.TOP_CENTRE,
                        provinceCodeAlignment = TextAlignment.TOP_CENTRE
                    )
                }
            }
            else -> {
                // Standard Punjab Car Style
                StyleConfig(
                    bgColor = background,
                    textColor = text,
                    stripColor = 0,
                    hasStrip = false,
                    stripOrientation = StripOrientation.NONE,
                    logoAlignment = LogoAlignment.Kpk_logo,
                    provinceName = "KHYBER PAKHTUNKHWA",
                    cityName = "PESHAWAR",
                    regAlignment = TextAlignment.BOTTOM_CENTRE,
                    provinceAlignment = TextAlignment.TOP_CENTRE,
                    provinceCodeAlignment = TextAlignment.TOP_RIGHT
                )
            }
        }
    }

    private fun getBalochistanStyle(vehicle: VehicleType, side: PlateSide): StyleConfig {
        val (background, text) = when (vehicle) {
            VehicleType.COMMERCIAL -> 0xFFFFD700L to 0xFF1A1A1AL // Yellow background, Black text
            VehicleType.GOVERNMENT -> 0xFF004B23 to 0xFFFFFFFFL // Green background, White text
            VehicleType.DIPLOMATIC -> 0xFFE33528 to 0xFFFFFFFFL // Red background, White text
            VehicleType.RICKSHAW -> 0xFFFFD700L to 0xFF1A1A1AL
            VehicleType.ELECTRIC_BIKE, VehicleType.ELECTRIC_CAR -> 0xFF0A4624 to 0xFFFFFFFFL
            else -> 0xFFFFFFFFL to 0xFF1A1A1AL // Default White background, Black text
        }
        return when (vehicle) {
            VehicleType.MOTORBIKE -> {
                if (side == PlateSide.FRONT) {
                    // Bike Front
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Kpk_logo,
                        provinceName = "BALOCHISTAN",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.LEFT_CENTRE,
                        provinceCodeAlignment = TextAlignment.BOTTOM_LEFT
                    )
                } else {
                    // Bike Rear
                    StyleConfig(
                        bgColor = background,
                        textColor = text,
                        stripColor = 0,
                        hasStrip = false,
                        stripOrientation = StripOrientation.NONE,
                        logoAlignment = LogoAlignment.Kpk_logo,
                        provinceName = "BALOCHISTAN",
                        regAlignment = TextAlignment.CENTRE,
                        provinceAlignment = TextAlignment.TOP_CENTRE,
                        provinceCodeAlignment = TextAlignment.TOP_CENTRE
                    )
                }
            }
            else -> {
                // Standard Punjab Car Style
                StyleConfig(
                    bgColor = background,
                    textColor = text,
                    stripColor = 0,
                    hasStrip = false,
                    stripOrientation = StripOrientation.NONE,
                    logoAlignment = LogoAlignment.Kpk_logo,
                    provinceName = "BALOCHISTAN",
                    cityName = "QUETTA",
                    regAlignment = TextAlignment.BOTTOM_CENTRE,
                    provinceAlignment = TextAlignment.TOP_CENTRE,
                    provinceCodeAlignment = TextAlignment.TOP_RIGHT
                )
            }
        }
    }

    private fun getSindhStyle(vehicle: VehicleType, side: PlateSide): StyleConfig {
        return if (vehicle == VehicleType.MOTORBIKE) {
            // Sindh Bike Style (Aksar yellow/white mix ya specific formats)
            StyleConfig(
                bgColor = 0xFFFFFFFF,
                textColor = 0xFF1A1A1A,
                hasStrip = true,
                stripColor = 0xFF8B0000,
                stripOrientation = StripOrientation.Horizontal_TOP, // Bike pe aksar upar strip hoti h
                logoAlignment = LogoAlignment.Sindh_logo,
                regAlignment = TextAlignment.CENTRE,
                provinceName = "SINDH",
                provinceAlignment = TextAlignment.TOP_CENTRE
            )
        } else {
            // Sindh Car Style
            StyleConfig(
                bgColor = 0xFFFFFFFF,
                textColor = 0xFF1A1A1A,
                hasStrip = true,
                stripColor = 0xFF8B0000,
                stripOrientation = StripOrientation.VERTICAL_LEFT,
                logoAlignment = LogoAlignment.Sindh_logo,
                regAlignment = TextAlignment.CENTRE,
                provinceName = "SINDH",
                provinceAlignment = TextAlignment.TOP_CENTRE
            )
        }
    }

    // Default Fallback
    private fun getDefaultStyle() = StyleConfig(
        bgColor = 0xFFFFFFFF,
        textColor = 0xFF000000,
        stripColor = 0,
        hasStrip = false,
        stripOrientation = StripOrientation.NONE,
        logoAlignment = LogoAlignment.NONE,
        provinceName = "PAKISTAN",
        regAlignment = TextAlignment.CENTRE,
        provinceAlignment = TextAlignment.TOP_CENTRE
    )
    data class StyleConfig(
        val bgColor: Long,
        val textColor: Long,
        val stripColor: Long,
        val hasStrip: Boolean,
        val cityName : String = "",
        val cityNamelignment: TextAlignment = TextAlignment.NONE,
        val stripOrientation: StripOrientation,
        val logoAlignment: LogoAlignment,
        val provinceName: String,
        val regAlignment: TextAlignment,
        val provinceCode: String = "",
        val provinceAlignment: TextAlignment,
        val provinceCodeAlignment: TextAlignment = TextAlignment.NONE
    )
}