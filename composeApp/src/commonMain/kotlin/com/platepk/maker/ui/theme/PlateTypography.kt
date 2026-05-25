package com.platepk.maker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.fe_font // File name must match
import org.jetbrains.compose.resources.Font

object PlateTypography {

    @Composable
    fun getPlateFont(): FontFamily {
        return FontFamily(
            Font(resource = Res.font.fe_font, weight = FontWeight.Normal)
        )
    }
}