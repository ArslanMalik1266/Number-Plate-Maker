package com.webscare.numberplatemaker.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomSplashScreen() {
    // Parent Box jo puri screen cover karega
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C8A53)),
        contentAlignment = Alignment.Center // Ye sab kuch center mein rakhega
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Square Icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("PK", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0C8A53))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("PK Plate Generator", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Pakistan number plate designer", color = Color.White.copy(0.7f))
        }

        // Bottom Text ko alag se align karein taake wo hamesha neeche rahe
        Text(
            text = "ISLAMIC REPUBLIC OF PAKISTAN",
            color = Color.White.copy(0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter) // Box ke andar bottom mein
                .padding(bottom = 32.dp)
        )
    }
}