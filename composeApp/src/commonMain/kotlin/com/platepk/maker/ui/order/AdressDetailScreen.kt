package com.platepk.maker.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.platepk.maker.domain.models.ShippingMethod
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.order.components.OrderStepTopAppBar
import com.platepk.maker.ui.order.components.PhoneVisualTransformation
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.ui.theme.redColor
import com.platepk.maker.ui.theme.softBlack
import com.platepk.maker.ui.theme.subtitleGray
import com.platepk.maker.util.addPressEffect

@Composable
fun AddressDetailScreen(
    viewModel: PlateViewModel, onBackClick: () -> Unit, onContinueClick: () -> Unit, plateId: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val plateData = remember(plateId, uiState.savedPlates) {
        uiState.savedPlates.find { it.id == plateId }
    }
    val orderState = uiState.orderState
    val showErrors = orderState.showValidationErrors
    val focusManager = LocalFocusManager.current

    LaunchedEffect(orderState.availableShippingMethods) {
        if (orderState.selectedShippingMethod == null && orderState.availableShippingMethods.isNotEmpty()) {
            val standard = orderState.availableShippingMethods.find { it.title == "Standard Shipping" }
            standard?.let { viewModel.onShippingMethodSelected(it) }
        }
    }
    LaunchedEffect(plateId) {
        plateId?.let { viewModel.loadHistoryPlateData(it) }
    }
    val registrationDisplay = orderState.vehicleName.ifBlank { uiState.registrationNumber }
    val categoryDisplay = orderState.vehicleCategory.ifBlank { uiState.selectedVehicle?.name ?: "" }
    val provinceDisplay = orderState.province.ifBlank { uiState.selectedProvince?.name ?: "" }

    Scaffold(topBar = {
        OrderStepTopAppBar(
            title = "Delivery", currentStep = 2, totalSteps = 3, onBackClick = onBackClick
        )
    }, bottomBar = {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(56.dp)
                    .addPressEffect {
                        viewModel.validateAndContinue(
                            onSuccess = { onContinueClick() }
                        )
                    }.clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0C8A53)), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Review Order",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding).fillMaxSize()
                .background(MaterialTheme.colorScheme.appBackground).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Where should we send it?",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.softBlack
                )
                Text(
                    text = "Plates are dispatched once registration is verified.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.subtitleGray
                )
            }

            item {
                FieldLabel("FULL NAME *")
                CustomTextField(
                    orderState.fullName,
                    placeholder = "Enter your full name",
                    isError = showErrors && orderState.fullName.isBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                ) {
                    viewModel.updateAddressDetails(
                        orderState.copy(
                            fullName = it
                        )
                    )
                }
            }

            item {
                FieldLabel("PHONE NUMBER *")
                CustomTextField(
                    value = orderState.phone,
                    "3XX XXXX XXX",
                    isError = showErrors && (orderState.phone.length != 10),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    }),
                    visualTransformation = PhoneVisualTransformation(),
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        if (filtered.length <= 10) {
                            val clean = if (filtered.startsWith("0")) filtered.drop(1) else filtered
                            viewModel.updateAddressDetails(orderState.copy(phone = clean))
                        }
                    }
                )
            }
            item {
                FieldLabel("EMAIL")
                CustomTextField(
                    orderState.email, placeholder = "e.g. name@example.com",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                ) {
                    viewModel.updateAddressDetails(
                        orderState.copy(email = it)
                    )
                }
            }

            item {
                Text(
                    "DELIVERY ADDRESS",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            item {
                FieldLabel("PROVINCE *")
                ProvinceSpinner(
                    selectedProvince = orderState.province,
                    onProvinceSelected = { viewModel.updateAddressDetails(orderState.copy(province = it)) }
                )
            }

            item {
                FieldLabel("CITY *")
                CustomTextField(
                    orderState.city, placeholder = "Enter city name",
                    isError = showErrors && orderState.city.isBlank(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                ) {
                    viewModel.updateAddressDetails(
                        orderState.copy(city = it)
                    )
                }
            }

            item {
                FieldLabel("AREA / TOWN")
                CustomTextField(
                    orderState.area,
                    placeholder = "Enter area or sector",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                ) {
                    viewModel.updateAddressDetails(
                        orderState.copy(
                            area = it
                        )
                    )
                }
            }

            item {
                FieldLabel("COMPLETE ADDRESS *")
                CustomTextField(
                    orderState.completeAddress,
                    "House #, street, block / sector, landmark",
                    isError = showErrors && orderState.completeAddress.isBlank(),
                    minLines = 3
                ) { viewModel.updateAddressDetails(orderState.copy(completeAddress = it)) }
            }
            item {
                FieldLabel("POSTAL CODE")
                CustomTextField(
                    value = orderState.postalCode,
                    placeholder = "e.g. 54000",
                    onValueChange = { viewModel.updateAddressDetails(orderState.copy(postalCode = it)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
            item {
                FieldLabel("SHIPPING METHOD *")
            }

            items(orderState.availableShippingMethods) { method ->
                DeliveryOptionCard(
                    title = method.title,
                    subtitle = "Delivery within ${method.deliveryTime} days",
                    isSelected = orderState.selectedShippingMethod?.id == method.id,
                    onClick = { viewModel.onShippingMethodSelected(method) }
                )

            }

            item {
                Text(
                    text = "VEHICLE INFO - AUTO-FILLED",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                // Background box container
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    ReadOnlyInfoRow("Registration", registrationDisplay)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    ReadOnlyInfoRow("Vehicle type", categoryDisplay)
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = Color.LightGray.copy(alpha = 0.3f)
                    )
                    ReadOnlyInfoRow("Province", provinceDisplay)
                }
            }
        }

    }
}

@Composable
fun FieldLabel(text: String) {
    val annotatedString = buildAnnotatedString {
        // Pura text likhen
        append(text)

        // Agar text mein '*' hai, toh usay red color dein
        if (text.contains("*")) {
            val startIndex = text.indexOf("*")
            addStyle(
                style = SpanStyle(color = MaterialTheme.colorScheme.redColor),
                start = startIndex,
                end = startIndex + 1
            )
        }
    }

    Text(
        text = annotatedString,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun CustomTextField(
    value: String,
    placeholder: String = "",
    minLines: Int = 1,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        minLines = minLines,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun DeliveryOptionCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Color(0xFF0C8A53) else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .addPressEffect { onClick() }
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.softBlack)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.subtitleGray)
        }
    }
}

@Composable
fun ReadOnlyInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.subtitleGray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.softBlack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceSpinner(
    selectedProvince: String,
    onProvinceSelected: (String) -> Unit
) {
    val provinces = listOf(
        "Punjab", "Sindh", "Khyber Pakhtunkhwa", "Balochistan",
        "Islamabad", "Azad Kashmir", "Gilgit-Baltistan"
    )
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedProvince,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select your province", fontSize = 14.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            // Important: enabled = false krne se ye field input nahi legi
            // aur hum puray container pe click handle kar saken ge
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledIndicatorColor = Color.Transparent,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface
            )
        )

        // Yeh Box TextField ke bilkul uper baithe ga aur click catch kare ga
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        // Dropdown Menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(12.dp))
        ) {
            provinces.forEach { province ->
                DropdownMenuItem(
                    text = { Text(province) },
                    onClick = {
                        onProvinceSelected(province)
                        expanded = false
                    }
                )
            }
        }
    }
}