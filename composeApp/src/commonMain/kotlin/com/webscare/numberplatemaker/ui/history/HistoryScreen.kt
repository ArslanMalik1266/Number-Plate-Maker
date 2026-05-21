package com.webscare.numberplatemaker.ui.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.webscare.numberplatemaker.domain.models.RecentPlateItem

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    onPlateItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier
){
//    var historyList by remember {
//        mutableStateOf(
//            listOf(
//                RecentPlateItem("1", "CD SACDS", "Diplomatic", "Diplomatic", "20 May 26 · 09:41"),
//                RecentPlateItem("2", "ZXC", "Motorcycle", "Balochistan", "20 May 26 · 09:41"),
//                RecentPlateItem("3", "SXV", "Motorcycle", "Balochistan", "20 May 26 · 09:39"),
//                RecentPlateItem("4", "XXCB", "Motorcycle", "Sindh", "20 May 26 · 09:38"),
//                RecentPlateItem("5", "LEA-1234", "Private Car", "Punjab", "18 May 26 · 14:56"),
//                RecentPlateItem("6", "KX-9087", "Commercial", "Sindh", "17 May 26 · 13:36"),
//                RecentPlateItem("7", "GBA-441", "Electric Car", "Gilgit-Baltistan", "16 May 26 · 11:36"),
//                RecentPlateItem("8", "PHB-7732", "Motorcycle", "Khyber Pakhtunkhwa", "14 May 26 · 15:36")
//            )
//        )
//    }

    Scaffold(
        modifier = modifier,
        topBar = {
            HistoryTopAppBar(
                onBackClick = onBackClick,
                onClearAllClick = {  }
            )
        }
    ) { innerPadding ->
//        HistoryContent(
//            historyList = historyList,
//            onPlateItemClick = onPlateItemClick,
//            onDeleteItemClick = { targetedItem ->
//                historyList = historyList.filter { it.id != targetedItem.id }
//            },
//            modifier = Modifier.padding(innerPadding)
//        )

    }
}