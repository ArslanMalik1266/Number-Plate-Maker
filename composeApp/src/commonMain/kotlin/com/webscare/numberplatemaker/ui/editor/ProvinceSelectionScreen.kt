package com.webscare.numberplatemaker.ui.editor

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.ui.editor.components.EditorStepTopAppBar
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.islamabad_logo
import org.jetbrains.compose.resources.painterResource


private data class ProvinceUiModel(
    val province: Province,
    val title: String,
    val description: String,
    val iconRes: org.jetbrains.compose.resources.DrawableResource
)

@Composable
fun ProvinceSelectionScreen(
    onBackClick: () -> Unit,
    onProvinceSelected: (Province) -> Unit,
    modifier: Modifier = Modifier
) {
    val provinces = remember {
        listOf(
            ProvinceUiModel(
                Province.PUNJAB,
                "Punjab",
                "Province of the five rivers",
                Res.drawable.islamabad_logo
            ),
            ProvinceUiModel(
                Province.SINDH,
                "Sindh",
                "Land of the Indus",
                Res.drawable.islamabad_logo
            ),
            ProvinceUiModel(Province.KPK, "KPK", "Khyber Pakhtunkhwa", Res.drawable.islamabad_logo),
            ProvinceUiModel(
                Province.BALOCHISTAN,
                "Balochistan",
                "Land of mountains",
                Res.drawable.islamabad_logo
            ),
            ProvinceUiModel(
                Province.ISLAMABAD,
                "Islamabad",
                "Capital Territory",
                Res.drawable.islamabad_logo
            ),
            ProvinceUiModel(
                Province.AJK,
                "AJK",
                "Azad Jammu & Kashmir",
                Res.drawable.islamabad_logo
            ),
            ProvinceUiModel(Province.GB, "GB", "Gilgit-Baltistan", Res.drawable.islamabad_logo)
        )
    }
    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Select Province",
                currentStep = 2,
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
                text = "Select your region",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PlateColors.SoftBlack
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registration laws vary by province.",
                fontSize = 14.sp,
                color = PlateColors.SubtitleGray
            )
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        provinces.forEach { province ->
                            ProvinceCard(
                                uiModel = province,
                                onClick = { onProvinceSelected(province.province) }
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
private fun ProvinceCard(
    uiModel: ProvinceUiModel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .addPressEffect { onClick() }
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(PlateColors.AppBackground, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(uiModel.iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiModel.title,
                fontWeight = FontWeight.Bold,
                color = PlateColors.SoftBlack
            )
            Text(text = uiModel.description, fontSize = 12.sp, color = PlateColors.SubtitleGray)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )

    }
}
