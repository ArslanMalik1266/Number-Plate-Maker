package com.webscare.numberplatemaker.ui

import androidx.lifecycle.ViewModel
import com.webscare.numberplatemaker.domain.models.PlateDimensions
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.domain.models.PlateUiState
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.usecases.GeneratePlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateStylingUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlateViewModel(
    private val generatePlateUseCase: GeneratePlateUseCase,
   private val getPlateStylingUseCase: GetPlateStylingUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlateUiState())
    val uiState: StateFlow<PlateUiState> = _uiState.asStateFlow()

    // --- UI Events (User Actions) ---

    fun onVehicleSelected(vehicle: VehicleType) {
        _uiState.update {
            it.copy(
                selectedVehicle = vehicle,
                currentStep = PlateStep.ProvinceSelection
            )
        }
    }
    fun onProvinceSelected(province: Province) {
        _uiState.update {
            it.copy(
                selectedProvince = province,
                currentStep = PlateStep.InputNumber
            )
        }
    }
    fun onRegistrationNumberChanged(number: String) {
        _uiState.update { it.copy(registrationNumber = number) }
    }

    fun onGeneratePlate() {
        _uiState.update { state ->
            val vehicle = state.selectedVehicle ?: VehicleType.PRIVATE_CAR
            val province = state.selectedProvince ?: Province.PUNJAB

            // UseCase ko call karke full PlateModel lein (jis mein colors, borders sab hon)
            // Default side hum FRONT rakhte hain preview start karne ke liye
            val generatedPlate = generatePlateUseCase(
                vehicleType = vehicle,
                province = province,
                side = PlateSide.FRONT,
                registrationNumber = state.registrationNumber
            )

            state.copy(
                currentStep = PlateStep.Preview,
                finalPlate = generatedPlate,
            )
        }
    }
    fun getPlateForSide(side: PlateSide): PlateModel? {
        val state = _uiState.value
        val vehicle = state.selectedVehicle ?: return null
        val province = state.selectedProvince ?: return null

        return generatePlateUseCase(
            vehicleType = vehicle,
            province = province,
            side = side,
            registrationNumber = state.registrationNumber
        )
    }
    fun navigateBack() {
        val previousStep = when (_uiState.value.currentStep) {
            is PlateStep.ProvinceSelection -> PlateStep.VehicleSelection
            is PlateStep.InputNumber -> PlateStep.ProvinceSelection
            is PlateStep.Preview -> PlateStep.InputNumber
            else -> PlateStep.VehicleSelection
        }
        _uiState.update { it.copy(currentStep = previousStep) }
    }
    fun resetFlow() {
        _uiState.value = PlateUiState()
    }

}