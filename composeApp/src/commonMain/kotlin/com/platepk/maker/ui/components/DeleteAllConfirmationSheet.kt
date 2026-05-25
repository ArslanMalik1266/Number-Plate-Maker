package com.platepk.maker.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAllConfirmationSheet(
    plateCount: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isSingle = plateCount == 1
    val title = if (isSingle) "Delete this plate?" else "Delete all recents?"
    val subtitle = if (isSingle)
        "This saved plate will be removed. This can't be undone."
    else "All $plateCount saved plates will be removed. This can't be undone."
    val buttonText = if (isSingle) "Delete" else "Delete all"
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Red Trash Icon in Circle
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFFEBEE), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = subtitle,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF5F5F5),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Cancel", fontWeight = FontWeight.Bold)
                }

                // Delete All Button
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD32F2F),
                        contentColor = Color.White
                    )
                ) {
                    Text(buttonText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}