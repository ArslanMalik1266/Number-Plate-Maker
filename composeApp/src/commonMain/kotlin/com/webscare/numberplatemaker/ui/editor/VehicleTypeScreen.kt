package com.webscare.numberplatemaker.ui.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.editor.components.EditorStepTopAppBar
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.ic_commercial
import numberplatemaker.composeapp.generated.resources.ic_gov
import numberplatemaker.composeapp.generated.resources.ic_motorcycle
import numberplatemaker.composeapp.generated.resources.ic_private_car
import numberplatemaker.composeapp.generated.resources.ic_riksha
import org.jetbrains.compose.resources.painterResource

private data class VehicleUiModel(
    val type: VehicleType,
    val title: String,
    val subtitle: String,
    val iconRes: org.jetbrains.compose.resources.DrawableResource,
    val iconBgColor: Color,
)

@Composable
fun VehicleTypeScreen(
    onBackClick: () -> Unit,
    viewModel: PlateViewModel,
    onVehicleTypeSelected: (VehicleType) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val vehicleUiItems = remember {
        listOf(
            VehicleUiModel(
                VehicleType.PRIVATE_CAR,
                "Private Car",
                "Personal vehicles",
                Res.drawable.ic_private_car,
                Color(0xFFF8FAFC)
            ),
            VehicleUiModel(
                VehicleType.MOTORBIKE,
                "Motorcycle",
                "2-wheelers under 250cc",
                Res.drawable.ic_motorcycle,
                Color(0xFFF8FAFC)
            ),
            VehicleUiModel(
                VehicleType.COMMERCIAL,
                "Commercial",
                "Goods & passenger services",
                Res.drawable.ic_commercial,
                Color(0xFFFFCC00)
            ),
            VehicleUiModel(
                VehicleType.GOVERNMENT,
                "Government",
                "Federal / provincial fleet",
                Res.drawable.ic_gov,
                Color(0xFF0C8A53)
            ),
            VehicleUiModel(
                VehicleType.HEAVY_TRANSPORT,
                "Heavy Transport",
                "Trucks & trailers",
                Res.drawable.ic_commercial,
                Color(0xFFFFCC00)
            ),
            VehicleUiModel(
                VehicleType.RICKSHAW,
                "Rickshaw",
                "3-wheeler auto",
                Res.drawable.ic_riksha,
                Color(0xFFFFCC00)
            ),
            VehicleUiModel(
                VehicleType.DIPLOMATIC,
                "Diplomatic",
                "Embassy / consular",
                Res.drawable.ic_private_car,
                Color(0xFFD32F2F)
            ),
            VehicleUiModel(
                VehicleType.ELECTRIC_CAR,
                "Electric Car",
                "EV passenger car",
                Res.drawable.ic_private_car,
                Color(0xFF00C853)
            ),
            VehicleUiModel(
                VehicleType.ELECTRIC_BIKE,
                "Electric Car",
                "EV passenger car",
                Res.drawable.ic_motorcycle,
                Color(0xFF00C853)
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            EditorStepTopAppBar(
                title = "Vehicle Type",
                currentStep = 1,
                totalSteps = 3,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PlateColors.AppBackground)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "What are you registering?",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PlateColors.SoftBlack,
                lineHeight = 34.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Vehicle category determines plate color and prefix.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PlateColors.SubtitleGray,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        vehicleUiItems.forEach { plateItem ->
                            VehicleTypeCard(
                                uiModel = plateItem,
                                onCardClick = { onVehicleTypeSelected(plateItem.type) },
                                isSelected = uiState.selectedVehicle == plateItem.type,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun VehicleTypeCard(
    viewModel: PlateViewModel,
    uiModel: VehicleUiModel,
    onCardClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearRegistrationFields()
        }
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .addPressEffect { onCardClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF0C8A53) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(uiModel.iconBgColor, shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(uiModel.iconRes),
                contentDescription = uiModel.title,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = uiModel.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PlateColors.SoftBlack,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = uiModel.subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = PlateColors.SubtitleGray,
                lineHeight = 16.sp
            )
        }


        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate Next",
            tint = PlateColors.SubtitleGray.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}