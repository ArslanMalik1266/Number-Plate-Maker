package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.ui.components.RecentPlateCard
import com.webscare.numberplatemaker.ui.theme.appBackground
import com.webscare.numberplatemaker.ui.theme.subtitleGray
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun HistoryContent(
    historyList: List<RecentPlateItem>,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    onDeleteItemClick: (RecentPlateItem) -> Unit,
    isSelectionMode: Boolean = false,
    selectedItems: List<String> = emptyList(),
    onUnselectAll: () -> Unit = {},
    onSelectionToggle: () -> Unit = {},
    onItemSelectionToggle: (String) -> Unit = {},
    onDeleteSelected: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.appBackground)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSelectionMode)
                    "${selectedItems.size} selected"
                else
                    "${historyList.size} plates · tap to view",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.subtitleGray
            )

            Text(
                text = if (isSelectionMode) "Unselect" else "Select",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelectionMode) Color(0xFFE53935) else MaterialTheme.colorScheme.primary,
                modifier = Modifier.addPressEffect {
                    when {
                        !isSelectionMode -> onSelectionToggle()
                        selectedItems.isEmpty() -> onUnselectAll()
                        else ->  onUnselectAll()
                    }
                }
            )
        }

        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No history found", color = MaterialTheme.colorScheme.subtitleGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(
                    items = historyList,
                    key = { it.id }
                ) { plateItem ->
                    val isSelected = selectedItems.contains(plateItem.id)
                    RecentPlateCard(
                        item = plateItem,
                        onItemClick = {
                            if (isSelectionMode) {
                                onItemSelectionToggle(plateItem.id)
                            } else {
                                onPlateItemClick(it)
                            }
                        },

                        isSelectionMode = isSelectionMode,
                        isSelected = isSelected,
                        onDeleteClick = if (isSelectionMode) null else {
                            { onDeleteItemClick(plateItem) }
                        },
                        modifier = Modifier.then(
                            if (isSelected)
                                Modifier.border(
                                    2.dp,
                                    Color(0xFF0C8A53),
                                    androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                )
                            else Modifier
                        )
                    )

                }
            }
        }
    }
}