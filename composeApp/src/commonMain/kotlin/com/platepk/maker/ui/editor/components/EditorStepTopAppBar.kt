package com.platepk.maker.ui.editor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.softBlack
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorStepTopAppBar(
    title: String,
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit,
    showSteps: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CenterAlignedTopAppBar(
            modifier = modifier.padding(end = 8.dp),
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.softBlack
                )
            },
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(40.dp)
                        .addPressEffect { onBackClick() }
                        .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.softBlack,
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
                        color = MaterialTheme.colorScheme.subtitleGray.copy(alpha = 0.8f),
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
                actions()
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.appBackground
            )
        )
        HorizontalDivider(
            modifier = Modifier.background(MaterialTheme.colorScheme.appBackground),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.subtitleGray.copy(alpha = 0.15f)
        )
    }
}