package com.webscare.numberplatemaker.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
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
import com.webscare.numberplatemaker.domain.models.PlateConfig
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.ui.theme.PlateTypography
import com.webscare.numberplatemaker.util.PlatePlatformTextStyle
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.ajk_logo
import numberplatemaker.composeapp.generated.resources.ajk_strip_bg
import numberplatemaker.composeapp.generated.resources.ajrak_bg
import numberplatemaker.composeapp.generated.resources.balochistan_logo
import numberplatemaker.composeapp.generated.resources.gb_logo
import numberplatemaker.composeapp.generated.resources.islamabad_logo
import numberplatemaker.composeapp.generated.resources.islamabad_strip_logo
import numberplatemaker.composeapp.generated.resources.kpk_logo
import numberplatemaker.composeapp.generated.resources.punjab_logo
import numberplatemaker.composeapp.generated.resources.sindh_logo
import org.jetbrains.compose.resources.painterResource
import kotlin.math.ceil

@Composable
fun PlateCanvas(
    config: PlateConfig, modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val feFont = PlateTypography.getPlateFont()
    val punjabLogoPainter = painterResource(Res.drawable.punjab_logo)
    val kpkLogoPainter = painterResource(Res.drawable.kpk_logo)
    val sindhLogoPainter = painterResource(Res.drawable.sindh_logo)
    val ajkLogoPainter = painterResource(Res.drawable.ajk_logo)
    val gbLogoPainter = painterResource(Res.drawable.gb_logo)
    val islamabadLogoPainter = painterResource(Res.drawable.islamabad_logo)
    val islamabadStripLogoPainter = painterResource(Res.drawable.islamabad_strip_logo)
    val ajkStripLogoPainter = painterResource(Res.drawable.ajk_strip_bg)
    val ajrakPainter = painterResource(Res.drawable.ajrak_bg)

    val BalochistanLogoPainter = painterResource(Res.drawable.balochistan_logo)

    Canvas(
        modifier = modifier.aspectRatio(config.dimensions.width / config.dimensions.height)
            .clipToBounds() // Ensure drawing doesn't bleed out
    ) {
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
        if (config.provinceName.equals("Diplomatic", ignoreCase = true)) {
            drawDiplomaticPlate(config, textMeasurer, w, h, feFont)
        } else {

            when (config.provinceName.uppercase()) {

                "PUNJAB" -> {
                    when (config.vehicleType) {
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawPunjabBikeFront(
                                    config, textMeasurer, w, h, feFont, punjabLogoPainter
                                )
                            } else {
                                drawPunjabBikeRear(
                                    config, textMeasurer, w, h, feFont, punjabLogoPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {
                            drawPunjabCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                punjabLogoPainter
                            )
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
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawSindhBikeFront(
                                    config = config,
                                    textMeasurer = textMeasurer,
                                    w = w,
                                    h = h,
                                    registrationFont = feFont,
                                    logoPainter = sindhLogoPainter,
                                    ajrakPainter
                                )
                            } else {
                                drawSindhBikeRear(
                                    config = config,
                                    textMeasurer = textMeasurer,
                                    w = w,
                                    h = h,
                                    registrationFont = feFont,
                                    logoPainter = sindhLogoPainter,
                                    ajrakPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.COMMERCIAL -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.GOVERNMENT -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.ELECTRIC_CAR -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.RICKSHAW -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.HEAVY_TRANSPORT -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }

                        VehicleType.DIPLOMATIC -> {
                            drawSindhCarPlate(
                                config = config,
                                textMeasurer = textMeasurer,
                                w = w,
                                h = h,
                                registrationFont = feFont,
                                logoPainter = sindhLogoPainter,
                                ajrakBgPainter = ajrakPainter
                            )
                        }
                    }
                }

                "KHYBER PAKHTUNKHWA" -> {
                    when (config.vehicleType) {
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawKpkBikeFront(
                                    config, textMeasurer, w, h, feFont, kpkLogoPainter
                                )
                            } else {
                                drawKpkBikeRear(
                                    config, textMeasurer, w, h, feFont, kpkLogoPainter
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
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawBalochistanBikeFront(
                                    config, textMeasurer, w, h, feFont, BalochistanLogoPainter
                                )
                            } else {
                                drawBalochistanBikeRear(
                                    config, textMeasurer, w, h, feFont, BalochistanLogoPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {

                            drawBalochistanCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                BalochistanLogoPainter
                            )
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
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawIslamabadBikeFront(
                                    config,
                                    textMeasurer,
                                    w,
                                    h,
                                    feFont,
                                    islamabadLogoPainter,
                                    islamabadStripLogoPainter
                                )
                            } else {
                                drawIslamabadBikeRear(
                                    config,
                                    textMeasurer,
                                    w,
                                    h,
                                    feFont,
                                    islamabadLogoPainter,
                                    islamabadStripLogoPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.COMMERCIAL -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.GOVERNMENT -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.DIPLOMATIC -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.RICKSHAW -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.HEAVY_TRANSPORT -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }

                        VehicleType.ELECTRIC_CAR -> {
                            drawIslamabadCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                islamabadLogoPainter,
                                islamabadStripLogoPainter
                            )
                        }
                    }
                }

                "AJ&K" -> {
                    when (config.vehicleType) {
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawAjkBikeFront(
                                    config,
                                    textMeasurer,
                                    w,
                                    h,
                                    feFont,
                                    ajkLogoPainter,
                                    ajkStripLogoPainter
                                )
                            } else {
                                drawAjkBikeRear(
                                    config,
                                    textMeasurer,
                                    w,
                                    h,
                                    feFont,
                                    ajkLogoPainter,
                                    ajkStripLogoPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.COMMERCIAL -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.GOVERNMENT -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.RICKSHAW -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.HEAVY_TRANSPORT -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.ELECTRIC_CAR -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }

                        VehicleType.DIPLOMATIC -> {
                            drawAjkCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                ajkLogoPainter,
                                ajkStripLogoPainter
                            )
                        }
                    }
                }

                "GB" -> {
                    when (config.vehicleType) {
                        VehicleType.MOTORBIKE, VehicleType.ELECTRIC_BIKE -> {
                            if (config.side == PlateSide.FRONT) {
                                drawGbBikeFront(
                                    config, textMeasurer, w, h, feFont, gbLogoPainter
                                )
                            } else {
                                drawGbBikeRear(
                                    config, textMeasurer, w, h, feFont, gbLogoPainter
                                )
                            }
                        }

                        VehicleType.PRIVATE_CAR -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.COMMERCIAL -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.GOVERNMENT -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.RICKSHAW -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.HEAVY_TRANSPORT -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.ELECTRIC_CAR -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }

                        VehicleType.DIPLOMATIC -> {
                            drawGbCarPlate(
                                config,
                                textMeasurer,
                                w,
                                h,
                                feFont,
                                gbLogoPainter
                            )
                        }
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
            draw(
                size = Size(finalWidth, finalHeight),
                // Use ColorFilter here to apply the black color from config
                colorFilter = ColorFilter.tint(Color(config.logoColor))
            )
        }
    }

    val provText = textMeasurer.measure(
        text = config.provinceName, style = TextStyle(
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
                fontSize = (h * 0.12f).toSp(), fontWeight = FontWeight.Bold, color = textColor
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
                    ), start = index, end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString, style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            color = textColor,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString, style = TextStyle(
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
            draw(
                size = Size(logoWidth, logoSize),
                // This applies your black color from the config
                colorFilter = ColorFilter.tint(Color(config.logoColor))
            )
        }
    }

    // "PUNJAB" Text
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(), style = TextStyle(
            fontSize = (h * 0.04f).toSp(), fontWeight = FontWeight.ExtraBold, color = textColor
        )
    )
    val provX = leftMargin + (logoWidth / 2) - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.31f))

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode, style = TextStyle(
                fontSize = (h * 0.04f).toSp(), fontWeight = FontWeight.Bold, color = textColor
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
            text = formattedReg, style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ), constraints = Constraints(maxWidth = maxRegWidth.toInt()), softWrap = true
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
            draw(
                size = Size(logoWidth, logoHeight),
                // Use ColorFilter to apply your black logo color
                colorFilter = ColorFilter.tint(Color(config.logoColor))
            )
        }
    }

    // "PUNJAB" (Big Center Text)
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(), style = TextStyle(
            fontSize = (h * 0.16f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 2.sp
        )
    )
// Vertical center alignment with logo
    drawText(
        provText, topLeft = Offset(
            (w / 2) - (provText.size.width / 2), topRowCenterY - (provText.size.height / 2)
        )
    )

    if (config.provinceCode.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.provinceCode, style = TextStyle(
                fontSize = (h * 0.08f).toSp(), fontWeight = FontWeight.Bold, color = textColor
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
                    style = SpanStyle(letterSpacing = (-15).sp), start = index, end = index + 1
                )
            }
        }
    }

    var regLayoutResult = textMeasurer.measure(
        text = customRegString, style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            color = textColor,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
            )
        )
    )
    // Scaling logic for long strings
    if (regLayoutResult.size.width > maxRegWidth) {
        val factor = maxRegWidth / regLayoutResult.size.width
        regFontSize = (regFontSize.value * factor).sp
        regLayoutResult = textMeasurer.measure(
            text = customRegString, style = TextStyle(
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
            scaleX = 1.0f, scaleY = 1.3f, // 1.1x Stretch as requested
            pivot = Offset(
                regX + regLayoutResult.size.width / 2f, regY + regLayoutResult.size.height / 2f
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
    val provTextValue = config.provinceName.uppercase().replace(" ", "\n")

    val provText = textMeasurer.measure(
        text = provTextValue, style = TextStyle(
            fontSize = (h * 0.06f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    )
    val provX = absoluteLogoCenterX - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.52f))


    if (config.cityName.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.cityName, // "ET&NC"
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(), fontWeight = FontWeight.Bold, color = textColor
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
                    ), start = index, end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString, style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString, style = TextStyle(
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

    // Default values set karein taake agar text empty ho to code crash na kare
    var provinceBottomEdge = 0f
    var cityTopEdge = h

    // --- 1. PROVINCE NAME (Top Centre) ---
    if (config.provinceName.isNotEmpty()) {
        val provLayout = textMeasurer.measure(
            text = config.provinceName.uppercase(),
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val provX = (w / 2f) - (provLayout.size.width / 2f)
        val provY = h * 0.04f
        drawText(provLayout, topLeft = Offset(provX, provY))

        // Value update: Top margin + text ki apni height
        provinceBottomEdge = provY + provLayout.size.height
    }

    // --- 2. CITY NAME (Bottom Centre) ---
    if (config.cityName.isNotEmpty()) {
        val cityLayout = textMeasurer.measure(
            text = config.cityName.uppercase(),
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val cityX = (w / 2f) - (cityLayout.size.width / 2f)
        val cityY = h - cityLayout.size.height - (h * 0.04f)
        drawText(cityLayout, topLeft = Offset(cityX, cityY))

        // Value update: Jahan city text shuru ho raha hai
        cityTopEdge = cityY
    }

    // --- 3. REGISTRATION (Gap Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val maxRegWidth = w * 0.85f

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult
    val availableGap = cityTopEdge - provinceBottomEdge

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = regFontSize * 1.1f // Lines overlap na hon
            ),
            constraints = Constraints(maxWidth = maxRegWidth.toInt())
        )

        // Stretch factor (1.3f) ko include karke check kar rahe hain ke text gap se bahar to nahi ja raha
        val totalScaledHeight = regLayoutResult.size.height * 1.3f
        val isTooBig =
            totalScaledHeight > (availableGap * 0.9f) // 90% gap use karein safety ke liye

        if (regLayoutResult.lineCount > 2 || isTooBig) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while ((regLayoutResult.lineCount > 2 || (regLayoutResult.size.height * 1.3f) > (availableGap * 0.9f)) && regFontSize.value > 8f)

    // Positioning logic (Same)
    val gapCenterY = provinceBottomEdge + (availableGap / 2f)
    val regX = (w / 2f) - (regLayoutResult.size.width / 2f)
    val vertical_upside = h * 0.03f
    val regY = gapCenterY - (regLayoutResult.size.height / 2f) - vertical_upside

    withTransform({
        scale(scaleX = 1.0f, scaleY = 1.3f, pivot = Offset(w / 2f, gapCenterY))
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
    val provText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    )
    drawText(
        provText,
        topLeft = Offset(
            (w / 2) - (provText.size.width / 2),
            topRowCenterY - (provText.size.height / 2)
        )
    )
    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(
            config.cityName.uppercase(),
            TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
        )
        val cityY = (h * 0.90f) - (cityNameText.size.height / 2)
        drawText(cityNameText, topLeft = Offset((w / 2) - (cityNameText.size.width / 2), cityY))
        cityTopEdge = cityY
    }

    // --- 2. SPLIT & MEASURE REGISTRATION ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""
    val verticalShift = -(h * 0.06f)

    // Overall size thora chota karne ke liye available height 75% kar di
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.40f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    // Logo size: Text ki height ka 70% (size chota karne ke liye)
    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth =
        logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check
    val totalComponentWidth =
        layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = w - (horizontalPadding * 2)

    val widthFactor =
        if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val finalLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val finalLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    // --- 3. VERTICAL CENTERING (Text Focused) ---
    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (finalLayout1.size.height / 2f)

    // Logo Vertical Center relative to Text
    // Text ki bounding box ke bilkul darmiyan mein logo fit hoga
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    // --- 4. DRAWING ---
    // --- 4. DRAWING ---
    val finalTotalWidth =
        finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width
    val startX = (w - finalTotalWidth) / 2f

    // 1. First Part (e.g., ALE) - Scaled and Offset
    withTransform({
        // Vertically stretch the text by 1.3x
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(startX, textY + finalLayout1.size.height / 2f)
        )
    }) {
        drawText(finalLayout1, topLeft = Offset(startX, textY + verticalShift))
    }

    // 2. Logo (Original shape maintain karega)
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }

    // 3. Second Part (e.g., 931) - Scaled and Offset
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(secondPartX, textY + finalLayout2.size.height / 2f)
        )
    }) {
        drawText(finalLayout2, topLeft = Offset(secondPartX, textY + verticalShift))
    }
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
    val provTextValue = config.provinceName.uppercase().replace(" ", "\n")

    val provText = textMeasurer.measure(
        text = provTextValue, style = TextStyle(
            fontSize = (h * 0.06f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    )
    val provX = absoluteLogoCenterX - (provText.size.width / 2)
    drawText(provText, topLeft = Offset(provX, h * 0.52f))


    if (config.cityName.isNotEmpty()) {
        val codeText = textMeasurer.measure(
            text = config.cityName, // "ET&NC"
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(), fontWeight = FontWeight.Bold, color = textColor
            )
        )
        val codeX = absoluteLogoCenterX - (codeText.size.width / 2)
        drawText(codeText, topLeft = Offset(codeX, h * 0.60f))
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
                    ), start = index, end = index + 1
                )
            }
        }
    }
    var regText = textMeasurer.measure(
        text = customRegString, style = TextStyle(
            fontSize = regFontSize,
            fontFamily = registrationFont,
            platformStyle = PlatePlatformTextStyle,
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
            ),
        )
    )
    if (regText.size.width > maxRegWidth) {
        val scaleFactor = maxRegWidth / regText.size.width
        regFontSize = (regFontSize.value * scaleFactor).sp
        regText = textMeasurer.measure(
            text = customRegString, style = TextStyle(
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

    // Default values set karein taake agar text empty ho to code crash na kare
    var provinceBottomEdge = 0f
    var cityTopEdge = h

    // --- 1. PROVINCE NAME (Top Centre) ---
    if (config.provinceName.isNotEmpty()) {
        val provLayout = textMeasurer.measure(
            text = config.provinceName.uppercase(),
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val provX = (w / 2f) - (provLayout.size.width / 2f)
        val provY = h * 0.04f
        drawText(provLayout, topLeft = Offset(provX, provY))

        // Value update: Top margin + text ki apni height
        provinceBottomEdge = provY + provLayout.size.height
    }

    // --- 2. CITY NAME (Bottom Centre) ---
    if (config.cityName.isNotEmpty()) {
        val cityLayout = textMeasurer.measure(
            text = config.cityName.uppercase(),
            style = TextStyle(
                fontSize = (h * 0.06f).toSp(),
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
        val cityX = (w / 2f) - (cityLayout.size.width / 2f)
        val cityY = h - cityLayout.size.height - (h * 0.04f)
        drawText(cityLayout, topLeft = Offset(cityX, cityY))

        // Value update: Jahan city text shuru ho raha hai
        cityTopEdge = cityY
    }

    // --- 3. REGISTRATION (Gap Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val maxRegWidth = w * 0.85f

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult
    val availableGap = cityTopEdge - provinceBottomEdge

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg,
            style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                lineHeight = regFontSize * 1.1f // Lines overlap na hon
            ),
            constraints = Constraints(maxWidth = maxRegWidth.toInt())
        )

        // Stretch factor (1.3f) ko include karke check kar rahe hain ke text gap se bahar to nahi ja raha
        val totalScaledHeight = regLayoutResult.size.height * 1.3f
        val isTooBig =
            totalScaledHeight > (availableGap * 0.9f) // 90% gap use karein safety ke liye

        if (regLayoutResult.lineCount > 2 || isTooBig) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while ((regLayoutResult.lineCount > 2 || (regLayoutResult.size.height * 1.3f) > (availableGap * 0.9f)) && regFontSize.value > 8f)

    // Positioning logic (Same)
    val gapCenterY = provinceBottomEdge + (availableGap / 2f)
    val regX = (w / 2f) - (regLayoutResult.size.width / 2f)
    val vertical_upside = h * 0.03f
    val regY = gapCenterY - (regLayoutResult.size.height / 2f) - vertical_upside

    withTransform({
        scale(scaleX = 1.0f, scaleY = 1.3f, pivot = Offset(w / 2f, gapCenterY))
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
    val provText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    )
    drawText(
        provText,
        topLeft = Offset(
            (w / 2) - (provText.size.width / 2),
            topRowCenterY - (provText.size.height / 2)
        )
    )
    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(
            config.cityName.uppercase(),
            TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
        )
        val cityY = (h * 0.90f) - (cityNameText.size.height / 2)
        drawText(cityNameText, topLeft = Offset((w / 2) - (cityNameText.size.width / 2), cityY))
        cityTopEdge = cityY
    }

    // --- 2. SPLIT & MEASURE REGISTRATION ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""
    val verticalShift = -(h * 0.06f)

    // Overall size thora chota karne ke liye available height 75% kar di
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.40f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    // Logo size: Text ki height ka 70% (size chota karne ke liye)
    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth =
        logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check
    val totalComponentWidth =
        layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = w - (horizontalPadding * 2)

    val widthFactor =
        if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val finalLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val finalLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    // --- 3. VERTICAL CENTERING (Text Focused) ---
    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (finalLayout1.size.height / 2f)


    // Logo Vertical Center relative to Text
    // Text ki bounding box ke bilkul darmiyan mein logo fit hoga
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    // --- 4. DRAWING ---
    val finalTotalWidth =
        finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width
    val startX = (w - finalTotalWidth) / 2f

    // 1. First Part (ALE) - Scaled and Offset
    withTransform({
        // 1.3f scaleY text ko lamba karega
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(startX, textY + finalLayout1.size.height / 2f)
        )
    }) {
        drawText(finalLayout1, topLeft = Offset(startX, textY + verticalShift))
    }

    // 2. Logo (Original Shape, No Offset)
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }

    // 3. Second Part (931) - Scaled and Offset
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(secondPartX, textY + finalLayout2.size.height / 2f)
        )
    }) {
        drawText(finalLayout2, topLeft = Offset(secondPartX, textY + verticalShift))
    }
}

//SINDH
private fun DrawScope.drawSindhBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    ajrakBgPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val stripWidth = w * 0.15f
    val borderThickness = w * 0.01f
    val cornerRadius = h * 0.10f
    val contentLeftEdge = stripWidth
    val availableWidth = w - contentLeftEdge
    val horizontalPadding = availableWidth * 0.08f
    val roundedPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    left = borderThickness,
                    top = borderThickness,
                    right = stripWidth, // Width constrained to strip
                    bottom = h - borderThickness
                ),
                topLeft = CornerRadius(cornerRadius),
                bottomLeft = CornerRadius(cornerRadius),
                topRight = CornerRadius(0f),
                bottomRight = CornerRadius(0f)
            )
        )
    }
    clipPath(roundedPath) {
        clipRect(top = 0f, left = 0f, right = stripWidth, bottom = h * 0.70f) {
            withTransform({
                rotate(90f, pivot = Offset(0f, 0f))
                translate(left = 0f, top = -stripWidth)
            }) {
                with(ajrakBgPainter) {
                    val intrinsicW = intrinsicSize.width
                    val intrinsicH = intrinsicSize.height
                    val scale = (stripWidth / intrinsicH) * 0.95f
                    draw(size = Size(intrinsicW * scale, intrinsicH * scale))
                }
            }
        }
    }
    var provinceTopEdge = h
    if (config.provinceName.isNotEmpty()) {
        val provinceNameText = textMeasurer.measure(
            config.provinceName.uppercase(),
            TextStyle(
                fontSize = (h * 0.09f).toSp(),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = textColor
            )
        )
        val provX = ((stripWidth + borderThickness) / 2f) - (provinceNameText.size.width / 2f)
        val provY = (h * 0.80f) - (provinceNameText.size.height / 2)
        drawText(provinceNameText, topLeft = Offset(provX, provY))
        provinceTopEdge = provY
    }
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val maxAvailableHeight = h * 0.70f // Fixed height constraint for center alignment
    val baseFontSize = (h * 0.44f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth =
        logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check within 80% area
    val totalComponentWidth =
        layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = availableWidth - (horizontalPadding * 2)

    val widthFactor =
        if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val regStyle =
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    val finalLayout1 = textMeasurer.measure(firstPart, regStyle)
    val finalLayout2 = textMeasurer.measure(secondPart, regStyle)

    // --- 5. POSITIONING & DRAWING ---
    val textY = (h / 2f) - (finalLayout1.size.height / 2f)
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    val finalTotalWidth =
        finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width

    // startX starts after contentLeftEdge
    val startX = contentLeftEdge + (availableWidth - finalTotalWidth) / 2f

    // Draw First Part
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(
                startX + finalLayout1.size.width / 2f,
                textY + finalLayout1.size.height / 2f
            )
        )
    }) {
        drawText(finalLayout1, topLeft = Offset(startX, textY))
    }

    // Draw Logo
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }

    // Draw Second Part
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(
                secondPartX + finalLayout2.size.width / 2f,
                textY + finalLayout2.size.height / 2f
            )
        )
    }) {
        drawText(finalLayout2, topLeft = Offset(secondPartX, textY))
    }
}

private fun DrawScope.drawSindhBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    ajrakBgPainter: androidx.compose.ui.graphics.painter.Painter


) {
    val leftMargin = w * 0.06f
    val textColor = Color(config.textColor)
    val stripWidth = w * 0.15f
    val borderThickness = w * 0.01f
    val cornerRadius = h * 0.04f

    // --- 1. VERTICAL STRIP PATH ---
    val roundedPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    left = borderThickness,
                    top = borderThickness,
                    right = stripWidth,
                    bottom = h - borderThickness
                ),
                topLeft = CornerRadius(cornerRadius),
                bottomLeft = CornerRadius(cornerRadius),
                topRight = CornerRadius(0f),
                bottomRight = CornerRadius(0f)
            )
        )
    }

    // --- 2. DRAW AJRAK (Top 50% of the strip) ---
    val ajrakHeight = h * 0.50f
    clipPath(roundedPath) {
        clipRect(top = 0f, left = 0f, right = stripWidth, bottom = h * 0.50f) {
            withTransform({
                rotate(90f, pivot = Offset(0f, 0f))
                translate(left = 0f, top = -stripWidth)
            }) {
                // Sahi tareeka ye hai:
                with(ajrakBgPainter) {
                    val intrinsicW = intrinsicSize.width
                    val intrinsicH = intrinsicSize.height
                    val scale = (stripWidth / intrinsicH) * 0.95f
                    draw(size = Size(intrinsicW * scale, intrinsicH * scale))
                }
            }
        }
    }

    // --- 3. LOGO & PROVINCE (Inside Strip, Below Ajrak) ---
    val logoSize = h * 0.10f
    val logoWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    // 1. Province Text (Ab ye Ajrak ke foran baad aayega)
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(),
        style = TextStyle(
            fontSize = (h * 0.04f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    val provX = (stripWidth / 2f) - (provText.size.width / 2f)
    val provY = ajrakHeight + (h * 0.03f) // Ajrak ke niche Province
    drawText(provText, topLeft = Offset(provX, provY))

    // 2. Logo (Province ke niche)
    val logoX = (stripWidth / 2f) - (logoWidth / 2f)
    val logoY = provY + provText.size.height + (h * 0.02f) // Province ke niche Logo

    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoSize))
        }
    }
    // --- 2. REGISTRATION (Total Plate Center) ---
    val formattedReg = config.registrationNumber.replace("-", " ")
    val horizontalPadding = w * 0.08f
    val maxRegWidth = w - (horizontalPadding * 2)

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg, style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ), constraints = Constraints(maxWidth = maxRegWidth.toInt()), softWrap = true
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

private fun DrawScope.drawSindhCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    ajrakBgPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val horizontalPadding = w * 0.08f
    val ajrakHeight = h * 0.25f
    val borderThickness = w * 0.01f
    val cornerRadius = h * 0.06f

    val roundedPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    left = borderThickness,
                    top = borderThickness,
                    right = w - borderThickness,
                    bottom = ajrakHeight
                ),
                topLeft = CornerRadius(cornerRadius),
                topRight = CornerRadius(cornerRadius),
                bottomLeft = CornerRadius(0f),
                bottomRight = CornerRadius(0f)
            )
        )
    }
    clipPath(roundedPath) {
        val intrinsicSize = ajrakBgPainter.intrinsicSize
        val zoomFactor = 1f
        val scale = (ajrakHeight / intrinsicSize.height) * zoomFactor
        val scaledWidth = intrinsicSize.width * scale
        val scaledHeight = intrinsicSize.height * scale
        val tilesNeeded = ceil((w - (borderThickness * 2)) / scaledWidth).toInt()
        for (i in 0..tilesNeeded) {
            val dx = borderThickness + (i * scaledWidth)
            val dy = borderThickness
            translate(left = dx, top = dy) {
                with(ajrakBgPainter) {
                    draw(size = Size(scaledWidth, scaledHeight))
                }
            }
        }
    }
    val ajrakBottomEdge = ajrakHeight

    var provinceTopEdge = h
    if (config.provinceName.isNotEmpty()) {
        val provinceNameText = textMeasurer.measure(
            config.provinceName.uppercase(),
            TextStyle(
                fontSize = (h * 0.12f).toSp(),
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp,
                color = textColor
            )
        )
        val cityY = (h * 0.90f) - (provinceNameText.size.height / 2)
        drawText(
            provinceNameText,
            topLeft = Offset((w / 2) - (provinceNameText.size.width / 2), cityY)
        )
        provinceTopEdge = cityY
    }

    // --- 2. SPLIT & MEASURE REGISTRATION ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val maxAvailableHeight = (provinceTopEdge - ajrakBottomEdge) * 0.80f
    val baseFontSize = (h * 0.44f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    // Logo size: Text ki height ka 70% (size chota karne ke liye)
    val logoHeight = layout1.size.height.toFloat() * 0.7f
    val logoWidth =
        logoHeight * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)
    val internalGap = w * 0.03f

    // Total width check
    val totalComponentWidth =
        layout1.size.width + logoWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = w - (horizontalPadding * 2)

    val widthFactor =
        if (totalComponentWidth > maxAvailableWidth) maxAvailableWidth / totalComponentWidth else 1f
    val heightFactor = maxAvailableHeight / layout1.size.height
    val finalScale = minOf(widthFactor, heightFactor)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalLogoWidth = logoWidth * finalScale
    val finalLogoHeight = logoHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val finalLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val finalLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    // --- 3. VERTICAL CENTERING (Text Focused) ---
    val gapCenterY = ajrakBottomEdge + ((provinceTopEdge - ajrakBottomEdge) / 2f)
    val textY = gapCenterY - (finalLayout1.size.height / 2f)

    val verticalShift = -(h * 0.06f)
    // Logo Vertical Center relative to Text
    // Text ki bounding box ke bilkul darmiyan mein logo fit hoga
    val logoY = textY + (finalLayout1.size.height / 2f) - (finalLogoHeight / 2f)

    // --- 4. DRAWING ---

    val finalTotalWidth =
        finalLayout1.size.width + finalLogoWidth + (finalInternalGap * 2) + finalLayout2.size.width
    val startX = (w - finalTotalWidth) / 2f
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(startX, textY + finalLayout1.size.height / 2f)
        )
    }) {
        drawText(finalLayout1, topLeft = Offset(startX, textY + verticalShift))
    }
    val logoX = startX + finalLayout1.size.width + finalInternalGap
    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(finalLogoWidth, finalLogoHeight))
        }
    }
    val secondPartX = logoX + finalLogoWidth + finalInternalGap
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(secondPartX, textY + finalLayout2.size.height / 2f)
        )
    }) {
        drawText(finalLayout2, topLeft = Offset(secondPartX, textY + verticalShift))
    }
}

//AJ&K
private fun DrawScope.drawAjkBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    ajkLogoPainter: androidx.compose.ui.graphics.painter.Painter, // Centered Logo
    ajkStripBg: androidx.compose.ui.graphics.painter.Painter    // Full Height Strip BG
) {
    val textColor = Color(config.textColor)

    // --- 0. STRIP AREA CALCULATIONS (25% of Plate) ---
    val borderStrokeWidth = 10f
    val totalStripAreaWidth = w * 0.25f
    val bgWidth = w * 0.06f
    val bgX = totalStripAreaWidth - bgWidth


    translate(left = bgX, top = borderStrokeWidth) {
        with(ajkStripBg) {
            draw(size = Size(bgWidth, h - (borderStrokeWidth * 2)))
        }
    }

    // 2. Draw "ajk_logo" (Centered inside the 25% Strip Area)
    val availableSpaceForLogo = totalStripAreaWidth - bgWidth - borderStrokeWidth
    val logoSize = totalStripAreaWidth * 0.7f // Logo thora chota rakha taake center mein fit ho
    val logoX = borderStrokeWidth + (availableSpaceForLogo / 2f) - (logoSize / 2f)
    val logoY = (h - logoSize) / 2f

    translate(left = logoX, top = logoY) {
        with(ajkLogoPainter) {
            draw(size = Size(logoSize, logoSize))
        }
    }

    // --- Content Boundaries (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth
    val horizontalPadding = availableContentWidth * 0.08f

    // --- 1. PROVINCE & CITY ---
    val topRowCenterY = h * 0.13f
    val provText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.18f).toSp(),
            fontWeight = FontWeight.Medium,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    drawText(
        provText,
        topLeft = Offset(
            contentLeftEdge + (availableContentWidth / 2) - (provText.size.width / 2),
            topRowCenterY - (provText.size.height / 2)
        )
    )

    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(
            config.cityName.uppercase(),
            TextStyle(
                fontSize = (h * 0.18f).toSp(),
                fontWeight = FontWeight.Medium,
                color = textColor,
                letterSpacing = 1.sp
            )
        )
        val cityY = (h * 0.87f) - (cityNameText.size.height / 2)
        drawText(
            cityNameText,
            topLeft = Offset(
                contentLeftEdge + (availableContentWidth / 2) - (cityNameText.size.width / 2),
                cityY
            )
        )
        cityTopEdge = cityY
    }

    // --- 2. REGISTRATION WITH SQUARE DOT ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val verticalShift = -(h * 0.04f)
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.38f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    val squareSize = layout1.size.height * 0.25f
    val internalGap = w * 0.03f
    val dotAdjustment = h * 0.04f

    val totalComponentWidth =
        layout1.size.width + squareSize + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = availableContentWidth - (horizontalPadding * 2)

    val finalScale = minOf(
        maxAvailableWidth / totalComponentWidth,
        maxAvailableHeight / layout1.size.height
    ).coerceAtMost(1f)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalSquareSize = squareSize * finalScale
    val finalInternalGap = internalGap * finalScale

    val fLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (fLayout1.size.height / 2f)

    val finalTotalWidth =
        fLayout1.size.width + finalSquareSize + (finalInternalGap * 2) + fLayout2.size.width
    val startX = contentLeftEdge + (availableContentWidth - finalTotalWidth) / 2f

    // --- 3. DRAWING REGISTRATION ---
    withTransform({
        scale(1.0f, 1.3f, pivot = Offset(startX, textY + fLayout1.size.height / 2f))
    }) {
        drawText(fLayout1, topLeft = Offset(startX, textY + verticalShift))
    }

    val dotX = startX + fLayout1.size.width + finalInternalGap
    val dotY =
        (textY + (fLayout1.size.height / 2f)) - (finalSquareSize / 2f) + verticalShift + dotAdjustment

    drawRect(
        color = textColor,
        topLeft = Offset(dotX, dotY),
        size = Size(finalSquareSize, finalSquareSize)
    )

    val secondPartX = dotX + finalSquareSize + finalInternalGap
    withTransform({
        scale(1.0f, 1.3f, pivot = Offset(secondPartX, textY + fLayout2.size.height / 2f))
    }) {
        drawText(fLayout2, topLeft = Offset(secondPartX, textY + verticalShift))
    }
}

private fun DrawScope.drawAjkBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    ajkLogoPainter: androidx.compose.ui.graphics.painter.Painter,
    ajkStripBg: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)

    // --- 0. STRIP AREA CALCULATIONS (25% of Plate) ---
    val borderStrokeWidth = 10f
    val totalStripAreaWidth = w * 0.25f
    val bgWidth = w * 0.06f
    val bgX = totalStripAreaWidth - bgWidth

    // Draw Vertical BG Strip
    translate(left = bgX, top = borderStrokeWidth) {
        with(ajkStripBg) {
            draw(size = Size(bgWidth, h - (borderStrokeWidth * 2)))
        }
    }

    // --- 1. STRIP CONTENT (Logo -> Province -> City) ---
    val availableSpaceForStripContent = totalStripAreaWidth - bgWidth - borderStrokeWidth

    // 1.1 Logo (Top)
    val logoSize = totalStripAreaWidth * 0.6f
    val logoX = borderStrokeWidth + (availableSpaceForStripContent / 2f) - (logoSize / 2f)
    val logoY = h * 0.10f

    translate(left = logoX, top = logoY) {
        with(ajkLogoPainter) {
            draw(size = Size(logoSize, logoSize))
        }
    }

    // 1.2 Province Name (Under Logo)
    val stripTextStyle = TextStyle(
        fontSize = (h * 0.045f).toSp(),
        fontWeight = FontWeight.Bold,
        color = textColor,
        textAlign = TextAlign.Center
    )

    val provText = textMeasurer.measure(config.provinceName.uppercase(), stripTextStyle)
    val provX =
        borderStrokeWidth + (availableSpaceForStripContent / 2f) - (provText.size.width / 2f)
    val provY = logoY + logoSize + (h * 0.02f)
    drawText(provText, topLeft = Offset(provX, provY))

    // 1.3 City Name (Under Province)
    if (config.cityName.isNotEmpty()) {
        val cityText = textMeasurer.measure(config.cityName.uppercase(), stripTextStyle)
        val cityX =
            borderStrokeWidth + (availableSpaceForStripContent / 2f) - (cityText.size.width / 2f)
        val cityY = provY + provText.size.height + (h * 0.01f)
        drawText(cityText, topLeft = Offset(cityX, cityY))
    }

    // --- 2. REGISTRATION AREA (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth
    val horizontalPadding = availableContentWidth * 0.05f

    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    // 2 Lines ke liye height constraint aur font size adjust kiya
    val maxAvailableHeight = h * 0.95f
    var regFontSize = (h * 0.32f).toSp()

    val regStyle = TextStyle(
        fontSize = regFontSize,
        fontFamily = registrationFont,
        color = textColor,
        textAlign = TextAlign.Center,
    )

    // Measure both parts
    var fLayout1 = textMeasurer.measure(firstPart, regStyle)
    var fLayout2 = textMeasurer.measure(secondPart, regStyle)

    do {
        fLayout1 = textMeasurer.measure(firstPart, regStyle.copy(fontSize = regFontSize))
        fLayout2 = textMeasurer.measure(secondPart, regStyle.copy(fontSize = regFontSize))

        val maxW = availableContentWidth - (horizontalPadding * 2)
        val isWidthExceeded = fLayout1.size.width > maxW || fLayout2.size.width > maxW

        if (isWidthExceeded) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while (isWidthExceeded && regFontSize.value > 10f)

    // Vertical Gap ko minimize kiya
    val verticalGap = h * 0.10f
    val regBlockHeight = (fLayout1.size.height + fLayout2.size.height) * 1.3f + verticalGap

    // Center positioning
    val startY = (h / 2f) - (regBlockHeight / 2f)

    // --- 3. DRAWING REGISTRATION (Full Height) ---

    // Line 1
    val line1X = contentLeftEdge + (availableContentWidth / 2f) - (fLayout1.size.width / 2f)
    val line1Y = startY
    withTransform({
        scale(
            1.0f,
            1.3f,
            pivot = Offset(line1X + fLayout1.size.width / 2f, line1Y + fLayout1.size.height / 2f)
        )
    }) {
        drawText(fLayout1, topLeft = Offset(line1X, line1Y))
    }

    // Line 2
    val line2X = contentLeftEdge + (availableContentWidth / 2f) - (fLayout2.size.width / 2f)
    val line2Y = line1Y + (fLayout1.size.height * 1.3f) + verticalGap
    withTransform({
        scale(
            1.0f,
            1.3f,
            pivot = Offset(line2X + fLayout2.size.width / 2f, line2Y + fLayout2.size.height / 2f)
        )
    }) {
        drawText(fLayout2, topLeft = Offset(line2X, line2Y))
    }
}

private fun DrawScope.drawAjkCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    ajkLogoPainter: androidx.compose.ui.graphics.painter.Painter, // Centered Logo
    ajkStripBg: androidx.compose.ui.graphics.painter.Painter    // Full Height Strip BG
) {
    val textColor = Color(config.textColor)

    // --- 0. STRIP AREA CALCULATIONS (25% of Plate) ---
    val borderStrokeWidth = 10f
    val totalStripAreaWidth = w * 0.25f
    val bgWidth = w * 0.06f
    val bgX = totalStripAreaWidth - bgWidth


    translate(left = bgX, top = borderStrokeWidth) {
        with(ajkStripBg) {
            draw(size = Size(bgWidth, h - (borderStrokeWidth * 2)))
        }
    }

    // 2. Draw "ajk_logo" (Centered inside the 25% Strip Area)
    val availableSpaceForLogo = totalStripAreaWidth - bgWidth - borderStrokeWidth
    val logoSize = totalStripAreaWidth * 0.7f // Logo thora chota rakha taake center mein fit ho
    val logoX = borderStrokeWidth + (availableSpaceForLogo / 2f) - (logoSize / 2f)
    val logoY = (h - logoSize) / 2f

    translate(left = logoX, top = logoY) {
        with(ajkLogoPainter) {
            draw(size = Size(logoSize, logoSize))
        }
    }

    // --- Content Boundaries (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth
    val horizontalPadding = availableContentWidth * 0.08f

    // --- 1. PROVINCE & CITY ---
    val topRowCenterY = h * 0.13f
    val provText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.18f).toSp(),
            fontWeight = FontWeight.Medium,
            color = textColor,
            letterSpacing = 1.sp
        )
    )
    drawText(
        provText,
        topLeft = Offset(
            contentLeftEdge + (availableContentWidth / 2) - (provText.size.width / 2),
            topRowCenterY - (provText.size.height / 2)
        )
    )

    val provinceBottomEdge = (topRowCenterY - (provText.size.height / 2)) + provText.size.height

    var cityTopEdge = h
    if (config.cityName.isNotEmpty()) {
        val cityNameText = textMeasurer.measure(
            config.cityName.uppercase(),
            TextStyle(
                fontSize = (h * 0.18f).toSp(),
                fontWeight = FontWeight.Medium,
                color = textColor,
                letterSpacing = 1.sp
            )
        )
        val cityY = (h * 0.87f) - (cityNameText.size.height / 2)
        drawText(
            cityNameText,
            topLeft = Offset(
                contentLeftEdge + (availableContentWidth / 2) - (cityNameText.size.width / 2),
                cityY
            )
        )
        cityTopEdge = cityY
    }

    // --- 2. REGISTRATION WITH SQUARE DOT ---
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val verticalShift = -(h * 0.04f)
    val maxAvailableHeight = (cityTopEdge - provinceBottomEdge) * 0.75f
    val baseFontSize = (h * 0.38f).toSp()

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    val squareSize = layout1.size.height * 0.25f
    val internalGap = w * 0.03f
    val dotAdjustment = h * 0.04f

    val totalComponentWidth =
        layout1.size.width + squareSize + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = availableContentWidth - (horizontalPadding * 2)

    val finalScale = minOf(
        maxAvailableWidth / totalComponentWidth,
        maxAvailableHeight / layout1.size.height
    ).coerceAtMost(1f)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalSquareSize = squareSize * finalScale
    val finalInternalGap = internalGap * finalScale

    val fLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    val gapCenterY = provinceBottomEdge + ((cityTopEdge - provinceBottomEdge) / 2f)
    val textY = gapCenterY - (fLayout1.size.height / 2f)

    val finalTotalWidth =
        fLayout1.size.width + finalSquareSize + (finalInternalGap * 2) + fLayout2.size.width
    val startX = contentLeftEdge + (availableContentWidth - finalTotalWidth) / 2f

    // --- 3. DRAWING REGISTRATION ---
    withTransform({
        scale(1.0f, 1.3f, pivot = Offset(startX, textY + fLayout1.size.height / 2f))
    }) {
        drawText(fLayout1, topLeft = Offset(startX, textY + verticalShift))
    }

    val dotX = startX + fLayout1.size.width + finalInternalGap
    val dotY =
        (textY + (fLayout1.size.height / 2f)) - (finalSquareSize / 2f) + verticalShift + dotAdjustment

    drawRect(
        color = textColor,
        topLeft = Offset(dotX, dotY),
        size = Size(finalSquareSize, finalSquareSize)
    )

    val secondPartX = dotX + finalSquareSize + finalInternalGap
    withTransform({
        scale(1.0f, 1.3f, pivot = Offset(secondPartX, textY + fLayout2.size.height / 2f))
    }) {
        drawText(fLayout2, topLeft = Offset(secondPartX, textY + verticalShift))
    }
}

//GB
private fun DrawScope.drawGbBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)

    // --- 0. PURPLE STRIP BG (Custom Corners & Border Safety) ---
    val borderStrokeWidth = 10f // Aapke border ki thickness
    val cornerRadiusValue = 25f // Plate ke corners ka radius
    val stripWidth = w * 0.22f
    val purpleColor = Color(0xFF2129D1)

    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(stripWidth - borderStrokeWidth, h - (borderStrokeWidth * 2))
                ),
                // Sirf Left corners round honge
                topLeft = CornerRadius(cornerRadiusValue, cornerRadiusValue),
                bottomLeft = CornerRadius(cornerRadiusValue, cornerRadiusValue),
                // Right side bilkul seedhi (sharp) rahegi
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }

    drawPath(path = stripPath, color = purpleColor)

    // Reference point for items inside strip
    val stripCenterX = (borderStrokeWidth + stripWidth) / 2f

    // --- 1. LOGO (Inside Strip) ---
    val logoSize = h * 0.40f
    val finalWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    translate(left = stripCenterX - (finalWidth / 2f), top = h * 0.15f) {
        with(logoPainter) {
            draw(size = Size(finalWidth, logoSize))
        }
    }

    // --- 2. PROVINCE & CITY (Inside Strip) ---
    val stripContentColor = Color.White

    val provText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.15f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = stripContentColor
        )
    )
    drawText(provText, topLeft = Offset(stripCenterX - (provText.size.width / 2f), h * 0.60f))


    // --- 3. REGISTRATION ---
    val contentLeftEdge = stripWidth + borderStrokeWidth
    val availableContentWidth = w - contentLeftEdge - borderStrokeWidth
    val maxRegWidth = availableContentWidth * 0.9f

    var regFontSize = (h * 0.56f).toSp()
    val customRegString = buildAnnotatedString { append(config.registrationNumber.uppercase()) }

    var regLayoutResult = textMeasurer.measure(
        customRegString,
        TextStyle(fontSize = regFontSize, fontFamily = registrationFont, color = textColor)
    )

    if (regLayoutResult.size.width > maxRegWidth) {
        regFontSize = (regFontSize.value * (maxRegWidth / regLayoutResult.size.width)).sp
        regLayoutResult = textMeasurer.measure(
            customRegString,
            TextStyle(fontSize = regFontSize, fontFamily = registrationFont, color = textColor)
        )
    }

    val regX = contentLeftEdge + (availableContentWidth / 2f) - (regLayoutResult.size.width / 2f)
    val regY = (h / 2f) - (regLayoutResult.size.height / 2f) - h * 0.03f

    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = 1.3f,
            pivot = Offset(regX + regLayoutResult.size.width / 2f, h / 2f)
        )
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawGbBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val borderStrokeWidth = 10f // Border safety offset
    val cornerRadiusValue = 25f // Plate corner radius

    // --- 0. PURPLE STRIP BG (Left Side) ---
    val stripWidth = w * 0.22f
    val purpleColor = Color(0xFF2129D1)

    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(stripWidth - borderStrokeWidth, h - (borderStrokeWidth * 2))
                ),
                topLeft = CornerRadius(cornerRadiusValue, cornerRadiusValue),
                bottomLeft = CornerRadius(cornerRadiusValue, cornerRadiusValue),
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }
    drawPath(path = stripPath, color = purpleColor)

    // Strip center for alignment
    val stripCenterX = (borderStrokeWidth + stripWidth) / 2f

    // --- 1. LOGO & PROVINCE BLOCK (Inside Strip) ---
    val logoSize = h * 0.20f
    val logoWidth = logoSize * (logoPainter.intrinsicSize.width / logoPainter.intrinsicSize.height)

    // Logo (White area items moving to strip)
    translate(left = stripCenterX - (logoWidth / 2f), top = h * 0.15f) {
        with(logoPainter) {
            draw(size = Size(logoWidth, logoSize))
        }
    }

    val stripContentColor = Color.White

    // Province Text
    val provText = textMeasurer.measure(
        text = config.provinceName.uppercase(), style = TextStyle(
            fontSize = (h * 0.12f).toSp(),
            fontWeight = FontWeight.ExtraBold,
            color = stripContentColor
        )
    )
    drawText(provText, topLeft = Offset(stripCenterX - (provText.size.width / 2f), h * 0.40f))


    // --- 2. REGISTRATION (Remaining Area Center) ---
    val contentLeftEdge = stripWidth + borderStrokeWidth
    val availableContentWidth = w - contentLeftEdge - borderStrokeWidth

    val formattedReg = config.registrationNumber.replace("-", " ")
    val maxRegWidth = availableContentWidth * 0.90f

    var regFontSize = (h * 0.32f).toSp()
    var regLayoutResult: TextLayoutResult

    do {
        regLayoutResult = textMeasurer.measure(
            text = formattedReg, style = TextStyle(
                fontSize = regFontSize,
                fontFamily = registrationFont,
                color = textColor,
                textAlign = TextAlign.Center,
                platformStyle = PlatePlatformTextStyle,
                lineHeightStyle = LineHeightStyle(
                    alignment = LineHeightStyle.Alignment.Center, trim = LineHeightStyle.Trim.Both
                ),
                lineHeight = regFontSize * 1.05f
            ), constraints = Constraints(maxWidth = maxRegWidth.toInt()), softWrap = true
        )

        if (regLayoutResult.lineCount > 2) {
            regFontSize = (regFontSize.value - 1f).sp
        }
    } while (regLayoutResult.lineCount > 2 && regFontSize.value > 10f)

    val visualAdjustment = h * 0.05f
    // Center in the white area
    val regX = contentLeftEdge + (availableContentWidth / 2f) - (regLayoutResult.size.width / 2f)
    val regY = (h - regLayoutResult.size.height) / 2f - visualAdjustment

    val scaleY = 1.3f
    withTransform({
        scale(
            scaleX = 1.0f,
            scaleY = scaleY,
            pivot = Offset(regX + regLayoutResult.size.width / 2f, h / 2f)
        )
    }) {
        drawText(regLayoutResult, topLeft = Offset(regX, regY))
    }
}

private fun DrawScope.drawGbCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val borderStrokeWidth = 10f

    // --- 0. STRIP AREA CALCULATIONS (25% of Plate) ---
    val totalStripAreaWidth = w * 0.25f
    val leftRadius = 36f

    // 1. Draw Custom Rounded Blue Strip Background (Asymmetric Corners)
    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(
                        totalStripAreaWidth - borderStrokeWidth,
                        h - (borderStrokeWidth * 2)
                    )
                ),
                topLeft = CornerRadius(leftRadius, leftRadius),
                bottomLeft = CornerRadius(leftRadius, leftRadius),
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }
    drawPath(path = stripPath, color = Color(0xFF2129D1))

    // --- LOGO & PROVINCE PLACEMENT (Vertically Top) ---
    val stripContentWidth = totalStripAreaWidth - borderStrokeWidth

    // A. Logo Placement (Top padding adjusted to 12%)
    val logoSize = stripContentWidth * 0.7f
    val logoX = borderStrokeWidth + (stripContentWidth - logoSize) / 2f
    val logoY = borderStrokeWidth + (h * 0.12f)

    translate(left = logoX, top = logoY) {
        with(logoPainter) {
            draw(size = Size(logoSize, logoSize))
        }
    }

    // B. Province Name Placement (Logo ke theek niche)
    val provinceText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.16f).toSp(),
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    )

    val provX = borderStrokeWidth + (stripContentWidth - provinceText.size.width) / 2f
    val provY = logoY + logoSize + (h * 0.03f)

    drawText(provinceText, topLeft = Offset(provX, provY))

    // --- Content Boundaries (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth
    val horizontalPadding = availableContentWidth * 0.1f

    // --- 2. REGISTRATION WITH DASH ---
    val verticalOffset = -(h * 0.03f)
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val baseFontSize = (h * 0.42f).toSp()
    val dotAdjustment = h * 0.02f

    val layout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val layout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    // Dash Dimensions (Dot se Dash banaya)
    val dashWidth = (layout1.size.height * 0.22f) * 1.7f
    val dashHeight = (layout1.size.height * 0.22f) * 0.6f
    val internalGap = w * 0.03f

    val totalComponentWidth =
        layout1.size.width + dashWidth + (internalGap * 2) + layout2.size.width
    val maxAvailableWidth = availableContentWidth - (horizontalPadding * 2)
    val finalScale = (maxAvailableWidth / totalComponentWidth).coerceAtMost(1f)

    val finalFontSize = (baseFontSize.value * finalScale).sp
    val finalDashWidth = dashWidth * finalScale
    val finalDashHeight = dashHeight * finalScale
    val finalInternalGap = internalGap * finalScale

    val fLayout1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fLayout2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    val textY = (h / 2f) - (fLayout1.size.height / 2f) + verticalOffset
    val finalTotalWidth =
        fLayout1.size.width + finalDashWidth + (finalInternalGap * 2) + fLayout2.size.width
    val startX = contentLeftEdge + (availableContentWidth - finalTotalWidth) / 2f

    // --- 3. DRAWING REGISTRATION ---
    // Part 1
    withTransform({
        scale(1.0f, 1.2f, pivot = Offset(startX, textY + fLayout1.size.height / 2f))
    }) {
        drawText(fLayout1, topLeft = Offset(startX, textY))
    }

    // Dash (Replacement for Dot)
    val dashX = startX + fLayout1.size.width + finalInternalGap
    val dashY = (h / 2f) - (finalDashHeight / 2f) + dotAdjustment

    drawRect(
        color = textColor,
        topLeft = Offset(dashX, dashY),
        size = Size(finalDashWidth, finalDashHeight)
    )

    // Part 2
    val secondPartX = dashX + finalDashWidth + finalInternalGap
    withTransform({
        scale(1.0f, 1.2f, pivot = Offset(secondPartX, textY + fLayout2.size.height / 2f))
    }) {
        drawText(fLayout2, topLeft = Offset(secondPartX, textY))
    }
}

//ISLAMABAD

private fun DrawScope.drawIslamabadBikeFront(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    stripLogoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val borderStrokeWidth = 10f

    // --- 0. STRIP AREA ---
    val totalStripAreaWidth = w * 0.25f
    val leftRadius = 36f

    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(
                        totalStripAreaWidth - borderStrokeWidth,
                        h - (borderStrokeWidth * 2)
                    )
                ),
                topLeft = CornerRadius(leftRadius, leftRadius),
                bottomLeft = CornerRadius(leftRadius, leftRadius),
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }
    drawPath(path = stripPath, color = Color(0xFF0316C7))

    // --- STRIP CONTENT ---
    val stripContentWidth = totalStripAreaWidth - borderStrokeWidth

    // 1. Top Logo (Islamabad Logo) - Reduced from 0.5f to 0.35f
    val logoSize = stripContentWidth * 0.35f
    val logoX = borderStrokeWidth + (stripContentWidth - logoSize) / 2f
    val logoY = borderStrokeWidth + (h * 0.12f) // Thora neeche move kiya

    translate(left = logoX, top = logoY) {
        with(logoPainter) { draw(size = Size(logoSize, logoSize)) }
    }

    // 2. Bottom Province Text
    val provinceText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.07f).toSp(),
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    )
    val provX = borderStrokeWidth + (stripContentWidth - provinceText.size.width) / 2f
    val provY = h - borderStrokeWidth - provinceText.size.height - (h * 0.03f)
    drawText(provinceText, topLeft = Offset(provX, provY))

    // 3. Middle Strip Logo - Reduced from 0.8f to 0.55f for a cleaner look
    val middleLogoSize = stripContentWidth * 0.55f
    val middleLogoX = borderStrokeWidth + (stripContentWidth - middleLogoSize) / 2f
    val availableSpaceTop = logoY + logoSize
    val middleLogoY = availableSpaceTop + (provY - availableSpaceTop) / 2f - (middleLogoSize / 2f)

    translate(left = middleLogoX, top = middleLogoY) {
        with(stripLogoPainter) { draw(size = Size(middleLogoSize, middleLogoSize)) }
    }

    // --- REGISTRATION AREA (FULL HEIGHT CENTERED) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth
    val registrationAreaHeight = h - (borderStrokeWidth * 2)

    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val baseFontSize = (h * 0.55f).toSp()

    val l1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )
    val dashWidth = (l1.size.height * 0.22f) * 1.2f
    val dashHeight = (l1.size.height * 0.22f) * 0.6f
    val internalGap = w * 0.03f

    val totalCompWidth = l1.size.width + dashWidth + (internalGap * 2) +
            textMeasurer.measure(
                secondPart,
                TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
            ).size.width

    val scale =
        ((availableContentWidth - (availableContentWidth * 0.15f)) / totalCompWidth).coerceAtMost(1f)

    val finalFontSize = (baseFontSize.value * scale).sp
    val fDashW = dashWidth * scale
    val fDashH = dashHeight * scale
    val fGap = internalGap * scale

    val fL1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fL2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    val textY =
        borderStrokeWidth + (registrationAreaHeight / 2f) - (fL1.size.height / 2f) - h * 0.05f
    val finalTotalWidth = fL1.size.width + fDashW + (fGap * 2) + fL2.size.width
    val startX = contentLeftEdge + (availableContentWidth - finalTotalWidth) / 2f

    withTransform({
        scale(1.0f, 1.4f, pivot = Offset(startX, textY + fL1.size.height / 2f))
    }) {
        drawText(fL1, topLeft = Offset(startX, textY))
    }

    val dashX = startX + fL1.size.width + fGap
    val dashY = textY + (fL1.size.height / 2f) - (fDashH / 2f)
    drawRect(color = textColor, topLeft = Offset(dashX, dashY), size = Size(fDashW, fDashH))

    val secondPartX = dashX + fDashW + fGap
    withTransform({
        scale(1.0f, 1.4f, pivot = Offset(secondPartX, textY + fL2.size.height / 2f))
    }) {
        drawText(fL2, topLeft = Offset(secondPartX, textY))
    }
}

private fun DrawScope.drawIslamabadBikeRear(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    stripLogoPainter: androidx.compose.ui.graphics.painter.Painter
) {
    val textColor = Color(config.textColor)
    val borderStrokeWidth = 10f

    // --- 0. STRIP AREA ---
    val totalStripAreaWidth = w * 0.25f
    val leftRadius = 36f

    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(
                        totalStripAreaWidth - borderStrokeWidth,
                        (h - (borderStrokeWidth * 2)) * 0.80f
                    )

                ),
                topLeft = CornerRadius(leftRadius, leftRadius),
                bottomLeft = CornerRadius(0f, 0f),
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }
    drawPath(path = stripPath, color = Color(0xFF0316C7))

    // --- STRIP CONTENT (LOGO TOP, STRIP_LOGO MIDDLE, PROVINCE BOTTOM) ---
    val stripContentWidth = totalStripAreaWidth - borderStrokeWidth

    // 1. Top Logo (Islamabad Logo)
    val logoSize = stripContentWidth * 0.5f
    val logoX = borderStrokeWidth + (stripContentWidth - logoSize) / 2f
    val logoY = borderStrokeWidth + (h * 0.10f) + (h * 0.10f)

    translate(left = logoX, top = logoY) {
        with(logoPainter) { draw(size = Size(logoSize, logoSize)) }
    }

    // 2. Bottom Province Text
    val provinceText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = (h * 0.04f).toSp(),
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    )
    val provX = borderStrokeWidth + (stripContentWidth - provinceText.size.width) / 2f
    val stripBottom = borderStrokeWidth + (h - (borderStrokeWidth * 2)) * 0.80f
    val provY = stripBottom - provinceText.size.height - (h * 0.02f)
    drawText(provinceText, topLeft = Offset(provX, provY))
    val middleLogoSize = stripContentWidth * 0.8f // Iska size thora bada rakha hai
    val middleLogoX = borderStrokeWidth + (stripContentWidth - middleLogoSize) / 2f
    val availableSpaceTop = logoY + logoSize
    val middleLogoY = availableSpaceTop + (provY - availableSpaceTop) / 2f - (middleLogoSize / 2f)

    translate(left = middleLogoX, top = middleLogoY) {
        with(stripLogoPainter) { draw(size = Size(middleLogoSize, middleLogoSize)) }
    }

    // --- Content Boundaries (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth

    // --- CITY NAME ---
    val cityText = textMeasurer.measure(
        config.cityName.uppercase(),
        TextStyle(
            fontSize = (h * 0.18f).toSp(),
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 6.sp
        ),
        constraints = Constraints(maxWidth = availableContentWidth.toInt())
    )
    val cityY = h - borderStrokeWidth - cityText.size.height - (h * 0.01f)
    val cityX = (w / 2f) - (cityText.size.width / 2f)
    drawText(cityText, topLeft = Offset(cityX, cityY))

    // --- REGISTRATION AREA ---
    val registrationAreaHeight = cityY - borderStrokeWidth
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val baseFontSize = (h * 0.36f).toSp()

    val fL1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fL2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont, color = textColor)
    )

// Row 1 - firstPart vertically centered in top half
    val row1CenterY = borderStrokeWidth + (registrationAreaHeight / 4f)
    val row1Y = row1CenterY - (fL1.size.height / 2f)
    val row1X = contentLeftEdge + (availableContentWidth / 2f) - (fL1.size.width / 2f)

// Row 2 - secondPart vertically centered in bottom half
    val row2CenterY = borderStrokeWidth + (registrationAreaHeight * 3f / 4f)
    val row2Y = row2CenterY - (fL2.size.height / 2f)
    val row2X = contentLeftEdge + (availableContentWidth / 2f) - (fL2.size.width / 2f)

// Draw Row 1
    withTransform({
        scale(1.0f, 1.0f, pivot = Offset(row1X, row1Y + fL1.size.height / 2f))
    }) {
        drawText(fL1, topLeft = Offset(row1X, row1Y))
    }

// Draw Row 2
    withTransform({
        scale(1.0f, 1.0f, pivot = Offset(row2X, row2Y + fL2.size.height / 2f))
    }) {
        drawText(fL2, topLeft = Offset(row2X, row2Y))
    }
}

private fun DrawScope.drawIslamabadCarPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily,
    logoPainter: androidx.compose.ui.graphics.painter.Painter,
    stripLogoPainter: androidx.compose.ui.graphics.painter.Painter // Naya Logo parameter
) {
    val textColor = Color(config.textColor)
    val borderStrokeWidth = 10f

    // --- 0. STRIP AREA ---
    val totalStripAreaWidth = w * 0.25f
    val leftRadius = 36f

    val stripPath = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(borderStrokeWidth, borderStrokeWidth),
                    size = Size(
                        totalStripAreaWidth - borderStrokeWidth,
                        h - (borderStrokeWidth * 2)
                    )
                ),
                topLeft = CornerRadius(leftRadius, leftRadius),
                bottomLeft = CornerRadius(leftRadius, leftRadius),
                topRight = CornerRadius(0f, 0f),
                bottomRight = CornerRadius(0f, 0f)
            )
        )
    }
    drawPath(path = stripPath, color = Color(0xFF0316C7))

    // --- STRIP CONTENT (LOGO TOP, STRIP_LOGO MIDDLE, PROVINCE BOTTOM) ---
    val stripContentWidth = totalStripAreaWidth - borderStrokeWidth

    // 1. Top Logo (Islamabad Logo)
    val logoSize = stripContentWidth * 0.5f
    val logoX = borderStrokeWidth + (stripContentWidth - logoSize) / 2f
    val logoY = borderStrokeWidth + (h * 0.10f)

    translate(left = logoX, top = logoY) {
        with(logoPainter) { draw(size = Size(logoSize, logoSize)) }
    }

    // 2. Bottom Province Text
    val provinceText = textMeasurer.measure(
        config.provinceName.uppercase(),
        TextStyle(
            fontSize = if (config.vehicleType == VehicleType.RICKSHAW) (h * 0.05f).toSp() else (h * 0.08f).toSp(),
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    )
    val provX = borderStrokeWidth + (stripContentWidth - provinceText.size.width) / 2f
    val provY = h - borderStrokeWidth - provinceText.size.height - (h * 0.05f)
    drawText(provinceText, topLeft = Offset(provX, provY))

    // 3. Middle Strip Logo (Islamabad Strip Logo)
    // Ye logo ooper wale logo aur neeche wale text ke bilkul darmiyan mein fit hoga
    val middleLogoSize = stripContentWidth * 0.8f // Iska size thora bada rakha hai
    val middleLogoX = borderStrokeWidth + (stripContentWidth - middleLogoSize) / 2f

    // Center point between logoY + logoSize and provY
    val availableSpaceTop = logoY + logoSize
    val middleLogoY = availableSpaceTop + (provY - availableSpaceTop) / 2f - (middleLogoSize / 2f)

    translate(left = middleLogoX, top = middleLogoY) {
        with(stripLogoPainter) { draw(size = Size(middleLogoSize, middleLogoSize)) }
    }

    // --- Content Boundaries (Remaining 75%) ---
    val contentLeftEdge = totalStripAreaWidth
    val availableContentWidth = w - totalStripAreaWidth

    // --- CITY NAME ---
    val cityText = textMeasurer.measure(
        config.cityName.uppercase(),
        TextStyle(
            fontSize = if (config.vehicleType == VehicleType.RICKSHAW) (h * 0.10f).toSp() else (h * 0.14f).toSp(),
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 2.sp
        )
    )
    val cityY = h - borderStrokeWidth - cityText.size.height - (h * 0.02f)
    val cityX = contentLeftEdge + (availableContentWidth / 2f) - (cityText.size.width / 2f)
    drawText(cityText, topLeft = Offset(cityX, cityY))

    // --- REGISTRATION AREA ---
    val registrationAreaHeight = cityY - borderStrokeWidth
    val parts = config.registrationNumber.trim().split(" ")
    val firstPart = parts.getOrNull(0) ?: ""
    val secondPart = parts.getOrNull(1) ?: ""

    val baseFontSize = (h * 0.44f).toSp()
    val l1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
    )

    val dashWidth = (l1.size.height * 0.22f) * 1.2f
    val dashHeight = (l1.size.height * 0.22f) * 0.6f
    val internalGap = w * 0.03f

    val totalCompWidth = l1.size.width + dashWidth + (internalGap * 2) +
            textMeasurer.measure(
                secondPart,
                TextStyle(fontSize = baseFontSize, fontFamily = registrationFont)
            ).size.width

    val scale =
        ((availableContentWidth - (availableContentWidth * 0.12f)) / totalCompWidth).coerceAtMost(1f)

    val finalFontSize = (baseFontSize.value * scale).sp
    val fDashW = dashWidth * scale
    val fDashH = dashHeight * scale
    val fGap = internalGap * scale

    val fL1 = textMeasurer.measure(
        firstPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )
    val fL2 = textMeasurer.measure(
        secondPart,
        TextStyle(fontSize = finalFontSize, fontFamily = registrationFont, color = textColor)
    )

    val textY = borderStrokeWidth + (registrationAreaHeight / 2f) - (fL1.size.height / 2f)
    val finalTotalWidth = fL1.size.width + fDashW + (fGap * 2) + fL2.size.width
    val startX = contentLeftEdge + (availableContentWidth - finalTotalWidth) / 2f

    // --- DRAWING REGISTRATION ---
    withTransform({
        scale(1.0f, 1.4f, pivot = Offset(startX, textY + fL1.size.height / 2f))
    }) {
        drawText(fL1, topLeft = Offset(startX, textY))
    }

    val dashX = startX + fL1.size.width + fGap
    val dashVerticalOffset = h * 0.03f
    val dashY = textY + (fL1.size.height / 2f) - (fDashH / 2f) + dashVerticalOffset
    drawRect(color = textColor, topLeft = Offset(dashX, dashY), size = Size(fDashW, fDashH))

    val secondPartX = dashX + fDashW + fGap
    withTransform({
        scale(1.0f, 1.4f, pivot = Offset(secondPartX, textY + fL2.size.height / 2f))
    }) {
        drawText(fL2, topLeft = Offset(secondPartX, textY))
    }
}

private fun DrawScope.drawDiplomaticPlate(
    config: PlateConfig,
    textMeasurer: TextMeasurer,
    w: Float,
    h: Float,
    registrationFont: FontFamily
) {
    val textColor = Color(config.textColor)



    // --- 2. Registration Number (Big & Centered) ---
    val parts = config.registrationNumber.trim().split(" ")
    val fullText = parts.joinToString("  ") // Space barha di taake clear nazar aaye

    val baseFontSize = (h * 0.45f).toSp()
    val regText = textMeasurer.measure(
        fullText,
        TextStyle(fontSize = baseFontSize, fontFamily = registrationFont, color = textColor)
    )

    // Scaling to fit width if necessary
    val scale = ((w * 0.85f) / regText.size.width).coerceAtMost(1f)
    val finalRegText = textMeasurer.measure(
        fullText,
        TextStyle(
            fontSize = (baseFontSize.value * scale).sp,
            fontFamily = registrationFont,
            color = textColor
        )
    )

    val regX = (w - finalRegText.size.width) / 2f
    val regY =
        (h - finalRegText.size.height) / 2f - h * 0.03f

    withTransform({
        scale(
            1.0f,
            1.4f,
            pivot = Offset(
                regX + finalRegText.size.width / 2f,
                regY + finalRegText.size.height / 2f
            )
        )
    }) {
        drawText(finalRegText, topLeft = Offset(regX, regY))
    }
}