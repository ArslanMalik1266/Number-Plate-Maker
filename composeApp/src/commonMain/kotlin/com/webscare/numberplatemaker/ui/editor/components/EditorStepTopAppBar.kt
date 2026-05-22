package com.webscare.numberplatemaker.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorStepTopAppBar(
    title: String,
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit,
    showSteps: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlateColors.SoftBlack
                )
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(40.dp)
                        .addPressEffect { onBackClick() }
                        .background(Color.White, shape = CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PlateColors.SoftBlack,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            actions = {
                if (showSteps) {
                    Text(
                        text = "$currentStep/$totalSteps",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlateColors.SubtitleGray.copy(alpha = 0.8f),
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = PlateColors.AppBackground
            )
        )
        HorizontalDivider(
            modifier = Modifier.background(PlateColors.AppBackground),
            thickness = 1.dp,
            color = PlateColors.SubtitleGray.copy(alpha = 0.15f)
        )
    }
}