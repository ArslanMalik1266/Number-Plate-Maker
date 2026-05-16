package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun ProvinceSelectionStep(
    viewModel: PlateViewModel,
    onProvinceSelected: (Province) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedProvince = uiState.selectedProvince // Back navigation state persistence safe

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F1)) // Same Premium Screen Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 1. Navigation Header Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. Screen Typography Header ---
            Text(
                text = "Choose Province",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp,
                    color = Color(0xFF1A1A1A)
                )
            )

            Text(
                text = "Select your region for specific plate styling",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF888880),
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(top = 6.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- 3. Grid Layout (Matching Vehicle Selection) ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Province.entries) { province ->
                    val isSelected = selectedProvince == province

                    // Replace "_" with space for visual titles (e.g. "AJK" or "GB")
                    val displayName = province.name.replace("_", " ")

                    ProvinceCard(
                        name = displayName,
                        isSelected = isSelected,
                        onClick = { onProvinceSelected(province) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProvinceCard(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Exact theme colors matching Vehicle Selection Screen
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
            defaultElevation = if (isSelected) 6.dp else 1.dp
        ),
        border = BorderStroke(width = if (isSelected) 2.dp else 1.dp, color = borderColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    letterSpacing = 0.sp
                ),
                textAlign = TextAlign.Center
            )

        }
    }
}