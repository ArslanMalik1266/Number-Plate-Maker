package com.platepk.maker.data.repository

import androidx.compose.ui.text.font.FontFamily
import com.platepk.maker.data.repository.helpers.DimensionProvider
import com.platepk.maker.data.repository.helpers.StyleResolver
import com.platepk.maker.domain.models.PlateConfig
import com.platepk.maker.domain.models.PlateSide
import com.platepk.maker.domain.models.Province
import com.platepk.maker.domain.models.TextAlignment
import com.platepk.maker.domain.models.VehicleType
import com.platepk.maker.domain.repo.PlateRepository

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
            logoColor = style.logoColor ?: 0xFF000000

        )
    }
}