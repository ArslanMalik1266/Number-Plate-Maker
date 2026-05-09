package com.webscare.numberplatemaker.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.StripOrientation
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.ui.theme.PlateTypography
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.punjab_logo
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlateCanvas(plate: PlateModel, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val plateFont = PlateTypography.getPlateFont()

    val baseWidth = plate.dimensions.width
    val baseHeight = plate.dimensions.height
    val aspectRatio = baseWidth / baseHeight

    val punjabLogoPainter = painterResource(Res.drawable.punjab_logo)

    BoxWithConstraints(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        val maxWidth = maxWidth
        val calculatedHeight = maxWidth / aspectRatio

        Canvas(modifier = Modifier.width(maxWidth).height(calculatedHeight)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val borderInset = 0.dp.toPx()
            val innerSize = size.copy(
                width = size.width - (borderInset * 2),
                height = size.height - (borderInset * 2)
            )

            // --- 1. COLORS SETUP ---

            val textColor = Color(plate.textColor)


            // --- 2. ORIENTATION SE STRIP & TEXT AREA DECIDE KARO ---
            val stripOrientation = plate.stripOrientation

            // Strip size: 20% width (vertical) ya 18% height (horizontal)
            val stripSize = when (stripOrientation) {
                StripOrientation.VERTICAL_LEFT   -> canvasWidth * 0.20f
                StripOrientation.HORIZONTAL_TOP  -> canvasHeight * 0.20f
                StripOrientation.NONE            -> 0f
            }

            // Text area: strip ke baad ka remaining space
            val textAreaWidth = when (stripOrientation) {
                StripOrientation.VERTICAL_LEFT  -> canvasWidth - stripSize
                StripOrientation.HORIZONTAL_TOP -> canvasWidth
                StripOrientation.NONE           -> canvasWidth
            }
            val textAreaHeight = when (stripOrientation) {
                StripOrientation.HORIZONTAL_TOP -> canvasHeight - stripSize
                StripOrientation.VERTICAL_LEFT  -> canvasHeight
                StripOrientation.NONE           -> canvasHeight
            }

            // Text drawing ka start point (strip ke baad)
            val textStartX = when (stripOrientation) {
                StripOrientation.VERTICAL_LEFT  -> stripSize
                else                            -> 0f
            }
            val textStartY = when (stripOrientation) {
                StripOrientation.HORIZONTAL_TOP -> stripSize
                else                            -> 0f
            }

            // --- 3. DRAW BACKGROUND ---
            drawRoundRect(
                color = Color.White,
                size = size,
                cornerRadius = CornerRadius(16.dp.toPx())
            )

            // --- 4. DRAW STRIP (Orientation ke hisaab se) ---
            when (stripOrientation) {

                StripOrientation.VERTICAL_LEFT -> {
                    // Left side rounded, right side square
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(borderInset, borderInset),
                        size = innerSize.copy(width = stripSize),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(borderInset + stripSize / 2, borderInset),
                        size = innerSize.copy(width = stripSize / 2)
                    )

                    // --- LOGO SIZE (aspect ratio preserve) ---
                    val intrinsicSize = punjabLogoPainter.intrinsicSize
                    val logoWidth = stripSize * 0.75f
                    val logoHeight = if (intrinsicSize.width > 0f && intrinsicSize.height > 0f) {
                        logoWidth * (intrinsicSize.height / intrinsicSize.width)
                    } else {
                        logoWidth
                    }
                    val logoX = borderInset + (stripSize - logoWidth) / 2

                    // --- TEXT MEASURE (logoY se pehle) ---
                    val provinceText = plate.province.name
                    val textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = (stripSize * 0.20f).toSp(),
                        fontWeight = FontWeight.Bold
                    )
                    val textLayout = textMeasurer.measure(provinceText, textStyle)

                    // --- LOGO Y: logo+text mila k center ---
                    val gap = 4.dp.toPx()
                    val totalContentHeight = logoHeight + gap + textLayout.size.height
                    val logoY = if (plate.side == PlateSide.FRONT) {
                        (canvasHeight - totalContentHeight) / 2f  // FRONT: logo+text combined center
                    } else {
                        canvasHeight * 0.20f                       // REAR: top se 20% neeche
                    }

                    // --- DRAW LOGO ---
                    translate(left = logoX, top = logoY) {
                        with(punjabLogoPainter) {
                            draw(size = Size(logoWidth, logoHeight))
                        }
                    }

                    // --- DRAW TEXT ---
                    drawText(
                        textLayoutResult = textLayout,
                        topLeft = Offset(
                            x = borderInset + (stripSize - textLayout.size.width) / 2,
                            y = logoY + logoHeight + gap
                        )
                    )
                }



                StripOrientation.HORIZONTAL_TOP -> {
                    // Top rounded, bottom square
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(borderInset, borderInset),
                        size = innerSize.copy(height = stripSize),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                    // Bottom half square karo
                    drawRect(
                        color = Color.White,
                        topLeft = Offset(borderInset, borderInset + stripSize / 2),
                        size = innerSize.copy(height = stripSize / 2)
                    )
                }

                StripOrientation.NONE -> { /* No strip */ }
            }

            // --- 5. INNER BLACK BORDER ---
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.9f),
                topLeft = Offset(borderInset, borderInset),
                size = innerSize,
                style = Stroke(width = 3.dp.toPx()),
                cornerRadius = CornerRadius(16.dp.toPx())
            )

            // --- 6. SMART TEXT LOGIC ---
            val isSquared = aspectRatio < 1.5f
            val parts = plate.registrationNumber.uppercase().split(" ", "-")

            if (isSquared && parts.size >= 2) {
                // --- SQUARED PLATE: 2 LINES ---
                val line1 = parts[0]
                val line2 = parts.drop(1).joinToString(" ")

                var fontSize = (textAreaHeight * 0.42f).toSp()

                var layout1 = textMeasurer.measure(line1, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))
                var layout2 = textMeasurer.measure(line2, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))

                val maxWidthAllowed = textAreaWidth - 15.dp.toPx()
                val currentMaxW = maxOf(layout1.size.width, layout2.size.width)

                if (currentMaxW > maxWidthAllowed) {
                    fontSize = (fontSize.value * (maxWidthAllowed / currentMaxW)).sp
                    layout1 = textMeasurer.measure(line1, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))
                    layout2 = textMeasurer.measure(line2, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))
                }

                drawText(
                    textLayoutResult = layout1,
                    topLeft = Offset(
                        x = textStartX + (textAreaWidth - layout1.size.width) / 2,
                        y = textStartY + textAreaHeight * 0.08f
                    )
                )
                drawText(
                    textLayoutResult = layout2,
                    topLeft = Offset(
                        x = textStartX + (textAreaWidth - layout2.size.width) / 2,
                        y = textStartY + textAreaHeight * 0.48f
                    )
                )

            } else {
                // --- LONG PLATE: 1 LINE ---
                val fullText = parts.joinToString("  ")
                var fontSize = (textAreaHeight * 0.85f).toSp()

                var regLayout = textMeasurer.measure(fullText, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))

                val maxWidthAllowed = textAreaWidth - 20.dp.toPx()
                if (regLayout.size.width > maxWidthAllowed) {
                    fontSize = (fontSize.value * (maxWidthAllowed / regLayout.size.width)).sp
                    regLayout = textMeasurer.measure(fullText, TextStyle(fontSize = fontSize, fontFamily = plateFont, color = textColor))
                }

                drawText(
                    textLayoutResult = regLayout,
                    topLeft = Offset(
                        x = textStartX + (textAreaWidth - regLayout.size.width) / 2,
                        y = textStartY + (textAreaHeight - regLayout.size.height) / 2
                    )
                )
            }
        }
    }
}