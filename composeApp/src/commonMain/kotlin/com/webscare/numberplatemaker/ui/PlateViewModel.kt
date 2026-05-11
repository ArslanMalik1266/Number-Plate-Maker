package com.webscare.numberplatemaker.ui

import androidx.lifecycle.ViewModel
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.domain.models.PlateUiState
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlateViewModel(
    private val getPlateConfigUseCase: GetPlateConfigUseCase
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

    fun generatePlatePreview(regNumber: String) {
        val currentState = _uiState.value
        val vehicle = currentState.selectedVehicle
        val province = currentState.selectedProvince

        if (vehicle != null && province != null && regNumber.isNotBlank()) {
            _uiState.update { it.copy(loading = true) }

            // UseCase se data bundle mangwaya
            val previewData = getPlateConfigUseCase(
                vehicleType = vehicle,
                province = province,
                regNumber = regNumber
            )

            // State mein Front aur Back dono ko alag save kiya
            _uiState.update {
                it.copy(
                    registrationNumber = regNumber,
                    frontPlate = PlateModel(
                        side = PlateSide.FRONT,
                        config = previewData.frontPlate
                    ),
                    backPlate = PlateModel(
                        side = PlateSide.BACK,
                        config = previewData.backPlate
                    ),
                    currentStep = PlateStep.Preview,
                    loading = false
                )
            }
        }
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