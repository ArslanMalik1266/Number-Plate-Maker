package com.platepk.maker.ui.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.platepk.maker.domain.models.RecentPlateItem
import com.platepk.maker.mapper.toEntity
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.components.DeleteAllConfirmationSheet

@Composable
fun HistoryScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteSheet by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<RecentPlateItem?>(null) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<String>() }

    Scaffold(
        modifier = modifier,
        topBar = {
            HistoryTopAppBar(
                onBackClick = {
                    if (isSelectionMode) {
                        // ✅ Back press se selection mode exit
                        isSelectionMode = false
                        selectedItems.clear()
                    } else {
                        onBackClick()
                    }
                },
                onClearAllClick = {
                    if (isSelectionMode && selectedItems.isNotEmpty()) {
                        // ✅ Selection mode mein — selected delete karo
                        itemToDelete = null
                        showDeleteSheet = true
                    } else {
                        // ✅ Normal mode — sab delete karo
                        itemToDelete = null
                        showDeleteSheet = true
                    }
                }, isSelectionMode = isSelectionMode,
                selectedCount = selectedItems.size

            )
        }
    ) { innerPadding ->
        key(isSelectionMode) {
            HistoryContent(
                historyList = uiState.savedPlates,
                onPlateItemClick = onPlateItemClick,
                onDeleteItemClick = { targetedItem ->
                    itemToDelete = targetedItem
                    showDeleteSheet = true
                },
                isSelectionMode = isSelectionMode,
                selectedItems = selectedItems,
                onSelectionToggle = {
                    selectedItems.clear()
                    isSelectionMode = true
                },
                onUnselectAll = {
                    selectedItems.clear()
                    isSelectionMode = false
                },
                onItemSelectionToggle = { id ->
                    if (isSelectionMode) {
                        if (selectedItems.contains(id)) {
                            selectedItems.remove(id)
                        } else {
                            selectedItems.add(id)
                        }
                    }
                },
                onDeleteSelected = {
                    if (selectedItems.isNotEmpty()) {
                        itemToDelete = null
                        showDeleteSheet = true
                    }
                },
                modifier = Modifier.padding(innerPadding)
            )
        }

        if (showDeleteSheet) {
            DeleteAllConfirmationSheet(
                plateCount = if (itemToDelete == null && !isSelectionMode)
                    uiState.savedPlates.size
                else if (isSelectionMode)
                    selectedItems.size
                else 1,
                onDismiss = { showDeleteSheet = false },
                onConfirm = {
                    when {
                        isSelectionMode -> {
                            // ✅ Selected items delete karo
                            uiState.savedPlates
                                .filter { selectedItems.contains(it.id) }
                                .forEach { viewModel.deletePlate(it.toEntity()) }
                            selectedItems.clear()
                            isSelectionMode = false
                        }

                        itemToDelete != null -> {
                            viewModel.deletePlate(itemToDelete!!.toEntity())
                        }

                        else -> {
                            viewModel.clearAllPlates()
                        }
                    }
                    showDeleteSheet = false
                    itemToDelete = null
                }
            )
        }
    }
}