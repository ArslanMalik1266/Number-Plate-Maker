package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
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

    // ✅ SIRF YE ADD KIYA
    val snackbarHostState = remember { SnackbarHostState() }

    // Density context for high-quality scaling
    val currentDensity = LocalDensity.current

    // Layers for capturing only the Canvas content
    val frontLayer = rememberGraphicsLayer()
    val backLayer = rememberGraphicsLayer()

    // ✅ SIRF SCAFFOLD ADD KIYA BAAKI SAB SAME
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // --- HEADER ---
            Text(
                text = "Final Previews",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
            )
            Text(
                text = "Ultra HD (4K) Export Enabled",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (plate != null) {
                // --- FRONT PLATE ---
                PreviewLabel("FRONT PLATE")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(plate.config.bgColor))
                        .drawWithContent {
                            // 1. Target (2K) Calculation
                            val targetWidth = 2048f
                            val scaleFactor = targetWidth / size.width
                            val targetHeight = size.height * scaleFactor

                            // 2. High-Res Recording (Invisible to user)
                            frontLayer.record(
                                size = androidx.compose.ui.unit.IntSize(targetWidth.toInt(), targetHeight.toInt())
                            ) {
                                withTransform({
                                    scale(scaleFactor, scaleFactor, pivot = Offset.Zero)
                                }) {
                                    // Ye high resolution mein record ho raha hai
                                    this@drawWithContent.drawContent()
                                }
                            }

                            // 3. Normal Preview (Visible to user)
                            // Hum scaled layer draw nahi kar rahe, balkay seedha content draw kar rahe hain
                            // Is se preview waisa hi dikhega jaisa mobile screen par fit hona chahiye
                            drawContent()
                        }
                ) {
                    uiState.frontPlate?.let { PlateCanvas(it.config, Modifier.fillMaxSize()) }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- REAR PLATE ---
                PreviewLabel("REAR PLATE")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(plate.config.bgColor))
                        .drawWithContent {
                            // 1. Target Resolution (2K)
                            val targetWidth = 2048f
                            val scaleFactor = targetWidth / size.width
                            val targetHeight = size.height * scaleFactor

                            // 2. Background Recording (High Res)
                            // Is block ke andar hum scaling apply karenge taake bitmap clear bane
                            backLayer.record(
                                size = androidx.compose.ui.unit.IntSize(targetWidth.toInt(), targetHeight.toInt())
                            ) {
                                withTransform({
                                    scale(scaleFactor, scaleFactor, pivot = Offset.Zero)
                                }) {
                                    this@drawWithContent.drawContent()
                                }
                            }

                            // 3. Screen Preview (Normal)
                            // Yahan drawLayer call karne ke bajaye seedha drawContent() use karein
                            // Is se preview waisa hi dikhega jaisa screen par hona chahiye (No Zoom)
                            drawContent()
                        }
                ) {
                    uiState.backPlate?.let { PlateCanvas(it.config, Modifier.fillMaxSize()) }
                }

                Spacer(modifier = Modifier.height(28.dp))
                DetailBox(plate)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- ACTIONS ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.exporting
                ) {
                    Text("Reset", fontSize = 14.sp)
                }
                Button(
                    onClick = { showExportSheet = true },
                    modifier = Modifier.weight(1f).height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.exporting
                ) {
                    if (uiState.exporting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                    } else {
                        Text("Download HD", fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            ExportOptionsList { format ->
                showExportSheet = false
                scope.launch {
                    // High resolution factor (6f = 4K quality approx)
                    val scaleFactor = 6f

                    delay(300)

                    try {
                        // Note: toImageBitmap() density parameter nahi leta
                        // Isliye hum seedha call karenge
                        val frontBitmap = frontLayer.toImageBitmap()
                        val backBitmap = backLayer.toImageBitmap()

                        // Transparency conversion with your updated function
                        val frontBytes = frontBitmap.toByteArray(format)
                        val backBytes = backBitmap.toByteArray(format)

                        viewModel.exportPlate(frontBytes, backBytes, format)

                        // ✅ YE ADD KIYA - FORMAT-SPECIFIC MESSAGES
                        val message = when (format) {
                            ExportFormat.PNG -> "✓ PNG images saved to Downloads folder!"
                            ExportFormat.JPEG -> "✓ JPEG images saved to Gallery!"
                            ExportFormat.PDF -> "✓ PDF file saved to Gallery!"
                        }

                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Short
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // ✅ ERROR MESSAGE
                        snackbarHostState.showSnackbar(
                            message = "Failed to save. Please try again.",
                            duration = SnackbarDuration.Short
                        )
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
        Text("Select Ultra HD Format", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

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
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
private fun PreviewLabel(label: String) {
    Text(
        text = label,
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp, start = 4.dp),
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
    )
}

@Composable
private fun DetailBox(plate: PlateModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Vehicle Info", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            DetailItem(Icons.Outlined.LocationOn, "Province", plate.config.provinceName)
            DetailItem(Icons.Outlined.DirectionsCar, "Reg Number", plate.config.registrationNumber.uppercase())
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
    }
}