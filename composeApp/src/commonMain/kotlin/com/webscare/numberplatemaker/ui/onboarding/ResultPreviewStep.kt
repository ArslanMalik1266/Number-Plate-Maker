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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val uiState by viewModel.uiState.collectAsState()
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
            // --- FRONT PLATE ---
            PreviewLabel("FRONT PLATE")
            uiState.frontPlate?.let { PlateCard(plate = it) }

            Spacer(modifier = Modifier.height(20.dp))

            // --- REAR PLATE ---
            PreviewLabel("REAR PLATE")
            uiState.backPlate?.let { PlateCard(plate = it) }

            Spacer(modifier = Modifier.height(28.dp))

            // --- DETAILS ---
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
        modifier = Modifier
            .fillMaxWidth() // Plate ko poori width do taake wo proportional scale ho
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
            ,
            contentAlignment = Alignment.Center
        ) {
            // Humara Universal Canvas call ho raha hai
            PlateCanvas(
                config = plate.config,
                modifier = Modifier.fillMaxWidth() // Thori si side space chori
            )
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
                text = "Vehicle Info",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 1. Province Name (e.g., SINDH -> Sindh)
            DetailItem(
                icon = Icons.Outlined.LocationOn,
                label = "Province",
                value = plate.config.provinceName.lowercase().replaceFirstChar { it.uppercase() }
            )

            // 2. City Name (Agar empty ho to 'All Punjab' ya 'Universal' dikha sakte hain)
            if (plate.config.cityName.isNotEmpty()) {
                DetailItem(
                    icon = Icons.Outlined.Info,
                    label = "City",
                    value = plate.config.cityName
                )
            }

            // 3. Registration Number (Hamesha Uppercase aur Bold)
            DetailItem(
                icon = Icons.Outlined.DirectionsCar,
                label = "Reg Number",
                value = plate.config.registrationNumber.uppercase()
            )
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