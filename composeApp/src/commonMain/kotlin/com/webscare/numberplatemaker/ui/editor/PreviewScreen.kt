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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.toByteArray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    viewModel: PlateViewModel, onBackClick: () -> Unit, onDownloadClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showExportSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val frontLayer = rememberGraphicsLayer()
    val backLayer = rememberGraphicsLayer()
    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Your Plate", currentStep = 3, totalSteps = 3, onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PlateColors.AppBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                        .background(Color.White).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    uiState.frontPlate?.let { plate ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "FRONT PLATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                    .drawWithContent {
                                        frontLayer.record { this@drawWithContent.drawContent() }
                                        drawContent()
                                    }) {
                                PlateCanvas(
                                    config = plate.config,
                                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                )
                            }
                        }
                    }
                    uiState.backPlate?.let { plate ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "BACK PLATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                    .drawWithContent {
                                        backLayer.record { this@drawWithContent.drawContent() }
                                        drawContent()
                                    }) {
                                PlateCanvas(
                                    config = plate.config,
                                    modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoRow("Registration", uiState.registrationNumber)
                        Divider(color = Color(0xFFF0F0F0))
                        InfoRow("Vehicle Type", uiState.selectedVehicle?.name ?: "N/A")
                        Divider(color = Color(0xFFF0F0F0))
                        InfoRow("Province", uiState.selectedProvince?.name ?: "N/A")
                        Divider(color = Color(0xFFF0F0F0))
                        InfoRow("Issued", "20 May 2026")
                    }
                }
                Button(
                    onClick = { showExportSheet = true },
                    enabled = !uiState.exporting,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0C8A53))
                ) {
                    if (uiState.exporting) CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    else Text("Download HD", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, start = 16.dp, end = 16.dp)
                    .align(Alignment.TopCenter)
            ) {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF0C8A53).copy(alpha = 0.9f),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
    if (showExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExportSheet = false }, sheetState = sheetState
        ) {
            ExportOptionsList { format ->
                showExportSheet = false
                scope.launch {
                    val frontBytes = frontLayer.toImageBitmap().toByteArray(format)
                    val backBytes = backLayer.toImageBitmap().toByteArray(format)
                    viewModel.exportPlate(frontBytes, backBytes, format)
                    snackbarHostState.showSnackbar("Exported as ${format.name}")
                }
            }
        }
    }
}

@Composable
private fun ExportOptionsList(onFormatSelected: (ExportFormat) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp, top = 8.dp)
    ) {
        Text(
            text = "Select Ultra HD Format", style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF1A1A1A), fontWeight = FontWeight.Black, fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        ExportFormatItem(
            "Save as PNG (Lossless)", "Perfect for sharp edges & text", Icons.Outlined.CheckCircle
        ) {
            onFormatSelected(ExportFormat.PNG)
        }
        ExportFormatItem(
            "Save as JPEG (High Quality)", "Compressed but clear", Icons.Outlined.CheckCircle
        ) {
            onFormatSelected(ExportFormat.JPEG)
        }
        ExportFormatItem(
            "Save as PDF (Vector Print)", "Best for physical printing", Icons.Outlined.PictureAsPdf
        ) {
            onFormatSelected(ExportFormat.PDF)
        }
    }
}

@Composable
private fun ExportFormatItem(
    title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF888880)
                )
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