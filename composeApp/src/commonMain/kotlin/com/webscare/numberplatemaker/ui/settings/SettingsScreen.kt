package com.webscare.numberplatemaker.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.components.DeleteAllConfirmationSheet
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.ui.theme.appBackground

@Composable
fun SettingsScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.settingsState.collectAsState()
    var showDeleteSheet by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            SettingsTopAppBar(onBackClick = onBackClick)
        },
        containerColor = MaterialTheme.colorScheme.appBackground
    ) { innerPadding ->
        SettingsScreenContent(
            uiState = uiState,
            onClearAllClick = { showDeleteSheet = true },
            onThemeToggle = { index ->
                viewModel.onThemeSelected(index)
            },
            contentPadding = innerPadding

        )
    }
    if (showDeleteSheet) {
        DeleteAllConfirmationSheet(
            plateCount = uiState.savedCount,
            onDismiss = { showDeleteSheet = false },
            onConfirm = {
                viewModel.clearAllPlates() // Perform deletion
                showDeleteSheet = false    // Close sheet
            }
        )
    }
}