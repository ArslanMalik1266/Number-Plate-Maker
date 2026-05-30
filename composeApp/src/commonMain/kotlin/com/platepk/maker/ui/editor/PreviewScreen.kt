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
import kotlinx.coroutines.delay
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
    val plateData = remember(plateId, uiState.savedPlates) {
        uiState.savedPlates.find { it.id == plateId }
    }
    println("DEBUG_PREVIEW: plateId = $plateId")
    println("DEBUG_PREVIEW: plateData.frontImageRes = ${plateData?.plateImageRes}")
    println("DEBUG_PREVIEW: plateData.backImageRes = ${plateData?.plateImageBackRes}")

    val frontConfig = uiState.frontPlate?.config
    val backConfig = uiState.backPlate?.config

    var showIndicator by remember { mutableStateOf(isFromRegistration) }

    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            viewModel.resetExportState()
        }
    }

    LaunchedEffect(uiState.frontPlate, uiState.backPlate) {
        if (plateId == null && uiState.frontPlate != null && uiState.backPlate != null) {
            scope.launch {
                try {
                    delay(500)
                    val frontBitmap = frontLayer.toImageBitmap()
                    val backBitmap = backLayer.toImageBitmap()
                    val frontBytes = frontBitmap.toByteArray(ExportFormat.PNG)
                    val backBytes = backBitmap.toByteArray(ExportFormat.PNG)
                    viewModel.savePlateImages(frontBytes, backBytes)
                } catch (e: Exception) {
                    println("DEBUG_CAPTURE_ERROR: ${e.message}")
                }
            }
        }
    }

    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Your Plate",
                currentStep = 3,
                totalSteps = 3,
                onBackClick = onBackClick,
                showSteps = false,
                actions = {
                    // Painted / Embossed style toggle.
                    // Reads the live material from whichever plate is on screen
                    // (front is the source of truth — back is kept in sync by the VM).
                    val activeMaterial = uiState.frontPlate?.config?.materialType
                        ?: uiState.orderState.selectedMaterial
                    PlateStyleToggle(
                        selected = activeMaterial,
                        onSelected = { viewModel.setRegistrationMaterial(it) }
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
                                .clip(CircleShape)
                            ,
                            contentAlignment = Alignment.Center
                        ){
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
                                    delay(150)
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
/**
 * Compact segmented control for the Preview toolbar.
 * Lets the user flip the rendered plate between PAINTED (flat ink) and
 * EMBOSSED (raised letter look) in one tap.
 *
 * Sits to the LEFT of the Home button so the eye reads:
 *   [ Painted | Embossed ]  ( Home )
 */
@Composable
private fun PlateStyleToggle(
    selected: MaterialType,
    onSelected: (MaterialType) -> Unit
) {
    val activeBg = Color(0xFF0C8A53)        // brand green, used elsewhere on this screen
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
