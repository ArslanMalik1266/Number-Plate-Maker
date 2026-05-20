package com.webscare.numberplatemaker.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.webscare.numberplatemaker.ui.editor.components.EditorStepTopAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
import com.webscare.numberplatemaker.ui.theme.PlateColors

@Composable
fun PreviewScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Your Plate",
                currentStep = 3,
                totalSteps = 3,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PlateColors.AppBackground)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Plate Preview Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp), // Padding se andar ka space milega
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                uiState.frontPlate?.let { plate ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("FRONT PLATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        // height fix hataya aur wrapContentHeight use kiya
                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            PlateCanvas(
                                config = plate.config,
                                modifier = Modifier.fillMaxWidth().wrapContentHeight() // Canvas bhi content ke mutabiq
                            )
                        }
                    }
                }

// --- Back Plate Group ---
                uiState.backPlate?.let { plate ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("BACK PLATE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                            PlateCanvas(
                                config = plate.config,
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Vehicle Info Card
            Card(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF0F0F0)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    InfoRow("Registration", uiState.registrationNumber)
                    Divider(color = Color(0xFFF0F0F0))
                    InfoRow("Vehicle Type", uiState.selectedVehicle?.name ?: "N/A")
                    Divider(color = Color(0xFFF0F0F0))
                    InfoRow("Province", uiState.selectedProvince?.name ?: "N/A")
                    Divider(color = Color(0xFFF0F0F0))
                    InfoRow("Issued", "20 May 2026")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Action
            Button(
                onClick = onDownloadClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C8A53))
            ) {
                Text("Download HD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}