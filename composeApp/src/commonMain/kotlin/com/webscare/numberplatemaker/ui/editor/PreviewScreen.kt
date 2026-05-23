package com.webscare.numberplatemaker.ui.editor

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.ui.theme.appBackground
import com.webscare.numberplatemaker.ui.theme.subtitleGray
import com.webscare.numberplatemaker.util.addPressEffect
import com.webscare.numberplatemaker.util.formatTimestamp
import com.webscare.numberplatemaker.util.toByteArray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    navigateToHome: () -> Unit,
    plateId: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showExportSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val frontLayer = rememberGraphicsLayer()
    val backLayer = rememberGraphicsLayer()
    val plateData = remember(plateId, uiState.savedPlates) {
        uiState.savedPlates.find { it.id == plateId }
    }
    println("DEBUG_PREVIEW: plateId = $plateId")
    println("DEBUG_PREVIEW: plateData.frontImageRes = ${plateData?.plateImageRes}")
    println("DEBUG_PREVIEW: plateData.backImageRes = ${plateData?.plateImageBackRes}")

    val frontConfig = uiState.frontPlate?.config
    val backConfig = uiState.backPlate?.config

    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            viewModel.resetExportState()
        }
    }

    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Your Plate",
                currentStep = 3,
                totalSteps = 3,
                onBackClick = onBackClick,
                showSteps = false
            )
        },
        bottomBar = {
            Surface(
                color = Color.Transparent,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .fillMaxHeight()
                            .addPressEffect {
                                viewModel.clearRegistrationFields()
                                navigateToHome()
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Home, null, tint = Color(0xFF555555))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .addPressEffect {
                                if (!uiState.exporting) showExportSheet = true
                            }
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (uiState.exporting) Color.Gray else Color(0xFF0C8A53)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.exporting) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                "Download HD",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.surface
                            )
                        }

                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.appBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    if (plateId != null && plateData != null) {
                        // History se aaya — saved image dikhao
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "FRONT PLATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = plateData.plateImageRes,
                                contentDescription = "Front Plate",
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "BACK PLATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AsyncImage(
                                model = plateData.plateImageBackRes,
                                contentDescription = "Back Plate",
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    } else {
                        // Naya generate hua — PlateCanvas dikhao
                        uiState.frontPlate?.let {
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
                                        }
                                ) {
                                    if (frontConfig != null) {
                                        PlateCanvas(
                                            config = frontConfig,
                                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                        )
                                    }
                                }
                            }
                        }
                        uiState.backPlate?.let {
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
                                        }
                                ) {
                                    if (backConfig != null) {
                                        PlateCanvas(
                                            config = backConfig,
                                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InfoRow(
                            label = "Registration",
                            value = plateData?.plateNumber ?: uiState.registrationNumber
                        )
                        Divider(color = MaterialTheme.colorScheme.subtitleGray)

                        InfoRow(
                            label = "Vehicle Type",
                            value = plateData?.category ?: uiState.selectedVehicle?.name ?: "N/A"
                        )
                        Divider(color = MaterialTheme.colorScheme.subtitleGray)

                        InfoRow(
                            label = "Province",
                            value = plateData?.province ?: uiState.selectedProvince?.name ?: "N/A"
                        )
                        Divider(color = MaterialTheme.colorScheme.subtitleGray)

                        // Agar plate saved hai toh uska timestamp dikhayein, warna aaj ki date
                        InfoRow(
                            label = "Issued",
                            value = plateData?.let { formatTimestamp(it.timestamp) }
                                ?: "20 May 2026"
                        )

                    }
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
                        contentColor = MaterialTheme.colorScheme.surface,
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
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
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
            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.subtitleGray
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