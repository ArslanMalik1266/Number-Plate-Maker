package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
    var itemToDelete by remember { mutableStateOf<RecentPlateItem?>(null) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<String>() }


    Scaffold(
        modifier = modifier,
        topBar = {
            HistoryTopAppBar(
                onBackClick = onBackClick,
                onClearAllClick = {
                    itemToDelete = null
                    showDeleteSheet = true}
            )
        }
    ) { innerPadding ->
        HistoryContent(
            historyList = uiState.savedPlates,
            onPlateItemClick = onPlateItemClick,
            onDeleteItemClick = { targetedItem ->
                itemToDelete = targetedItem // Specific item set karein
                showDeleteSheet = true          },
            modifier = Modifier.padding(innerPadding)
        )
        if (showDeleteSheet) {
            DeleteAllConfirmationSheet(
                plateCount = if (itemToDelete == null) uiState.savedPlates.size else 1,
                onDismiss = { showDeleteSheet = false },
                onConfirm = {
                    if (itemToDelete == null) {
                        viewModel.clearAllPlates()
                    } else {
                        viewModel.deletePlate(itemToDelete!!.toEntity())
                    }
                    showDeleteSheet = false
                    itemToDelete = null
                }
            )
        }
    }
}