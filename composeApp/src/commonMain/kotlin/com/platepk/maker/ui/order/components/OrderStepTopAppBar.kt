package com.platepk.maker.ui.order.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.platepk.maker.ui.theme.appBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderStepTopAppBar(
    title: String,
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit
) {
    Column (modifier = Modifier.background(MaterialTheme.colorScheme.appBackground)){
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Progress Bar (The Senior Touch)
        LinearProgressIndicator(
            progress = { currentStep.toFloat() / totalSteps },
            modifier = Modifier.fillMaxWidth().height(4.dp),
            color = Color(0xFF0C8A53), // Success Green
            trackColor = Color(0xFFE0E0E0)
        )
    }
}