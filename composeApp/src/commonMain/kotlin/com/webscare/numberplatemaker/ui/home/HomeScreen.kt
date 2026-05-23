package com.webscare.numberplatemaker.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.mapper.toEntity
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.components.PkPlateTopAppBar
import com.webscare.numberplatemaker.ui.components.RecentPlateCard
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.ui.theme.appBackground
import com.webscare.numberplatemaker.ui.theme.gradientGreenDark
import com.webscare.numberplatemaker.ui.theme.gradientGreenLight
import com.webscare.numberplatemaker.ui.theme.softBlack
import com.webscare.numberplatemaker.ui.theme.subtitleGray
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun HomeScreen(
    viewModel: PlateViewModel,
    onNavigateToSettings: () -> Unit,
    onViewAllRecentClick: () -> Unit,
    onGeneratePlateClick: () -> Unit,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stats = remember(uiState.savedPlates) { viewModel.getStats() }

    LaunchedEffect(Unit) {
        viewModel.resetSelection()
        viewModel.onVehicleSelected(VehicleType.PRIVATE_CAR)
        viewModel.onProvinceSelected(Province.PUNJAB)
    }

    Scaffold(
        modifier = modifier,
        topBar = { PkPlateTopAppBar(onSettingsClick = onNavigateToSettings) }
    ) { innerPadding ->

        // LazyColumn ko pura space dein
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.appBackground),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header Section
            item {
                Text(
                    text = "Build your plate",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.softBlack,
                    lineHeight = 38.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Pick a vehicle and province. Front and back, ready in seconds.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.subtitleGray
                )
                Spacer(modifier = Modifier.height(24.dp))
                GeneratePlateCard(onGenerateClick = onGeneratePlateClick)
            }

            // 2. Stats Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatItemCard(value = stats.first.toString(), label = "Total")
                    StatItemCard(value = stats.second.toString(), label = "Provinces")
                    StatItemCard(value = stats.third.toString(), label = "This week")
                }
            }

            // 3. Recent Plates Header
            item {
                RecentPlatesHeader(onViewAllClick = onViewAllRecentClick)
            }

            // 4. Recent Plates List
            if (uiState.savedPlates.isEmpty()) {
                item {
                    Text(
                        text = "No plates generated yet",
                        modifier = Modifier.padding(vertical = 40.dp).fillMaxWidth(),
                        color = MaterialTheme.colorScheme.subtitleGray
                    )
                }
            } else {
                items(uiState.savedPlates) { plateItem ->
                    RecentPlateCard(
                        item = plateItem,
                        onItemClick = onPlateItemClick,

                    )
                }
            }
        }
    }
}


@Composable
fun GeneratePlateCard(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradientColors = listOf(
        MaterialTheme.colorScheme.gradientGreenLight,
        MaterialTheme.colorScheme.gradientGreenDark
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .addPressEffect { onGenerateClick() }
            .background(
                brush = Brush.verticalGradient(
                    colors = gradientColors
                ),
                shape = RoundedCornerShape(28.dp)
            )

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "START HERE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Generate a plate",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "9 vehicle types · 7 provinces",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Circular Plus Action UI Button
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Generate",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun RowScope.StatItemCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .weight(1f)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.softBlack,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.subtitleGray,
            lineHeight = 16.sp
        )
    }
}

@Composable
fun RecentPlatesHeader(
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Recent plates",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.softBlack
            )
        }

        Text(
            text = "View all →",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = PlateColors.GovGreen,
            modifier = Modifier.addPressEffect { onViewAllClick() }
        )
    }
}