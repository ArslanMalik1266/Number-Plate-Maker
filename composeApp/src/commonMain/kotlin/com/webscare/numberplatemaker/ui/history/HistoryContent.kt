package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.ui.components.RecentPlateCard
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.ui.theme.appBackground
import com.webscare.numberplatemaker.ui.theme.subtitleGray
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun HistoryContent(
    historyList: List<RecentPlateItem>,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    onDeleteItemClick: (RecentPlateItem) -> Unit,
    isSelectionMode: Boolean = false,
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
                text = "${historyList.size} plates · tap to view",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.subtitleGray
            )

            // Right-aligned Select Button
            Text(
                text = if (isSelectionMode) "Delete" else "Select",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.addPressEffect() { }
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
                    RecentPlateCard(
                        item = plateItem,
                        onItemClick = onPlateItemClick,
                        onDeleteClick = { onDeleteItemClick(plateItem) }
                    )
                }
            }
        }
    }
}