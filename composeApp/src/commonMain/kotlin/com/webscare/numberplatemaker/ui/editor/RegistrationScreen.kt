package com.webscare.numberplatemaker.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.domain.models.PlateConfig // Sahi Import
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.ui.canvas.PlateCanvas
import com.webscare.numberplatemaker.ui.editor.components.EditorStepTopAppBar
import com.webscare.numberplatemaker.ui.theme.PlateColors
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun RegistrationScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    onGenerateClick: (String) -> Unit,
    registrationNumber: String,
    onNumberChange: (String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val registrationNumber = uiState.registrationNumber
    var textFieldValue by remember { mutableStateOf(TextFieldValue(registrationNumber)) }
    LaunchedEffect(registrationNumber) {
        if (textFieldValue.text != registrationNumber) {
            textFieldValue =
                TextFieldValue(registrationNumber, TextRange(registrationNumber.length))
        }
    }
    val isButtonEnabled = viewModel.isGenerateButtonEnabled()
    val frontConfig = uiState.frontPlate?.config
        ?: viewModel.getProvinceDefaultConfig(uiState.selectedProvince, PlateSide.FRONT)

    val backConfig = uiState.backPlate?.config
        ?: viewModel.getProvinceDefaultConfig(uiState.selectedProvince, PlateSide.BACK)
    val shouldShowNumberKeyboard =
        registrationNumber.endsWith(" ") || registrationNumber.any { it.isDigit() }

    Scaffold(
        topBar = {
            EditorStepTopAppBar(
                title = "Registration", currentStep = 3, totalSteps = 3, onBackClick = onBackClick
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .background(PlateColors.AppBackground).padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Enter plate number",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PlateColors.SoftBlack,
                lineHeight = 32.sp
            )
            Text(
                "Letters and digits — the app will format and uppercase automatically.",
                color = PlateColors.SubtitleGray,
                fontSize = 14.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Summary Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE5E2DA), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                InfoBox(
                    label = "VEHICLE",
                    value = uiState.selectedVehicle?.name ?: "N/A",
                    modifier = Modifier.weight(1f) // Equal weight
                )
                InfoBox(
                    label = "PROVINCE",
                    value = uiState.selectedProvince?.name ?: "N/A",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Input
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    onNumberChange(newValue.text)
                    viewModel.updatePreview(newValue.text)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (shouldShowNumberKeyboard) KeyboardType.Number else KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters
                ),
                label = { Text("Plate Number") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.5f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF0F0F0))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                frontConfig?.let { config ->
                    Text(
                        text = "Front Plate",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PlateCanvas(
                            config = config.copy(registrationNumber = registrationNumber.ifBlank { "" }),
                            modifier = Modifier.fillMaxWidth().height(100.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .addPressEffect { onGenerateClick(registrationNumber) }.height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isButtonEnabled) Color(0xFF0C8A53) else Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Generate Plate",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isButtonEnabled) Color.White else Color.LightGray
                )
            }
        }
    }
}

@Composable
fun InfoBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier // Modifier add kiya
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontSize = 10.sp,
            color = PlateColors.SubtitleGray,
            fontWeight = FontWeight.Bold
        )
        Text(value, fontWeight = FontWeight.Bold, color = PlateColors.SoftBlack)
    }
}