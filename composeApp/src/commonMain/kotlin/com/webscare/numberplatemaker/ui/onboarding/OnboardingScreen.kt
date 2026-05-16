package com.webscare.numberplatemaker.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.ui.PlateViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingScreen() {
    val viewModel: PlateViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Ultra-Senior: AnimatedContent adds a professional 'App' feel
        AnimatedContent(
            targetState = uiState.currentStep,
            transitionSpec = {
                // Agar hum agay ja rahe hain toh right se slide, wapsi par left se
                if (isForwardNavigation(initialState, targetState)) {
                    (slideInHorizontally(animationSpec = tween(400)) { it } + fadeIn()) togetherWith
                            slideOutHorizontally(animationSpec = tween(400)) { -it } + fadeOut()
                } else {
                    (slideInHorizontally(animationSpec = tween(400)) { -it } + fadeIn()) togetherWith
                            slideOutHorizontally(animationSpec = tween(400)) { it } + fadeOut()
                }
            },
            label = "StepTransition"
        ) { targetStep ->
            when (targetStep) {
                is PlateStep.VehicleSelection -> {
                    VehicleSelectionStep(
                        viewModel = viewModel,
                        onVehicleSelected = { vehicle ->
                            viewModel.onVehicleSelected(vehicle)
                        }
                    )
                }

                is PlateStep.ProvinceSelection -> {
                    ProvinceSelectionStep(
                        viewModel = viewModel,
                        onProvinceSelected = { province ->
                            viewModel.onProvinceSelected(province)
                        },
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is PlateStep.InputNumber -> {
                    InputNumberStep(
                        registrationNumber = uiState.registrationNumber,
                        onNumberChange = { viewModel.onRegistrationNumberChanged(it) },
                        onBack = { viewModel.navigateBack() },
                        onGenerate = { viewModel.generatePlatePreview(uiState.registrationNumber) },
                        viewModel = viewModel
                    )
                }

                is PlateStep.Preview -> {
                    ResultPreviewStep(
                        plate = uiState.frontPlate,
                        viewModel = viewModel,
                        onReset = { viewModel.resetFlow() }
                    )
                }
            }
        }
    }
}

/**
 * Helper function to determine navigation direction for animations
 */
private fun isForwardNavigation(initial: PlateStep, target: PlateStep): Boolean {
    val stepOrder = listOf(
        PlateStep.VehicleSelection::class,
        PlateStep.ProvinceSelection::class,
        PlateStep.InputNumber::class,
        PlateStep.Preview::class
    )
    val initialIdx = stepOrder.indexOf(initial::class)
    val targetIdx = stepOrder.indexOf(target::class)
    return targetIdx > initialIdx
}