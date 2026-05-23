package com.webscare.numberplatemaker.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.SettingsUiState
import com.webscare.numberplatemaker.ui.editor.InfoRow
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.ui.theme.appBackground
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onClearAllClick: () -> Unit,
    onThemeToggle: (Int) -> Unit,
    contentPadding: PaddingValues,

) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.appBackground),
        contentPadding = contentPadding
    ) {
        item { SectionHeader("APPEARANCE") }
        item { ThemeToggleRow(
            selectedIndex = uiState.selectedThemeIndex, // Index pass karein
            onThemeChange = onThemeToggle
        ) }

        item { SectionHeader("DATA") }
        item {
            SettingsRow(
                title = "Clear all recents",
                subtitle = if (uiState.savedCount == 0) "No plates saved" else "${uiState.savedCount} saved plates",
                onClick = onClearAllClick,
                titleColor = Color(0xFFD32F2F),
                icon = Icons.Default.DeleteOutline,
                showChevron = false
            )
        }
        item { SectionHeader("ABOUT") }
        item {
            InfoRowSettings(title = "App version", value = "1.0")
        }


    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp),
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color(0xFF6F767E),
        letterSpacing = 0.5.sp
    )
}

@Composable
fun InfoRowSettings(title: String, value: String) {
    Surface(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, fontWeight = FontWeight.Medium)
            Text(value, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    subtitle: String,
    icon: ImageVector, // Icon parameter made required
    onClick: () -> Unit,
    showChevron: Boolean = true,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .addPressEffect() { onClick() }
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface) // Light Mode background
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFDE8E8))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = titleColor
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Chevron (Navigation Arrow)
            if (showChevron) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = Color.Gray // Chevron color matching design
                )
            }
        }
    }
}

