package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.PlateViewModel
import com.webscare.numberplatemaker.util.addPressEffect

@Composable
fun InputNumberStep(
    registrationNumber: String,
    onNumberChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onBack: () -> Unit,
    viewModel: PlateViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val textFieldValue = TextFieldValue(
        text = registrationNumber,
        selection = TextRange(registrationNumber.length) // Hamesha text ke end par cursor rakho
    )
    val shouldShowNumberKeyboard = registrationNumber.endsWith(" ") ||
            registrationNumber.any { it.isDigit() }
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6F1)) // Same Premium Screen Background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(horizontal = 24.dp),
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
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color(0xFF1A1A1A)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. Header Section ---
            Text(
                text = "Registration Details",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 30.sp,
                    letterSpacing = (-0.5).sp,
                    color = Color(0xFF1A1A1A)
                )
            )

            Text(
                text = "Enter your vehicle's registration number",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF888880),
                    fontSize = 14.sp
                ),
                modifier = Modifier.padding(top = 6.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- 3. Input Field ---
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    onNumberChange(newValue.text)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (shouldShowNumberKeyboard) {
                        KeyboardType.Number
                    } else {
                        KeyboardType.Text
                    },
                    capitalization = KeyboardCapitalization.Characters
                ),
                label = { Text("Plate Number", color = Color(0xFF888880)) },
                placeholder = { Text("e.g. LEA 1234", color = Color(0xFFB5B5B0)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = 2.sp // Gives a plate-like feel during typing
                ),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1A1A1A),
                    unfocusedBorderColor = Color(0xFFE5E2DA),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF1A1A1A)
                )
            )

            Text(
                text = "Format: ${uiState.formatHint.ifEmpty { "ABC 1234" }}",
                modifier = Modifier.padding(top = 12.dp, start = 4.dp).fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    color = Color(0xFF888880),
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(40.dp))

            // --- 4. Final Action Button ---
            val isButtonEnabled = registrationNumber.isNotBlank() && registrationNumber.length >= 3

            Button(
                onClick = onGenerate,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .height(60.dp)
                    .then(if (isButtonEnabled) Modifier.addPressEffect {} else Modifier), // Enabled state press feedback loop
                enabled = isButtonEnabled,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A1A1A),       // Premium black when active
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFFE5E2DA), // Disables smoothly with background palette
                    disabledContentColor = Color(0xFFB5B5B0)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isButtonEnabled) 4.dp else 0.dp
                )
            ) {
                Text(
                    "Generate Plate",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }
    }
}