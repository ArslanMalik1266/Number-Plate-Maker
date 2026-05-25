package com.platepk.maker.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.components.DeleteAllConfirmationSheet
import com.platepk.maker.ui.theme.appBackground

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