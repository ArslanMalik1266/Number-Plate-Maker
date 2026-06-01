package com.platepk.maker.ui.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.platepk.maker.ui.editor.components.EditorStepTopAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.platepk.maker.domain.models.ExportFormat
import com.platepk.maker.domain.models.MaterialType
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.canvas.PlateCanvas
import com.platepk.maker.ui.theme.PlateColors
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.softBlack
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect
import com.platepk.maker.util.formatTimestamp
import com.platepk.maker.util.toByteArray
import com.platepk.maker.util.toSoftwareBitmap
import kotlinx.coroutines.launch
import numberplatemaker.composeapp.generated.resources.Res
import numberplatemaker.composeapp.generated.resources.ic_download
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    navigateToHome: () -> Unit,
    isFromRegistration: Boolean = true,
    plateId: String? = null,
    orderButton: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showExportSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val frontLayer = rememberGraphicsLayer()
    val backLayer = rememberGraphicsLayer()

    // ── OFFSCREEN LAYERS (invisible, placed at -10000 so user never sees them) ──
    val embossedFrontLayer = rememberGraphicsLayer()
    val embossedBackLayer = rememberGraphicsLayer()
    // These two flags flip to true the first time each offscreen canvas draws itself.
    // Once BOTH are true we have all 4 captures ready → trigger save.
    var embossedFrontReady by remember { mutableStateOf(false) }
    var embossedBackReady by remember { mutableStateOf(false) }

    val plateData = remember(plateId, uiState.savedPlates) {
        uiState.savedPlates.find { it.id == plateId }
    }
    println("DEBUG_PREVIEW: plateId = $plateId")
    println("DEBUG_PREVIEW: plateData.frontImageRes = ${plateData?.plateImageRes}")
    println("DEBUG_PREVIEW: plateData.backImageRes = ${plateData?.plateImageBackRes}")

    val frontConfig = uiState.frontPlate?.config
    val backConfig = uiState.backPlate?.config
    var isEmbossed by remember { mutableStateOf(false) }
    val frontImagePath = if (isEmbossed)
        plateData?.embossedFrontImagePath
    else plateData?.plateImageRes

    val backImagePath = if (isEmbossed)
        plateData?.embossedBackImagePath
    else plateData?.plateImageBackRes
    var showIndicator by remember { mutableStateOf(isFromRegistration) }

    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            viewModel.resetExportState()
        }
    }

    // ── CAPTURE TRIGGER ─────────────────────────────────────────────────────────
    // Fires when both offscreen canvases have rendered at least one frame.
    // No delays, no material switching — painted is captured from visible layers,
    // embossed is captured from the hidden offscreen layers.
    LaunchedEffect(embossedFrontReady, embossedBackReady, uiState.isAlreadySaved) {
        if (!embossedFrontReady || !embossedBackReady) return@LaunchedEffect
        if (uiState.isAlreadySaved) return@LaunchedEffect
        if (plateId != null) return@LaunchedEffect
        if (uiState.frontPlate == null || uiState.backPlate == null) return@LaunchedEffect

        try {
            val paintedFrontBytes  = frontLayer.toImageBitmap().toSoftwareBitmap().toByteArray(ExportFormat.PNG)
            val paintedBackBytes   = backLayer.toImageBitmap().toSoftwareBitmap().toByteArray(ExportFormat.PNG)
            val embossedFrontBytes = embossedFrontLayer.toImageBitmap().toSoftwareBitmap().toByteArray(ExportFormat.PNG)
            val embossedBackBytes  = embossedBackLayer.toImageBitmap().toSoftwareBitmap().toByteArray(ExportFormat.PNG)

            viewModel.savePlateImages(
                paintedFrontBytes, paintedBackBytes,
                embossedFrontBytes, embossedBackBytes
            )
        } catch (e: Exception) {
            println("ERROR capturing plates: ${e.message}")
        }
    }
    // ────────────────────────────────────────────────────────────────────────────

    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Your Plate",
                currentStep = 3,
                totalSteps = 3,
                onBackClick = onBackClick,
                showSteps = false,
                actions = {
                    val activeMaterial = uiState.frontPlate?.config?.materialType
                        ?: uiState.orderState.selectedMaterial
                    PlateStyleToggle(
                        selected = if (plateId != null) {
                            if (isEmbossed) MaterialType.EMBOSSED else MaterialType.PAINTED
                        } else {
                            activeMaterial
                        },
                        onSelected = { material ->
                            if (plateId != null) {
                                isEmbossed = (material == MaterialType.EMBOSSED)
                            } else {
                                viewModel.setRegistrationMaterial(material)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            viewModel.clearRegistrationFields()
                            navigateToHome()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .addPressEffect {
                                    viewModel.clearRegistrationFields()
                                    navigateToHome()
                                }
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Home, null, tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .addPressEffect {
                                scope.launch {
                                    if (!uiState.exporting) showExportSheet = true
                                }
                            }
                            .clip(CircleShape)
                            .background(if (uiState.exporting) Color.Gray else Color(0xFF0C8A53)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.exporting) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.ic_download),
                                contentDescription = "Download",
                                tint = Color.White
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .addPressEffect { orderButton() }
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0C8A53)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Order",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Order Plate",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
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
                                model = frontImagePath,
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
                                model = backImagePath,
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
                        Divider(color = Color.Gray)

                        InfoRow(
                            label = "Vehicle Type",
                            value = plateData?.category ?: uiState.selectedVehicle?.name ?: "N/A"
                        )
                        Divider(color = Color.Gray)

                        InfoRow(
                            label = "Province",
                            value = plateData?.province ?: uiState.selectedProvince?.name ?: "N/A"
                        )
                        Divider(color = Color.Gray)

                        InfoRow(
                            label = "Issued",
                            value = plateData?.let { formatTimestamp(it.timestamp) }
                                ?: "20 May 2026"
                        )

                    }
                }

                if (isFromRegistration) {
                    SavedToRecentsIndicator()
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

            // ── OFFSCREEN EMBOSSED CANVASES ──────────────────────────────────────
            // requiredSize forces Compose to measure & draw them at real plate dimensions.
            // layout { ... place(-10000, -10000) } pushes them completely off-screen so
            // the user never sees them — but Compose still renders them, so the
            // GraphicsLayer records a real image that we can capture.
            // Only active for new plates (plateId == null) that haven't been saved yet.
            if (plateId == null && !uiState.isAlreadySaved) {

                if (frontConfig != null) {
                    // fillMaxWidth so PlateCanvas gets the same width as the visible plate,
                    // then aspectRatio inside PlateCanvas sizes the height correctly.
                    // layout{} reports 0×0 to parent so it takes no space on screen,
                    // but place(-10000,-10000) keeps it off-screen while still being drawn.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(0, 0) { placeable.place(-10000, -10000) }
                            }
                            .drawWithContent {
                                embossedFrontLayer.record { this@drawWithContent.drawContent() }
                                drawContent()
                                if (!embossedFrontReady) embossedFrontReady = true
                            }
                    ) {
                        PlateCanvas(
                            config = frontConfig.copy(materialType = MaterialType.EMBOSSED),
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        )
                    }
                }

                if (backConfig != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(constraints)
                                layout(0, 0) { placeable.place(-10000, -10000) }
                            }
                            .drawWithContent {
                                embossedBackLayer.record { this@drawWithContent.drawContent() }
                                drawContent()
                                if (!embossedBackReady) embossedBackReady = true
                            }
                    ) {
                        PlateCanvas(
                            config = backConfig.copy(materialType = MaterialType.EMBOSSED),
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        )
                    }
                }
            }
            // ────────────────────────────────────────────────────────────────────
        }
    }
    if (showExportSheet) {
        ModalBottomSheet(
            onDismissRequest = { showExportSheet = false }, sheetState = sheetState
        ) {
            ExportOptionsList { format ->
                showExportSheet = false
                scope.launch {
                    if (plateId != null && plateData != null) {
                        // History mode
                        val frontPath = plateData.plateImageRes ?: ""
                        val backPath = plateData.plateImageBackRes ?: ""
                        if (frontPath.isEmpty() || backPath.isEmpty()) {
                            snackbarHostState.showSnackbar("Image not found")
                            return@launch
                        }
                        viewModel.exportFromHistory(
                            frontPath = frontPath,
                            backPath = backPath,
                            format = format,
                            registrationNumber = plateData.plateNumber,
                            vehicleType = plateData.category
                        )
                    } else {
                        // New plate
                        val frontBytes = frontLayer.toImageBitmap().toByteArray(format)
                        val backBytes = backLayer.toImageBitmap().toByteArray(format)
                        viewModel.exportPlate(frontBytes, backBytes, format)
                    }
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

@Composable
fun SavedToRecentsIndicator(
    modifier: Modifier = Modifier
) {
    val successGreen = Color(0xFF4CAF50)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .border(
                width = 1.dp,
                color = successGreen.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            ),

        color = successGreen.copy(alpha = 0.12f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF1E7E34),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Saved to Recents",
                color = Color(0xFF1E7E34),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun PlateStyleToggle(
    selected: MaterialType,
    onSelected: (MaterialType) -> Unit
) {
    val activeBg = Color(0xFF0C8A53)
    val trackBg = MaterialTheme.colorScheme.surface
    val borderColor = Color(0xFFE5E2DA)

    Row(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(trackBg)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StyleSegment(
            label = "Painted",
            active = selected == MaterialType.PAINTED,
            activeBg = activeBg,
            onClick = { onSelected(MaterialType.PAINTED) }
        )
        StyleSegment(
            label = "Embossed",
            active = selected == MaterialType.EMBOSSED,
            activeBg = activeBg,
            onClick = { onSelected(MaterialType.EMBOSSED) }
        )
    }
}

@Composable
private fun StyleSegment(
    label: String,
    active: Boolean,
    activeBg: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(15.dp))
            .background(if (active) activeBg else Color.Transparent)
            .addPressEffect { onClick() }
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (active) Color.White else MaterialTheme.colorScheme.subtitleGray
        )
    }
}