package com.platepk.maker.ui.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.platepk.maker.domain.models.OrderUiState
import com.platepk.maker.domain.models.RecentPlateItem
import com.platepk.maker.ui.PlateViewModel
import com.platepk.maker.ui.canvas.PlateCanvas
import com.platepk.maker.ui.order.components.OrderStepTopAppBar
import com.platepk.maker.ui.theme.appBackground
import com.platepk.maker.util.addPressEffect

@Composable
fun ReviewOrderScreen(
    viewModel: PlateViewModel,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    plateId: String? = null,
) {
    val uiState by viewModel.uiState.collectAsState()
    val orderState = uiState.orderState
    val plateData = remember(plateId, uiState.savedPlates) {
        uiState.savedPlates.find { it.id == plateId }
    }
    LaunchedEffect(plateId) {
        plateId?.let { viewModel.loadHistoryPlateData(it) }
    }

    val isAllConfirmed = orderState.isRegistrationCorrect &&
            orderState.isTermsAgreed &&
            orderState.isNonRefundableUnderstood
    Scaffold(
        topBar = {
            OrderStepTopAppBar(
                title = "Review",
                currentStep = 3,
                totalSteps = 3,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            val isLoading = uiState.isSubmitting
            val buttonColor = if (isAllConfirmed) Color(0xFF0C8A53) else Color.LightGray
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clickable {
                            if (isAllConfirmed && !isLoading) {
                                viewModel.submitOrder(onNavigateSuccess = {
                                    onContinueClick()
                                })
                            }
                        }
                        .clip(RoundedCornerShape(12.dp))
                        .background(buttonColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isAllConfirmed) "Place Order" else "Confirm checks above",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.appBackground)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("Order summary", fontSize = 20.sp, fontWeight = FontWeight.Black)
                Text(
                    "Check everything before placing the order.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            item {
                OrderSummaryCard(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth(),
                    orderState = orderState,
                    plateData = plateData
                )
            }
            item {
                Text(
                    text = "DELIVER TO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                // Using the data from your ViewModel state
                DeliveryInfoCard(
                    fullName = orderState.fullName,
                    phone = orderState.phone,
                    email = orderState.email,
                    address = orderState.completeAddress,
                    town = orderState.area,
                    city = orderState.city,
                    province = orderState.province,
                    postalCode = orderState.postalCode

                )
            }
            item {
                Text(
                    text = "PRICING",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                PricingSection(orderState = orderState)
            }
            item {
                Text(
                    text = "CONFIRM",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                CheckboxRow(
                    text = "I confirm the registration number is correct.",
                    checked = orderState.isRegistrationCorrect,
                    onCheckedChange = { viewModel.updateConfirmStatus(isRegCorrect = it) }
                )
                CheckboxRow(
                    text = "I agree to the terms of service.",
                    checked = orderState.isTermsAgreed,
                    onCheckedChange = { viewModel.updateConfirmStatus(isTermsAgreed = it) }
                )
                CheckboxRow(
                    text = "I understand this is custom-made — non-refundable once production starts.",
                    checked = orderState.isNonRefundableUnderstood,
                    onCheckedChange = { viewModel.updateConfirmStatus(isNonRefundable = it) }
                )
            }
        }

    }
}


@Composable
fun OrderSummaryCard(
    viewModel: PlateViewModel,
    modifier: Modifier = Modifier,
    orderState: OrderUiState,
    plateData: RecentPlateItem?
) {
    val uiState by viewModel.uiState.collectAsState()

    // Check karein ke data History se aa raha hai ya New flow se
    val isHistoryMode = uiState.frontImagePath != null || uiState.backImagePath != null

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            // Visuals Column (Front + Back)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                // Front Plate
                Box(
                    Modifier
                        .width(100.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Transparent)
                ) {
                    if (isHistoryMode) {
                        AsyncImage(
                            model = uiState.frontImagePath,
                            contentDescription = "Front",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        uiState.frontPlate?.config?.let { PlateCanvas(config = it) }
                    }
                }

                Box(
                    Modifier
                        .width(100.dp)
                        .wrapContentHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Transparent)
                ) {
                    if (isHistoryMode) {
                        AsyncImage(
                            model = uiState.backImagePath,
                            contentDescription = "Back",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        uiState.backPlate?.config?.let { PlateCanvas(config = it) }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Details Column
            Column {
                Text(
                    text = "REGISTRATION",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                val registrationDisplay = orderState.vehicleName.ifBlank { uiState.registrationNumber }
                // Dynamic Registration Number
                Text(
                    text = registrationDisplay,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )

                // Dynamic Plate Type
                Text(
                    text = orderState.selectedPlateType?.title ?: "Standard Plate",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                // Dynamic Quantity (Logic: Front + Back plate)
                Text(
                    text = "Quantity: 2 (Front + Back)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                val addOnsText = uiState.orderState.selectedAddsOns.joinToString(", ") { it.title }
                Text(
                    text = "Add-ons: ${if (addOnsText.isEmpty()) "None" else addOnsText}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DeliveryInfoCard(
    fullName: String,
    phone: String,
    email: String?,      // Optional
    address: String,
    city: String,
    province: String,
    town: String?,       // Optional
    postalCode: String?  // Optional
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.3f
            )
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Section
            Text(text = fullName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = phone, fontSize = 14.sp, color = Color.Gray)

            // Email (agar provided ho)
            if (!email.isNullOrBlank()) {
                Text(text = email, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Address Section
            Text(
                text = "Address",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )

            val fullAddress = buildString {
                append(address)
                if (!town.isNullOrBlank()) append(", $town")
                append(", $city, $province")
                if (!postalCode.isNullOrBlank()) append(" · $postalCode")
            }

            Text(
                text = fullAddress,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun PricingSection(orderState: OrderUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            PricingRow("Plate (pair)", "Rs. ${orderState.basePrice}")
            PricingRow("Add-ons", "Rs. ${orderState.addOnsPrice}")
            PricingRow("Delivery", "Rs. ${orderState.shippingPrice}")

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "Rs. ${orderState.totalPrice}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0C8A53)
                )
            }
        }
    }
}

@Composable
fun PricingRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun CheckboxRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .addPressEffect { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(24.dp).padding(end = 8.dp)
        )
        Text(text = text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
    }
}