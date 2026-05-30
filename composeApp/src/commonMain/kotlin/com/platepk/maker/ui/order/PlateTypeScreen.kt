package com.platepk.maker.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.platepk.maker.domain.models.MaterialType
import com.platepk.maker.domain.models.PlateConfig
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.canvas.PlateCanvas
import com.platepk.maker.ui.order.components.OrderStepTopAppBar
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.softBlack
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect

@Composable
fun PlateTypeScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    plateId: String? = null,
    onContinueClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val orderState = uiState.orderState

    LaunchedEffect(plateId) {
        if (!plateId.isNullOrEmpty()) {
            viewModel.loadHistoryPlateData(plateId!!)
        }
    }



    Scaffold(
        topBar = {
            OrderStepTopAppBar(
                title = "Order Plate",
                currentStep = 1,
                totalSteps = 3,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            // ... (Bottom bar logic waisa hi rahega)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
                    .addPressEffect { onContinueClick() }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C8A53)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.appBackground)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Text Section
            item {
                Text(
                    text = "Pick your material",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ships as a pair — front & back.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Material Cards Section
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)
                ) {
                    items(uiState.orderState.availablePlateTypes) { plateType ->
                        MaterialSelectionCard(
                            modifier = Modifier.width(160.dp),
                            title = plateType.title,
                            description = plateType.description ?: "",
                            price = "Rs. ${plateType.price.toInt()}",
                            imagePath = viewModel.getImagePathForPlateType(plateType.title),
                            isSelected = orderState.selectedPlateType?.id == plateType.id,
                            onClick = { viewModel.onPlateTypeSelected(plateType) }
                        )
                    }
                }
            }

            // Add-ons Section Header
            item {
                Text(
                    text = "Add-ons",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                )
            }

            // Add-ons List
            items(uiState.orderState.availableAddsOns) { addon ->
                AddonRow(
                    title = addon.title,
                    price = "+ Rs. ${addon.price.toInt()}",
                    isChecked = uiState.orderState.selectedAddsOns.contains(addon),
                    onCheckedChange = { isChecked ->
                        viewModel.toggleAddon(addon, isChecked)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

        }
    }
}

@Composable
fun MaterialSelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    price: String,
    isSelected: Boolean,
    imagePath: String?,
    isPopular: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF0C8A53) else Color.Transparent

    Box(
        modifier = modifier
            .width(160.dp)
            .height(200.dp)
            .addPressEffect { onClick() }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))

            .padding(12.dp)
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF0C8A53),
                modifier = Modifier.align(Alignment.TopStart).size(20.dp)
            )
        }

        if (isPopular) {
            Surface(
                color = Color(0xFF0C8A53),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    "POPULAR",
                    fontSize = 8.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = imagePath,
                    contentDescription = "Plate Preview",
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    contentScale = ContentScale.Fit
                )

            }
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(description, fontSize = 12.sp, color = Color.Gray, lineHeight = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    price,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color(0xFF0C8A53)
                )
            }
        }

    }
}

@Composable
fun AddonRow(
    title: String,
    price: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val borderColor = if (isChecked) Color(0xFF0C8A53) else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCheckedChange(!isChecked) }
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isChecked) Color(0xFF0C8A53) else Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            title,
            modifier = Modifier.padding(start = 12.dp).weight(1f),
            fontWeight = FontWeight.Bold
        )
        Text(price, color = Color(0xFF0C8A53), fontWeight = FontWeight.Bold)
    }
}

