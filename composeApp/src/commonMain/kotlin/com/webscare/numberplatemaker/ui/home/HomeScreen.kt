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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.ui.components.PkPlateTopAppBar
import com.webscare.numberplatemaker.ui.components.RecentPlateCard
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onViewAllRecentClick: () -> Unit,
    onGeneratePlateClick: () -> Unit,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val mockRecentPlates = remember {
        listOf(
            RecentPlateItem("1", "CD SACDS", "Diplomatic", "Diplomatic", "20 May 26 · 09:41"),
            RecentPlateItem("2", "ZXC", "Motorcycle", "Balochistan", "20 May 26 · 09:41"),
            RecentPlateItem("3", "SXV", "Motorcycle", "Balochistan", "20 May 26 · 09:39"),
            RecentPlateItem("4", "XXCB", "Motorcycle", "Sindh", "20 May 26 · 09:38"),
            RecentPlateItem("5", "LH 456", "Private Car", "Punjab", "19 May 26 · 18:22"),
            RecentPlateItem("6", "RI 789", "Commercial", "Islamabad", "18 May 26 · 14:15")
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            PkPlateTopAppBar(onSettingsClick = onNavigateToSettings)
        }
    ) { innerPadding ->

        // 1. Parent main Column (Is par koi scroll nahi lagaya, yeh fixed rahega)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PlateColors.AppBackground)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // ----- FIXED UPPER CONTENT SECTION -----
            Text(
                text = "Salaam — design your plate",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PlateColors.SoftBlack,
                lineHeight = 38.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pick a vehicle type and province. Front and back plates ready in seconds.",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = PlateColors.SubtitleGray,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            GeneratePlateCard(onGenerateClick = onGeneratePlateClick)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatItemCard(value = "4", label = "Total")
                StatItemCard(value = "4", label = "Provinces")
                StatItemCard(value = "4", label = "This week")
            }
            Spacer(modifier = Modifier.height(24.dp))
            RecentPlatesHeader(onViewAllClick = onViewAllRecentClick)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ){
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        mockRecentPlates.forEach { plateItem ->
                            RecentPlateCard(
                                item = plateItem,
                                onItemClick = onPlateItemClick
                            )
                        }
                    }
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
        Color(0xFF0C8A53),
        Color(0xFF046637)
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
                    text = "NEW PLATE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Generate a number\nplate",
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
                color = Color.White,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = PlateColors.SoftBlack,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = PlateColors.SubtitleGray,
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
                color = PlateColors.SoftBlack
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