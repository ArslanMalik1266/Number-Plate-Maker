package com.webscare.numberplatemaker.ui.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.VehicleType

@Composable
fun PlateCanvas(plate: PlateModel, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()

    // Repository wali dimensions
    val baseWidth = plate.dimensions.width
    val baseHeight = plate.dimensions.height
    val aspectRatio = baseWidth / baseHeight

    // Hum plate ko screen ki width ke mutabiq adjust karenge
    BoxWithConstraints(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        val maxWidth = maxWidth
        val calculatedHeight = maxWidth / aspectRatio

        Canvas(
            modifier = Modifier
                .width(maxWidth)
                .height(calculatedHeight)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // 1. Dynamic Colors from Repository Logic
            val isGov = plate.vehicleType == VehicleType.GOVERNMENT
            val isCommercial = plate.vehicleType.name.contains("COMMERCIAL")

            val bgColor = when {
                isGov -> Color(0xFF01411C)
                isCommercial -> Color(0xFFFFD54F) // Yellow for Commercial
                else -> Color.White
            }

            val textColor = if (isGov) Color.White else Color.Black

            // 2. Draw Plate Background
            drawRoundRect(
                color = bgColor,
                size = size,
                cornerRadius = CornerRadius(8.dp.toPx())
            )

            // 3. Border
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.8f),
                size = size,
                style = Stroke(width = 2.dp.toPx()),
                cornerRadius = CornerRadius(8.dp.toPx())
            )

            // --- SCALABLE TEXT DRAWING ---
            // Senior Tip: Use canvasHeight percentage for font size so it scales on all screens
            val fontSizeLarge = (canvasHeight * 0.4f).toSp()
            val fontSizeSmall = (canvasHeight * 0.15f).toSp()

            // 4. Province Text
            val provinceLayout = textMeasurer.measure(
                text = plate.province.name,
                style = TextStyle(
                    fontSize = fontSizeSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    letterSpacing = 4.sp
                )
            )

            drawText(
                textLayoutResult = provinceLayout,
                topLeft = Offset(
                    x = (canvasWidth - provinceLayout.size.width) / 2,
                    y = canvasHeight * 0.1f
                )
            )

            // 5. Registration Number
            val regLayout = textMeasurer.measure(
                text = plate.registrationNumber.uppercase(),
                style = TextStyle(
                    fontSize = fontSizeLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    letterSpacing = 2.sp
                )
            )

            drawText(
                textLayoutResult = regLayout,
                topLeft = Offset(
                    x = (canvasWidth - regLayout.size.width) / 2,
                    y = (canvasHeight / 2) - (regLayout.size.height / 2) + (canvasHeight * 0.1f)
                )
            )
        }
    }
}