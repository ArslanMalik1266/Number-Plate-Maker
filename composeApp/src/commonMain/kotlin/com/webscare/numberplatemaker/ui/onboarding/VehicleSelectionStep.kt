package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun VehicleSelectionStep(viewModel: PlateViewModel, onVehicleSelected: (VehicleType) -> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val selectedType = uiState.selectedVehicle

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F1))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Top badge
            Box(
                modifier = Modifier
                    .background(Color(0xFF1A1A1A), RoundedCornerShape(50.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "PAKISTAN",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp,
                        color = Color(0xFFD4A843)
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Choose Vehicle",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp,
                    color = Color(0xFF1A1A1A)
                )
            )

            Text(
                text = "Select your vehicle type to customize the plate",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF888880),
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(top = 6.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            val vehicleOptions = listOf(
                VehicleType.PRIVATE_CAR     to ("Private Car"       to "🚗"),
                VehicleType.MOTORBIKE       to ("Motorcycle"        to "🏍️"),
                VehicleType.COMMERCIAL      to ("Commercial"        to "🚐"),
                VehicleType.GOVERNMENT      to ("Government"        to "🏛️"),
                VehicleType.HEAVY_TRANSPORT to ("Heavy Transport"   to "🚛"),
                VehicleType.RICKSHAW        to ("Rickshaw"          to "🛺"),
                VehicleType.DIPLOMATIC      to ("Diplomatic"        to "🌐"),
                VehicleType.ELECTRIC_CAR    to ("Electric Car"      to "⚡"),
                VehicleType.ELECTRIC_BIKE   to ("Electric Bike"     to "⚡")
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(vehicleOptions) { (type, labelEmoji) ->
                    val (label, emoji) = labelEmoji
                    val isSelected = selectedType == type
                    VehicleCard(
                        label = label,
                        emoji = emoji,
                        isSelected = isSelected,
                        onClick = {
                            onVehicleSelected(type)

                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun VehicleCard(
    label: String,
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) Color(0xFF1A1A1A) else Color.White

    // Selected text color pure white, unselected par screen headings wala dark text
    val textColor = if (isSelected) Color(0xFF1A1A1A) else Color.Gray

    // Border unselected state par halka sa soft grey-beige blend hoga taake screen bg par classy lage
    val borderColor = if (isSelected) Color(0xFF1A1A1A) else Color(0xFFE5E2DA)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .addPressEffect { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =  Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        ),
        border = BorderStroke(width = if (isSelected) 2.dp else 1.dp, color = borderColor)

    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = emoji,
                fontSize = 22.sp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    letterSpacing = 0.sp
                )
            )
        }
    }
}