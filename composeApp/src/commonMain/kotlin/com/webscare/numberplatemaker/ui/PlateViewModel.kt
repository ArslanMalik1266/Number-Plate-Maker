package com.webscare.numberplatemaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.domain.models.PlateUiState
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.usecases.ExportPlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlateViewModel(
    private val getPlateConfigUseCase: GetPlateConfigUseCase,
    private val exportPlateUseCase: ExportPlateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlateUiState())
    val uiState: StateFlow<PlateUiState> = _uiState.asStateFlow()

    // --- UI Events (User Actions) ---

    fun onVehicleSelected(vehicle: VehicleType) {
        val nextStep = if (vehicle == VehicleType.DIPLOMATIC) {
            PlateStep.InputNumber
        } else {
            PlateStep.ProvinceSelection
        }
        _uiState.update {
            it.copy(
                selectedVehicle = vehicle,
                currentStep = nextStep,
                selectedProvince = if (vehicle == VehicleType.DIPLOMATIC) Province.ISLAMABAD else it.selectedProvince
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

    fun exportPlate(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(exporting = true) }
            val result = exportPlateUseCase(frontImageData, backImageData, format)
            _uiState.update {
                it.copy(
                    exporting = false,
                    exportSuccess = result.isSuccess,
                    exportError = result.exceptionOrNull()?.message
                )
            }
        }
    }


    fun resetExportState() {
        _uiState.update { it.copy(exportSuccess = false, exportError = null) }
    }
    fun navigateBack() {
        val currentState = _uiState.value
        val previousStep = when (currentState.currentStep) {
            is PlateStep.ProvinceSelection -> PlateStep.VehicleSelection
            is PlateStep.InputNumber -> {
                // Agar Diplomatic tha to wapis Vehicle selection pr jao, warna Province pr
                if (currentState.selectedVehicle == VehicleType.DIPLOMATIC) PlateStep.VehicleSelection
                else PlateStep.ProvinceSelection
            }
            is PlateStep.Preview -> PlateStep.InputNumber
            else -> PlateStep.VehicleSelection
        }
        _uiState.update { it.copy(currentStep = previousStep) }
    }
    fun resetFlow() {
        _uiState.value = PlateUiState()
    }

}