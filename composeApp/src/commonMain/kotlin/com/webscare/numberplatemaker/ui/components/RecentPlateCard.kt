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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import coil3.compose.AsyncImage
//import coil3.compose.LocalPlatformContext
//import coil3.request.ImageRequest
//import coil3.request.crossfade
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun RecentPlateCard(
    item: RecentPlateItem,
    onItemClick: (RecentPlateItem) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .addPressEffect { onItemClick(item) }
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .padding(all = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        AsyncImage(
//            model = item.plateImageRes,
//            contentDescription = "Plate Image",
//            modifier = Modifier.size(60.dp),
//            contentScale = ContentScale.Fit
//        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1.0f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Private Car",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PlateColors.SubtitleGray
            )

            Text(
                text = "ABC 123",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PlateColors.SoftBlack
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Punjab - 20 May 2026",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = PlateColors.SubtitleGray
            )
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
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate details",
                tint = PlateColors.SubtitleGray.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }
    }

}