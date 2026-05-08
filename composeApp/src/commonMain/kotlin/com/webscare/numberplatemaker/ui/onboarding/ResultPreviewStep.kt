package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas

@Composable
fun ResultPreviewStep(
    plate: PlateModel?,
    viewModel: PlateViewModel,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp) // Padding thori kam ki hai
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- 1. Compact Header ---
        Text(
            text = "Plate Previews",
            style = MaterialTheme.typography.titleLarge.copy( // headline se titleLarge par shift (Chota size)
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        )
        Text(
            text = "Review your final design",
            style = MaterialTheme.typography.bodySmall, // Mazeed chota subtitle
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (plate != null) {
            // --- 2. Side by Side or Compact Previews ---
            PreviewLabel("FRONT PLATE")
            val frontPlate = viewModel.getPlateForSide(PlateSide.FRONT)
            frontPlate?.let { PlateCard(plate = it) }

            Spacer(modifier = Modifier.height(20.dp))

            PreviewLabel("REAR PLATE")
            val backPlate = viewModel.getPlateForSide(PlateSide.BACK)
            backPlate?.let { PlateCard(plate = it) }

            Spacer(modifier = Modifier.height(28.dp))

            // --- 3. Configuration Details (Compact) ---
            DetailBox(plate)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. Actions (Fixed at bottom feel) ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f).height(48.dp), // Height 56 se 48 kar di
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Reset", fontSize = 14.sp)
            }
            Button(
                onClick = { /* Save Logic */ },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save Plate", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PreviewLabel(label: String) {
    Text(
        text = label,
        modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp, start = 4.dp),
        style = MaterialTheme.typography.labelSmall.copy( // Label ko chota kiya
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        ),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
    )
}

@Composable
private fun PlateCard(plate: PlateModel) {
    Card(
        modifier = Modifier.wrapContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Scale down plate preview if it feels too large
            PlateCanvas(plate = plate)
        }
    }
}

@Composable
private fun DetailBox(plate: PlateModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Vehicle Info", // "Configuration" se "Info" (Short labels)
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            DetailItem(Icons.Outlined.DirectionsCar, "Type", plate.vehicleType.name.replace("_", " "))
            DetailItem(Icons.Outlined.LocationOn, "Province", plate.province.name)
            DetailItem(Icons.Outlined.Info, "Number", plate.registrationNumber.uppercase())
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
    }
}