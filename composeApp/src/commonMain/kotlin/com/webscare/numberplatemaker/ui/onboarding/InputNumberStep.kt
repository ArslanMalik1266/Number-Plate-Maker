package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.webscare.numberplatemaker.ui.PlateViewModel

@Composable
fun InputNumberStep(
    registrationNumber: String,
    onNumberChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onBack: () -> Unit,
    viewModel: PlateViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val config = uiState.plateInputConfig

    val textFieldValue = TextFieldValue(
        text = registrationNumber,
        selection = TextRange(registrationNumber.length) // Hamesha text ke end par cursor rakho
    )
    val shouldShowNumberKeyboard = registrationNumber.endsWith(" ") ||
            registrationNumber.any { it.isDigit() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. Top Navigation ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- 2. Header Section ---
        Text(
            text = "Registration Details",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        )

        Text(
            text = "Enter your vehicle's registration number",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- 3. Input Field ---
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                // Sirf text ViewModel ko bhejo, formatting wahan se hogi
                onNumberChange(newValue.text)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (shouldShowNumberKeyboard) {
                    KeyboardType.Number
                } else {
                    KeyboardType.Text
                },  capitalization = KeyboardCapitalization.Characters
            ),
            label = { Text("Plate Number") },
            placeholder = { Text("e.g. LEA-1234") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp // Gives a plate-like feel during typing
            ),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        Text(
            text = "Format: ${uiState.formatHint.ifEmpty { "ABC 1234" }}",
            modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- 4. Final Action Button ---
        Button(
            onClick = onGenerate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(60.dp),
            // Requirement: At least 3 chars to enable
            enabled = registrationNumber.isNotBlank() && registrationNumber.length >= 3,
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                "Generate Plate",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}