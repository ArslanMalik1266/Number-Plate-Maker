package com.webscare.numberplatemaker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect
import org.jetbrains.compose.resources.painterResource
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.setting_icon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PkPlateTopAppBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PlateColors.AppBackground
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentHeight()
            ) {
                // PK Badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = PlateColors.GovGreen,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PK",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text Stack
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "PK Plate",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PlateColors.SoftBlack,
                        lineHeight = 18.sp
                    )
                    Text(
                        text = "Generator",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = PlateColors.SubtitleGray,
                        lineHeight = 14.sp
                    )
                }
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp)
                    .addPressEffect {
                        onSettingsClick()
                    }
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                ,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(resource = Res.drawable.setting_icon),
                    contentDescription = "Settings",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    )
}