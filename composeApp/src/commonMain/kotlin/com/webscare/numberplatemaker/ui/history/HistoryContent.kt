package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun HistoryContent(
    historyList: List<RecentPlateItem>,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    onDeleteItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PlateColors.AppBackground)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "${historyList.size} plates · tap to view, swipe to delete",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = PlateColors.SubtitleGray,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        if (historyList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No history found", color = PlateColors.SubtitleGray)
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