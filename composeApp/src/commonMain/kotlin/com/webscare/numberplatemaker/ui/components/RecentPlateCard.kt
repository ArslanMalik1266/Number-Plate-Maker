package com.webscare.numberplatemaker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.toUri

import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.ui.theme.redColor
import com.webscare.numberplatemaker.ui.theme.softBlack
import com.webscare.numberplatemaker.ui.theme.subtitleGray
import com.webscare.numberplatemaker.util.addPressEffect
import com.webscare.numberplatemaker.util.formatTimestamp

@Composable
fun RecentPlateCard(
    item: RecentPlateItem,
    onItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (() -> Unit)? = null
) {
    LaunchedEffect(item.plateImageRes) {
        println("DEBUG_PATH: Path provided is ${item.plateImageRes}")
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .addPressEffect { onItemClick(item) }
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp))
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.plateImageRes,
            contentDescription = "Plate Image",
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Fit,


        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.category,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.subtitleGray
            )

            Text(
                text = item.plateNumber,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.softBlack,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.province,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.subtitleGray
                )
                Text(
                    text = "·",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.subtitleGray
                )
                Text(
                    text = formatTimestamp(item.timestamp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.subtitleGray
                )
            }
        }
        if (onDeleteClick != null) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFFFFEBEE),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .addPressEffect { onDeleteClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete item",
                    tint = MaterialTheme.colorScheme.redColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate details",
                tint = MaterialTheme.colorScheme.subtitleGray.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }
    }

}