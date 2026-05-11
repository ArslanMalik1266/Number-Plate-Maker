package com.webscare.numberplatemaker.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.LogoAlignment
import com.webscare.numberplatemaker.domain.models.PlateConfig
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.ui.theme.PlateTypography
import com.webscare.numberplatemaker.util.PlatePlatformTextStyle
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.balochistan_logo
import numberplatemaker.composeapp.generated.resources.kpk_logo
import numberplatemaker.composeapp.generated.resources.punjab_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlateCanvas(
    config: PlateConfig,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val feFont = PlateTypography.getPlateFont()
    val punjabLogoPainter = painterResource(Res.drawable.punjab_logo)
    val kpkLogoPainter = painterResource(Res.drawable.kpk_logo)
    val BalochistanLogoPainter = painterResource(Res.drawable.balochistan_logo)

    Canvas(
        modifier = modifier
            .aspectRatio(config.dimensions.width / config.dimensions.height)
            .clipToBounds() // Ensure drawing doesn't bleed out
    )
    {
        val w = size.width
        val h = size.height

        // --- 1. BASE BACKGROUND & BORDER ---
        val density = androidx.compose.ui.platform.LocalDensity
        val strokeWidth = with(density) { 4.dp.toPx() }
        val fixedCornerRadius = with(density) { 16.dp.toPx() }
        drawRoundRect(
            color = Color(config.bgColor),
            size = size,
            cornerRadius = CornerRadius(fixedCornerRadius, fixedCornerRadius)
        )
        val padding = with(density) { 2.dp.toPx() }

        drawRoundRect(
            color = Color(config.borderColor),
            topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
            size = Size(w - strokeWidth, h - strokeWidth),
            cornerRadius = CornerRadius(fixedCornerRadius - padding, fixedCornerRadius - padding),
            style = Stroke(width = strokeWidth)
        )

        // --- 2. THE STRIP LOGIC (Sindh & Islamabad style) ---
        var contentOffsetX = 0f
        var contentOffsetY = 0f

        if (config.Strip) {
            val stripColor = Color(config.stripColor)
            when (config.stripOrientation) {
                StripOrientation.VERTICAL_LEFT -> {
                    // Sindh Style: Side bar
                    val stripWidth = w * config.stripSizeFraction
                    drawRect(color = stripColor, size = Size(stripWidth, h))
                    contentOffsetX = stripWidth
                }

                StripOrientation.Horizontal_TOP -> {
                    // Islamabad/New Style: Top bar
                    val stripHeight = h * config.stripSizeFraction
                    drawRect(color = stripColor, size = Size(w, stripHeight))
                    contentOffsetY = stripHeight
                }

                else -> {}
            }
        }


        when (config.provinceName.uppercase()) {

            "PUNJAB" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE-> {
                        if (config.side == PlateSide.FRONT) {
                            drawPunjabBikeFront(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                punjabLogoPainter
                            )
                        } else {
                            drawPunjabBikeRear(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                punjabLogoPainter
                            )
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        drawPunjabCarPlate(config, textMeasurer, w, h, feFont, punjabLogoPainter)
                    }

                    VehicleType.COMMERCIAL -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = punjabLogoPainter
                        )
                    }

                    VehicleType.GOVERNMENT -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = punjabLogoPainter
                        )
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = punjabLogoPainter
                        )
                    }

                    VehicleType.RICKSHAW -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.DIPLOMATIC -> {
                        drawPunjabCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = punjabLogoPainter
                        )
                    }
                }
            }

            "SINDH" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE -> {
                        // Sindh ki bike front aur back aksar ek jesi rectangular ya square hoti hain
                        if (config.side == PlateSide.FRONT) {
                            //               drawSindhBikeFront(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        } else {
                            //          drawSindhBikeRear(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        // Sindh Car: Vertical strip ke sath center aligned text
                        //       drawSindhCarPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.COMMERCIAL -> {
                        //      drawSindhCommercialPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.GOVERNMENT -> {
                        //     drawSindhGovtPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        //      drawSindhEVPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.RICKSHAW -> {
                        //      drawSindhRickshawPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        //      drawSindhHeavyPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.DIPLOMATIC -> {
                        //  drawSindhDiplomaticPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }
                }
            }

            "KHYBER PAKHTUNKHWA" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE -> {
                        if (config.side == PlateSide.FRONT) {
                            drawKpkBikeFront(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                kpkLogoPainter
                            )
                        } else {
                            drawKpkBikeRear(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                kpkLogoPainter
                            )
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        drawKpkCarPlate(config, textMeasurer, w, h, feFont, kpkLogoPainter)
                    }

                    VehicleType.COMMERCIAL -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.GOVERNMENT -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.RICKSHAW -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }

                    VehicleType.DIPLOMATIC -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = kpkLogoPainter
                        )
                    }
                }

            }

            "BALOCHISTAN" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE -> {
                        if (config.side == PlateSide.FRONT) {
                            drawKpkBikeFront(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                BalochistanLogoPainter
                            )
                        } else {
                            drawKpkBikeRear(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                BalochistanLogoPainter
                            )
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        drawKpkCarPlate(config, textMeasurer, w, h, feFont, BalochistanLogoPainter)
                    }

                    VehicleType.COMMERCIAL -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }

                    VehicleType.GOVERNMENT -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }

                    VehicleType.RICKSHAW -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }

                    VehicleType.DIPLOMATIC -> {
                        drawKpkCarPlate(
                            config = config,
                            textMeasurer = textMeasurer,
                            w = w,
                            h = h,
                            registrationFont = feFont,
                            logoPainter = BalochistanLogoPainter
                        )
                    }
                }
            }

            "ISLAMABAD" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE-> {
                        if (config.side == PlateSide.FRONT) {
                            //      drawIslamabadBikeFront(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        } else {
                            //      drawIslamabadBikeRear(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        // Islamabad Car: Blue Top Strip format ya classic format
                        //     drawIslamabadCarPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.COMMERCIAL -> {
                        //      drawIslamabadCommercialPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.GOVERNMENT -> {
                        //     drawIslamabadGovtPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.DIPLOMATIC -> {
                        // Islamabad mein Diplomatic plates (Red/Blue) bohot common hain
                        //      drawIslamabadDiplomaticPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.RICKSHAW -> {
                        //      drawIslamabadRickshawPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        //      drawIslamabadHeavyPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        //      drawIslamabadEVPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }
                }
            }

            "AJK" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE -> {
                        if (config.side == PlateSide.FRONT) {
                            //      drawAjkBikeFront(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        } else {
                            //     drawAjkBikeRear(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        // AJK Car: Top center "AJK" ya "AZAD KASHMIR" text
                        //    drawAjkCarPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.COMMERCIAL -> {
                        //    drawAjkCommercialPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.GOVERNMENT -> {
                        // Green background plates for AJK Govt
                        //     drawAjkGovtPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.RICKSHAW -> {
                        //      drawAjkRickshawPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        //       drawAjkHeavyPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        //       drawAjkEVPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.DIPLOMATIC -> {
                        //        drawAjkDiplomaticPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }
                }
            }

            "GB" -> {
                when (config.vehicleType) {
                    VehicleType.MOTORBIKE,
                    VehicleType.ELECTRIC_BIKE -> {
                        if (config.side == PlateSide.FRONT) {
                            //      drawGbBikeFront(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        } else {
                            //        drawGbBikeRear(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                        }
                    }

                    VehicleType.PRIVATE_CAR -> {
                        // GB Car: Clean and modern layout
                        //   drawGbCarPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.COMMERCIAL -> {
                        //    drawGbCommercialPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.GOVERNMENT -> {
                        //     drawGbGovtPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.RICKSHAW -> {
                        //     drawGbRickshawPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.HEAVY_TRANSPORT -> {
                        //     drawGbHeavyPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.ELECTRIC_CAR -> {
                        //   drawGbEVPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }

                    VehicleType.DIPLOMATIC -> {
                        //       drawGbDiplomaticPlate(config, textMeasurer, w, h, usableW, usableH, contentOffsetX, contentOffsetY)
                    }
                }
            }
        }
    }

}

//PUNJAB
private fun DrawScope.drawPunjabBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.04f

    val logoSize = h * 0.36f
    val logoCenterX = leftMargin + (logoSize / 2)
    val intrinsicSize = logoPainter.intrinsicSize
    val widthToHeightRatio = intrinsicSize.width / intrinsicSize.height
    val finalWidth = logoSize * widthToHeightRatio
    val finalHeight = logoSize
    val absoluteLogoCenterX = leftMargin + (finalWidth / 2)
    translate(left = leftMargin, top = h * 0.10f) {
        with(logoPainter) {
            draw(size = Size(finalWidth, finalHeight))
        }
    }

    val provText = textMeasurer.measure(
        text = config.provinceName,
        style = TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    val provX = absoluteLogoCenterX - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.52f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode, // "ET&NC"
            style = TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = absoluteLogoCenterX - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.67f))
    }
    val maxRegWidth = w - (leftMargin + finalWidth + (w * 0.08f))
    var regFontSize = (h * 0.56f).toSp()
    val customRegString = buildAnnotatedString {
        val rawText = config.registrationNumber
        append(rawText)
        rawText.forEachIndexed { index, char ->
            if (char == ' ') {
                addStyle(
                    style = SpanStyle(
                        letterSpacing = (-15).sp
                    ),
                    start = index,
                    end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString,
        style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString,
            style = TextStyle(
                fontSize = regFontSize,
                color = textColor,
                fontFamily = registrationFont,
                platformStyle = PlatePlatformTextStyle
            )
        )
    }
    val regX = (leftMargin + finalWidth) + (w - (leftMargin + finalWidth) - regText.size.width) / 2
    val verticalOffset = regFontSize.value * 0.3f
    val regY = (h / 2) - (regText.size.height / 2) - verticalOffset
    withTransform({
        // scaleX = 1.0 (Normal Width), scaleY = 1.3 (30% more height)
        // pivot as Offset(regX + width/2, regY + height/2) keeps it centered at its position
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(regX + regText.size.width / 2f, regY + regText.size.height / 2f)
        )
    }) {
        drawText(regText, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawPunjabBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.06f

    // --- 1. LOGO & PROVINCE BLOCK (Left Side) ---
    val logoSize = h * 0.12f
    val logoWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    // Logo draw karna
    translate(left = leftMargin, top = h * 0.18f) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoSize))
        }
    }

    // "PUNJAB" Text
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(),
        style = TextStyle(
            fontSize = (h * 0.04f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    )
    val provX = leftMargin + (logoWidth / 2) - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.31f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode,
            style = TextStyle(
                fontSize = (h * 0.04f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = leftMargin + (logoWidth / 2) - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.36f))
    }

    // --- 2. REGISTRATION (Total Plate Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val horizontalPadding = w * 0.08f
    val maxRegWidth = w - (horizontalPadding * 2)

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ),
            constraints = Constraints(maxWidth = maxRegWidth.toInt()),
            softWrap = true
        )

        if (regLayoutResult.lineCount > 2) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while (regLayoutResult.lineCount > 2 && regFontSize.value > 10f)

    val visualAdjustment = h * 0.05f
    // Horizontally and Vertically centered in the whole plate
    val regX = (w - regLayoutResult.size.width) / 2
    val regY = (h - regLayoutResult.size.height) / 2 - visualAdjustment

    val scaleY = 1.3f
    withTransform({
        scale(scaleX = 1.0f, scaleY = scaleY, pivot = Offset(w / 2, h / 2))
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawPunjabCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val horizontalPadding = w * 0.05f
    val topRowY = h * 0.10f

    // --- 1. TOP ROW: LOGO, BIG PROVINCE, & CODE ---

    val topRowCenterY = h * 0.15f // Thora niche taake border se door rahe

// Logo
    val logoHeight = h * 0.16f
    val logoWidth =
        logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    translate(left = horizontalPadding, top = topRowCenterY - (logoHeight / 2)) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoHeight))
        }
    }

    // "PUNJAB" (Big Center Text)
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(),
        style = TextStyle(
            fontSize = (h * 0.16f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 2.sp
        )
    )
// Vertical center alignment with logo
    drawText(
        provText,
        topLeft = Offset(
            (w / 2) - (provText.size.width / 2),
            topRowCenterY - (provText.size.height / 2)
        )
    )

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode,
            style = TextStyle(
                fontSize = (h * 0.08f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        // Offset check: w minus padding minus text width
        val codeX = w - horizontalPadding - codeText.size.width
        val codeY = topRowCenterY - (codeText.size.height / 2)

        drawText(codeText, topLeft = Offset(codeX, codeY))
    }

    val rawText = config.registrationNumber.trim().uppercase()
    val maxRegWidth = w - (horizontalPadding * 2)
    var regFontSize = (h * 0.58f).toSp()

    // AnnotatedString for custom gap logic
    val customRegString = buildAnnotatedString {
        append(rawText)
        rawText.forEachIndexed { index, char ->
            if (char == ' ') {
                // Space ki jagah gap control karne ke liye negative letterSpacing
                addStyle(
                    style = SpanStyle(letterSpacing = (-15).sp),
                    start = index,
                    end = index + 1
                )
            }
        }
    }

    var regLayoutResult = textMeasurer.measure(
        text = customRegString,
        style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            color = textColor,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        )
    )
    // Scaling logic for long strings
    if (regLayoutResult.size.width > maxRegWidth) {
        val factor = maxRegWidth / regLayoutResult.size.width
        regFontSize = (regFontSize.value * factor).sp
        regLayoutResult = textMeasurer.measure(
            text = customRegString,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                platformStyle = PlatePlatformTextStyle
            )
        )
    }

    val regX = (w - regLayoutResult.size.width) / 2
    val regY = (h * 0.38f) // Positioning below the top bar labels

    // --- 3. APPLY VERTICAL STRETCH (1.1x) ---
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f, // 1.1x Stretch as requested
            pivot = Offset(
                regX + regLayoutResult.size.width / 2f,
                regY + regLayoutResult.size.height / 2f
            )
        )
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

//KPK
private fun DrawScope.drawKpkBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.04f

    val logoSize = h * 0.36f
    val logoCenterX = leftMargin + (logoSize / 2)
    val intrinsicSize = logoPainter.intrinsicSize
    val widthToHeightRatio = intrinsicSize.width / intrinsicSize.height
    val finalWidth = logoSize * widthToHeightRatio
    val finalHeight = logoSize
    val absoluteLogoCenterX = leftMargin + (finalWidth / 2)
    translate(left = leftMargin, top = h * 0.10f) {
        with(logoPainter) {
            draw(size = Size(finalWidth, finalHeight))
        }
    }

    val provText = textMeasurer.measure(
        text = config.provinceName,
        style = TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    val provX = absoluteLogoCenterX - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.52f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode, // "ET&NC"
            style = TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = absoluteLogoCenterX - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.67f))
    }
    val maxRegWidth = w - (leftMargin + finalWidth + (w * 0.08f))
    var regFontSize = (h * 0.56f).toSp()
    val customRegString = buildAnnotatedString {
        val rawText = config.registrationNumber
        append(rawText)
        rawText.forEachIndexed { index, char ->
            if (char == ' ') {
                addStyle(
                    style = SpanStyle(
                        letterSpacing = (-15).sp
                    ),
                    start = index,
                    end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString,
        style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString,
            style = TextStyle(
                fontSize = regFontSize,
                color = textColor,
                fontFamily = registrationFont,
                platformStyle = PlatePlatformTextStyle
            )
        )
    }
    val regX = (leftMargin + finalWidth) + (w - (leftMargin + finalWidth) - regText.size.width) / 2
    val verticalOffset = regFontSize.value * 0.3f
    val regY = (h / 2) - (regText.size.height / 2) - verticalOffset
    withTransform({
        // scaleX = 1.0 (Normal Width), scaleY = 1.3 (30% more height)
        // pivot as Offset(regX + width/2, regY + height/2) keeps it centered at its position
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(regX + regText.size.width / 2f, regY + regText.size.height / 2f)
        )
    }) {
        drawText(regText, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawKpkBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.06f

    // --- 1. LOGO & PROVINCE BLOCK (Left Side) ---
    val logoSize = h * 0.12f
    val logoWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    // Logo draw karna
    translate(left = leftMargin, top = h * 0.18f) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoSize))
        }
    }

    // "PUNJAB" Text
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(),
        style = TextStyle(
            fontSize = (h * 0.04f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    )
    val provX = leftMargin + (logoWidth / 2) - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.31f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode,
            style = TextStyle(
                fontSize = (h * 0.04f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = leftMargin + (logoWidth / 2) - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.36f))
    }

    // --- 2. REGISTRATION (Total Plate Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val horizontalPadding = w * 0.08f
    val maxRegWidth = w - (horizontalPadding * 2)

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ),
            constraints = Constraints(maxWidth = maxRegWidth.toInt()),
            softWrap = true
        )

        if (regLayoutResult.lineCount > 2) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while (regLayoutResult.lineCount > 2 && regFontSize.value > 10f)

    val visualAdjustment = h * 0.05f
    // Horizontally and Vertically centered in the whole plate
    val regX = (w - regLayoutResult.size.width) / 2
    val regY = (h - regLayoutResult.size.height) / 2 - visualAdjustment

    val scaleY = 1.3f
    withTransform({
        scale(scaleX = 1.0f, scaleY = scaleY, pivot = Offset(w / 2, h / 2))
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawKpkCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val horizontalPadding = w * 0.08f // Padding thori barha di taake size thora chota lage

    // --- 1. TOP & BOTTOM BOUNDARIES ---
    val topRowCenterY = h * 0.15f
    val provText = textMeasurer.measure(config.provinceName.uppercase(), TextStyle(fontSize = (h * 0.12f).toSp(), fontWeight = FontWeight.ExtraBold, color = textColor))
    drawText(provText, topLeft = Offset((w / 2) - (provText.size.width / 2), topRowCenterY - (provText.size.height / 2)))
    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(config.cityName.uppercase(), TextStyle(fontSize = (h * 0.12f).toSp(), fontWeight = FontWeight.ExtraBold, color = textColor))
        val cityY = (h * 0.90f) - (cityNameText.size.height / 2)
        drawText(cityNameText, topLeft = Offset((w / 2) - (cityNameText.size.width / 2), cityY))
        cityTopEdge = cityY
    }

    // --- 2. SPLIT & MEASURE REGISTRATION ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    // Overall size thora chota karne ke liye available height 75% kar di
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.40f).toSp()

    val layout1 = textMeasurer.measure(firstPart, TextStyle(fontSize = baseFontSize, fontFamily = registrationFont))
    val layout2 = textMeasurer.measure(secondPart, TextStyle(fontSize = baseFontSize, fontFamily = registrationFont))

    // Logo size: Text ki height ka 70% (size chota karne ke liye)
    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth = logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check
    val totalComponentWidth = layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = w - (horizontalPadding * 2)

    val widthFactor = if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val finalLayout1 = textMeasurer.measure(firstPart, TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor))
    val finalLayout2 = textMeasurer.measure(secondPart, TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor))

    // --- 3. VERTICAL CENTERING (Text Focused) ---
    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (finalLayout1.size.height / 2f)

    // Logo Vertical Center relative to Text
    // Text ki bounding box ke bilkul darmiyan mein logo fit hoga
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    // --- 4. DRAWING ---
    val finalTotalWidth = finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width
    val startX = (w - finalTotalWidth) / 2f

    // First Part
    drawText(finalLayout1, topLeft = Offset(startX, textY))

    // Logo (Centered with Text)
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }

    // Second Part
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    drawText(finalLayout2, topLeft = Offset(secondPartX, textY))
}

//BALOCHISTAN

private fun DrawScope.drawBalochistanBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.04f

    val logoSize = h * 0.36f
    val logoCenterX = leftMargin + (logoSize / 2)
    val intrinsicSize = logoPainter.intrinsicSize
    val widthToHeightRatio = intrinsicSize.width / intrinsicSize.height
    val finalWidth = logoSize * widthToHeightRatio
    val finalHeight = logoSize
    val absoluteLogoCenterX = leftMargin + (finalWidth / 2)
    translate(left = leftMargin, top = h * 0.10f) {
        with(logoPainter) {
            draw(size = Size(finalWidth, finalHeight))
        }
    }

    val provText = textMeasurer.measure(
        text = config.provinceName,
        style = TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    val provX = absoluteLogoCenterX - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.52f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode, // "ET&NC"
            style = TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = absoluteLogoCenterX - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.67f))
    }
    val maxRegWidth = w - (leftMargin + finalWidth + (w * 0.08f))
    var regFontSize = (h * 0.56f).toSp()
    val customRegString = buildAnnotatedString {
        val rawText = config.registrationNumber
        append(rawText)
        rawText.forEachIndexed { index, char ->
            if (char == ' ') {
                addStyle(
                    style = SpanStyle(
                        letterSpacing = (-15).sp
                    ),
                    start = index,
                    end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString,
        style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString,
            style = TextStyle(
                fontSize = regFontSize,
                color = textColor,
                fontFamily = registrationFont,
                platformStyle = PlatePlatformTextStyle
            )
        )
    }
    val regX = (leftMargin + finalWidth) + (w - (leftMargin + finalWidth) - regText.size.width) / 2
    val verticalOffset = regFontSize.value * 0.3f
    val regY = (h / 2) - (regText.size.height / 2) - verticalOffset
    withTransform({
        // scaleX = 1.0 (Normal Width), scaleY = 1.3 (30% more height)
        // pivot as Offset(regX + width/2, regY + height/2) keeps it centered at its position
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(regX + regText.size.width / 2f, regY + regText.size.height / 2f)
        )
    }) {
        drawText(regText, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawBalochistanBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val leftMargin = w * 0.06f

    // --- 1. LOGO & PROVINCE BLOCK (Left Side) ---
    val logoSize = h * 0.12f
    val logoWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    // Logo draw karna
    translate(left = leftMargin, top = h * 0.18f) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoSize))
        }
    }

    // "PUNJAB" Text
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(),
        style = TextStyle(
            fontSize = (h * 0.04f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    )
    val provX = leftMargin + (logoWidth / 2) - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.31f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode,
            style = TextStyle(
                fontSize = (h * 0.04f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val codeX = leftMargin + (logoWidth / 2) - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.36f))
    }

    // --- 2. REGISTRATION (Total Plate Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val horizontalPadding = w * 0.08f
    val maxRegWidth = w - (horizontalPadding * 2)

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center,
                    trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ),
            constraints = Constraints(maxWidth = maxRegWidth.toInt()),
            softWrap = true
        )

        if (regLayoutResult.lineCount > 2) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while (regLayoutResult.lineCount > 2 && regFontSize.value > 10f)

    val visualAdjustment = h * 0.05f
    // Horizontally and Vertically centered in the whole plate
    val regX = (w - regLayoutResult.size.width) / 2
    val regY = (h - regLayoutResult.size.height) / 2 - visualAdjustment

    val scaleY = 1.3f
    withTransform({
        scale(scaleX = 1.0f, scaleY = scaleY, pivot = Offset(w / 2, h / 2))
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawBalochistanCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val horizontalPadding = w * 0.08f // Padding thori barha di taake size thora chota lage

    // --- 1. TOP & BOTTOM BOUNDARIES ---
    val topRowCenterY = h * 0.15f
    val provText = textMeasurer.measure(config.provinceName.uppercase(), TextStyle(fontSize = (h * 0.12f).toSp(), fontWeight = FontWeight.ExtraBold, color = textColor))
    drawText(provText, topLeft = Offset((w / 2) - (provText.size.width / 2), topRowCenterY - (provText.size.height / 2)))
    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(config.cityName.uppercase(), TextStyle(fontSize = (h * 0.12f).toSp(), fontWeight = FontWeight.ExtraBold, color = textColor))
        val cityY = (h * 0.90f) - (cityNameText.size.height / 2)
        drawText(cityNameText, topLeft = Offset((w / 2) - (cityNameText.size.width / 2), cityY))
        cityTopEdge = cityY
    }

    // --- 2. SPLIT & MEASURE REGISTRATION ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    // Overall size thora chota karne ke liye available height 75% kar di
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.40f).toSp()

    val layout1 = textMeasurer.measure(firstPart, TextStyle(fontSize = baseFontSize, fontFamily = registrationFont))
    val layout2 = textMeasurer.measure(secondPart, TextStyle(fontSize = baseFontSize, fontFamily = registrationFont))

    // Logo size: Text ki height ka 70% (size chota karne ke liye)
    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth = logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check
    val totalComponentWidth = layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = w - (horizontalPadding * 2)

    val widthFactor = if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val finalLayout1 = textMeasurer.measure(firstPart, TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor))
    val finalLayout2 = textMeasurer.measure(secondPart, TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor))

    // --- 3. VERTICAL CENTERING (Text Focused) ---
    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (finalLayout1.size.height / 2f)

    // Logo Vertical Center relative to Text
    // Text ki bounding box ke bilkul darmiyan mein logo fit hoga
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    // --- 4. DRAWING ---
    val finalTotalWidth = finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width
    val startX = (w - finalTotalWidth) / 2f

    // First Part
    drawText(finalLayout1, topLeft = Offset(startX, textY))

    // Logo (Centered with Text)
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }

    // Second Part
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    drawText(finalLayout2, topLeft = Offset(secondPartX, textY))
}
