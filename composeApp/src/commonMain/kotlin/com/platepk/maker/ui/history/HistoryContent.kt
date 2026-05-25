package com.platepk.maker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.platepk.maker.domain.models.RecentPlateItem
import com.platepk.maker.ui.components.RecentPlateCard
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect

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
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No recent plates",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Generated plates show up here.",
                        color = MaterialTheme.colorScheme.outline
                    )
                }
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