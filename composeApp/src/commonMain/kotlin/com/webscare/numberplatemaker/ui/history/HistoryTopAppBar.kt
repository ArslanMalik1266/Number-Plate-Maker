package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryTopAppBar(
    onBackClick: () -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Recent plates",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PlateColors.SoftBlack,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = PlateColors.SoftBlack
                )
            }
        },
        actions = {
            Text(
                text = "Clear all",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F),
                modifier = Modifier
                    .padding(end = 16.dp)
                    .addPressEffect { onClearAllClick() }
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PlateColors.AppBackground)
    )
}