package com.platepk.maker.ui.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.platepk.maker.domain.models.Province
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.editor.components.EditorStepTopAppBar
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.softBlack
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.ajk_logo
import numberplatemaker.composeapp.generated.resources.balochistan_logo
import numberplatemaker.composeapp.generated.resources.gb_logo_black
import numberplatemaker.composeapp.generated.resources.islamabad_logo
import numberplatemaker.composeapp.generated.resources.kpk_logo
import numberplatemaker.composeapp.generated.resources.punjab_logo
import org.jetbrains.compose.resources.painterResource


private data class ProvinceUiModel(
    val province: Province,
    val title: String,
    val description: String,
    val iconRes: org.jetbrains.compose.resources.DrawableResource,
    val tintIcon: Boolean = true
)

@Composable
fun ProvinceSelectionScreen(
    onBackClick: () -> Unit,
    viewModel: PlateViewModel,
    onProvinceSelected: (Province) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val provinces = remember {
        listOf(
            ProvinceUiModel(
                Province.PUNJAB,
                "Punjab",
                "Province of the five rivers",
                Res.drawable.punjab_logo,
                tintIcon = true
            ),
            ProvinceUiModel(
                Province.SINDH,
                "Sindh",
                "Land of the Indus",
                Res.drawable.islamabad_logo,
                tintIcon = true
            ),
            ProvinceUiModel(Province.KPK, "KPK", "Khyber Pakhtunkhwa", Res.drawable.kpk_logo),
            ProvinceUiModel(
                Province.BALOCHISTAN,
                "Balochistan",
                "Land of mountains",
                Res.drawable.balochistan_logo,
                tintIcon = true
            ),
            ProvinceUiModel(
                Province.ISLAMABAD,
                "Islamabad",
                "Capital Territory",
                Res.drawable.islamabad_logo,
                tintIcon = true
            ),
            ProvinceUiModel(
                Province.AJK,
                "AJK",
                "Azad Jammu & Kashmir",
                Res.drawable.ajk_logo,
                tintIcon = true
            ),
            ProvinceUiModel(Province.GB, "GB", "Gilgit-Baltistan", Res.drawable.gb_logo_black,tintIcon = true)
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
                .background(MaterialTheme.colorScheme.appBackground)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Select your region",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.softBlack
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registration laws vary by province.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.subtitleGray
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
                                isSelected = uiState.selectedProvince == province.province,
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
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .addPressEffect { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color(0xFF0C8A53) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(uiModel.iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            colorFilter = if (uiModel.tintIcon) {
                ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
            } else null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiModel.title,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.softBlack
            )
            Text(
                text = uiModel.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.subtitleGray
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray
        )

    }
}
