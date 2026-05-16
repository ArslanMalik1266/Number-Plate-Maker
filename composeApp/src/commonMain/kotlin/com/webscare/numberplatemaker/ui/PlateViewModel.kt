package com.webscare.numberplatemaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.models.PlateInputConfig
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.domain.models.PlateUiState
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.usecases.EnforcePlateInputUseCase
import com.webscare.numberplatemaker.domain.usecases.ExportPlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateInputConfigUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlateViewModel(
    private val getPlateConfigUseCase: GetPlateConfigUseCase,
    private val exportPlateUseCase: ExportPlateUseCase,
    private val getPlateInputConfigUseCase: GetPlateInputConfigUseCase,
    private val enforcePlateInputUseCase: EnforcePlateInputUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlateUiState())
    val uiState: StateFlow<PlateUiState> = _uiState.asStateFlow()
    private val currentConfig: PlateInputConfig?
        get() {
            val province = _uiState.value.selectedProvince ?: return null
            val vehicle = _uiState.value.selectedVehicle ?: return null
            return getPlateInputConfigUseCase(province, vehicle).also { config ->
                _uiState.update { it.copy(formatHint = config.formatHint) }
            }
        }


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
        val config = getPlateInputConfigUseCase(province, _uiState.value.selectedVehicle!!)
        _uiState.update {
            it.copy(
                selectedProvince = province,
                currentStep = PlateStep.InputNumber,
                plateInputConfig = config,
                formatHint = config.formatHint
            )
        }
    }
    fun onRegistrationNumberChanged(raw: String) {
        val config = currentConfig
        val minLetters = config?.minLetterCount ?: 2
        val maxLetters = config?.maxLetterCount ?: 4
        val maxNumbers = config?.maxNumberCount ?: 4
        val letters = raw.filter { it.isLetter() }.uppercase().take(maxLetters)
        val numbers = raw.filter { it.isDigit() }.take(maxNumbers)
        val enforced = when {
            letters.length < minLetters -> letters
            numbers.isNotEmpty() -> "$letters $numbers"
            letters.length >= minLetters -> {
                if (raw.length > letters.length || raw.endsWith(" ")) {
                    "$letters "
                } else {
                    letters
                }
            }
            else -> letters
        }
        _uiState.update { it.copy(registrationNumber = enforced) }
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

            val currentState = _uiState.value
            val regNo = currentState.registrationNumber
            val vehicleType = currentState.selectedVehicle?.name ?: "VEHICLE"
            val result = exportPlateUseCase(
                frontImageData = frontImageData,
                backImageData = backImageData,
                format = format,
                registrationNumber = regNo,
                vehicleType = vehicleType
            )

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

    fun onLetterInputChanged(raw: String) {
        val config = currentConfig ?: return
        _uiState.update {
            it.copy(letterInput = enforcePlateInputUseCase.enforceLetters(raw, config))
        }
    }

    fun onNumberInputChanged(raw: String) {
        val config = currentConfig ?: return
        _uiState.update {
            it.copy(numberInput = enforcePlateInputUseCase.enforceNumbers(raw, config))
        }
    }
}