package com.webscare.numberplatemaker.data.repository

import androidx.compose.ui.text.font.FontFamily
import com.webscare.numberplatemaker.data.repository.helpers.DimensionProvider
import com.webscare.numberplatemaker.data.repository.helpers.StyleResolver
import com.webscare.numberplatemaker.domain.models.PlateConfig
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.TextAlignment
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.repo.PlateRepository

class PlateRepositoryImpl : PlateRepository {

    override fun getPlateConfig(
        vehicleType: VehicleType,
        province: Province,
        registrationNumber: String,
        side: PlateSide,
        fontFamily: FontFamily
    ): PlateConfig {

        // 1. Dimensions uthao
        val dims = DimensionProvider.resolve(vehicleType, side, province)

        // 2. Style uthao
        val style = StyleResolver.resolve(province, vehicleType, side)

        // 3. Complete PlateConfig return karo
        return PlateConfig(
            registrationFont = fontFamily,
            registrationNumber = registrationNumber,
            provinceLogo = "",
            vehicleType = vehicleType,
            side = side,
            provinceName = style.provinceName,
            provinceCode = style.provinceCode,
            cityName = style.cityName,
            Strip = style.hasStrip,
            bgColor = style.bgColor,
            textColor = style.textColor,
            stripColor = style.stripColor,
            borderColor = 0xFF000000,
            borderWidth = 4f,
            stripOrientation = style.stripOrientation,
            stripSizeFraction = 0.2f,
            dimensions = dims,
            logoAlignment = style.logoAlignment,
            ProvinceTextAlignment = TextAlignment.TOP_CENTRE,
            CityTextAlignment = TextAlignment.BOTTOM_CENTRE,
            provinceCodeAlignment = TextAlignment.NONE,
            RegistrationTextAlignment = TextAlignment.CENTRE,

        )
    }
}