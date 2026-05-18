package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
import com.webscare.numberplatemaker.util.addPressEffect
import com.webscare.numberplatemaker.util.toByteArray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPreviewStep(
    plate: PlateModel?,
    viewModel: PlateViewModel,
    onReset: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showExportSheet by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val currentDensity = LocalDensity.current

    val frontLayer = rememberGraphicsLayer()
    val backLayer = rememberGraphicsLayer()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color(0xFFF8F6F1) // ✅ Screen background matched perfectly
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- 1. Top Navigation Row ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onReset, // Custom back trigger to step reset
                    colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF1A1A1A))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. Typography Header Block ---
            Text(
                text = "Final Previews",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp,
                    color = Color(0xFF1A1A1A)
                )
            )

            Text(
                text = "Ultra HD (4K) Export Enabled",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFD4A843), // Elegant gold accent
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (plate != null) {
                // --- FRONT PLATE ---
                PreviewLabel("FRONT PLATE")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(plate.config.bgColor))
                        .drawWithContent {
                            val targetWidth = 2048f
                            val scaleFactor = targetWidth / size.width
                            val targetHeight = size.height * scaleFactor

                            frontLayer.record(
                                size = androidx.compose.ui.unit.IntSize(targetWidth.toInt(), targetHeight.toInt())
                            ) {
                                withTransform({
                                    scale(scaleFactor, scaleFactor, pivot = Offset.Zero)
                                }) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                            drawContent()
                        }
                ) {
                    uiState.frontPlate?.let { PlateCanvas(it.config, Modifier.fillMaxSize()) }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // --- REAR PLATE ---
                PreviewLabel("BACK PLATE")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(plate.config.bgColor))
                        .drawWithContent {
                            val targetWidth = 2048f
                            val scaleFactor = targetWidth / size.width
                            val targetHeight = size.height * scaleFactor

                            backLayer.record(
                                size = androidx.compose.ui.unit.IntSize(targetWidth.toInt(), targetHeight.toInt())
                            ) {
                                withTransform({
                                    scale(scaleFactor, scaleFactor, pivot = Offset.Zero)
                                }) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                            drawContent()
                        }
                ) {
                    uiState.backPlate?.let { PlateCanvas(it.config, Modifier.fillMaxSize()) }
                }

                Spacer(modifier = Modifier.height(32.dp))
                DetailBox(plate)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- ACTIONS ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !uiState.exporting,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF1A1A1A)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE5E2DA))
                ) {
                    Text("Reset", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { showExportSheet = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !uiState.exporting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1A1A1A),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFFE5E2DA),
                        disabledContentColor = Color(0xFFB5B5B0)
                    )
                ) {
                    if (uiState.exporting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text("Download HD", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // --- FORMAT SELECTION SHEET ---
    if (showExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExportSheet = false },
            sheetState = sheetState,
            containerColor = Color.White, // Match card background theme
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ExportOptionsList { format ->
                showExportSheet = false
                scope.launch {
                    delay(300)
                    try {
                        val frontBitmap = frontLayer.toImageBitmap()
                        val backBitmap = backLayer.toImageBitmap()

                        val frontBytes = frontBitmap.toByteArray(format)
                        val backBytes = backBitmap.toByteArray(format)

                        viewModel.exportPlate(frontBytes, backBytes, format)

                        val message = when (format) {
                            ExportFormat.PNG -> "PNG images saved to Gallery!"
                            ExportFormat.JPEG -> "JPEG images saved to Gallery!"
                            ExportFormat.PDF -> "PDF file saved to Downloads folder!"
                        }

                        snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        snackbarHostState.showSnackbar(message = "Failed to save. Please try again.", duration = SnackbarDuration.Short)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExportOptionsList(onFormatSelected: (ExportFormat) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 48.dp, top = 8.dp)
    ) {
        Text(
            text = "Select Ultra HD Format",
            style = MaterialTheme.typography.titleMedium.copy(
                color = Color(0xFF1A1A1A),
                fontWeight = FontWeight.Black,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(20.dp))

        ExportFormatItem("Save as PNG (Lossless)", "Perfect for sharp edges & text", Icons.Outlined.CheckCircle) {
            onFormatSelected(ExportFormat.PNG)
        }
        ExportFormatItem("Save as JPEG (High Quality)", "Compressed but clear", Icons.Outlined.CheckCircle) {
            onFormatSelected(ExportFormat.JPEG)
        }
        ExportFormatItem("Save as PDF (Vector Print)", "Best for physical printing", Icons.Outlined.PictureAsPdf) {
            onFormatSelected(ExportFormat.PDF)
        }
    }
}

@Composable
private fun ExportFormatItem(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF888880))
            }
        }
    }
}

@Composable
private fun PreviewLabel(label: String) {
    Text(
        text = label,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, start = 4.dp),
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            fontSize = 11.sp
        ),
        color = Color(0xFF888880)
    )
}

@Composable
private fun DetailBox(plate: PlateModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E2DA)) // Blend beautifully with the cream background
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Vehicle Info",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
            Spacer(modifier = Modifier.height(14.dp))
            DetailItem(Icons.Outlined.LocationOn, "Province", plate.config.provinceName)
            val formattedVehicleType = plate.config.vehicleType.name.replace("_", " ").uppercase()
            DetailItem(Icons.Outlined.Build, "Vehicle Type", formattedVehicleType)
            DetailItem(Icons.Outlined.DirectionsCar, "Reg Number", plate.config.registrationNumber.uppercase())
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = Color(0xFF888880))
        Spacer(modifier = Modifier.width(10.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = Color(0xFF888880))
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A)))
    }
}