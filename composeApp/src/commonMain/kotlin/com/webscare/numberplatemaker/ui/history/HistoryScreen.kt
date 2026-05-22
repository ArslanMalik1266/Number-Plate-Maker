package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.mapper.toEntity
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.components.DeleteAllConfirmationSheet

@Composable
fun HistoryScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            HistoryTopAppBar(
                onBackClick = onBackClick,
                onClearAllClick = { showDeleteSheet = true } // ViewModel se clear logic
            )
        }
    ) { innerPadding ->
        HistoryContent(
            historyList = uiState.savedPlates,
            onPlateItemClick = onPlateItemClick,
            onDeleteItemClick = { targetedItem ->
                viewModel.deletePlate(targetedItem.toEntity())
            },
            modifier = Modifier.padding(innerPadding)
        )
        if (showDeleteSheet) {
            DeleteAllConfirmationSheet(
                plateCount = uiState.savedPlates.size,
                onDismiss = { showDeleteSheet = false },
                onConfirm = {
                    viewModel.clearAllPlates()
                    showDeleteSheet = false
                }
            )
        }
    }
}