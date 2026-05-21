package com.webscare.numberplatemaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.webscare.numberplatemaker.data.local.entity.PlateEntity
import com.webscare.numberplatemaker.domain.models.ExportFormat
import com.webscare.numberplatemaker.domain.models.PlateConfig
import com.webscare.numberplatemaker.domain.models.PlateInputConfig
import com.webscare.numberplatemaker.domain.models.PlateModel
import com.webscare.numberplatemaker.domain.models.PlateSide
import com.webscare.numberplatemaker.domain.models.PlateStep
import com.webscare.numberplatemaker.domain.models.PlateUiState
import com.webscare.numberplatemaker.domain.models.Province
import com.webscare.numberplatemaker.domain.models.RecentPlateItem
import com.webscare.numberplatemaker.domain.models.VehicleType
import com.webscare.numberplatemaker.domain.usecases.DeleteAllPlatesUseCase
import com.webscare.numberplatemaker.domain.usecases.DeletePlateUseCase
import com.webscare.numberplatemaker.domain.usecases.EnforcePlateInputUseCase
import com.webscare.numberplatemaker.domain.usecases.ExportPlateUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlateInputConfigUseCase
import com.webscare.numberplatemaker.domain.usecases.GetPlatesUseCase
import com.webscare.numberplatemaker.domain.usecases.SavePlateUseCase
import com.webscare.numberplatemaker.mapper.toDomain
import com.webscare.numberplatemaker.mapper.toEntity
import com.webscare.numberplatemaker.util.ExportResult
import com.webscare.numberplatemaker.util.PlatformClock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class PlateViewModel(
    private val clock: PlatformClock,
    private val getPlateConfigUseCase: GetPlateConfigUseCase,
    private val exportPlateUseCase: ExportPlateUseCase,
    private val getPlateInputConfigUseCase: GetPlateInputConfigUseCase,
    private val enforcePlateInputUseCase: EnforcePlateInputUseCase,
    private val getPlatesUseCase: GetPlatesUseCase,
    private val savePlateUseCase: SavePlateUseCase,
    private val deletePlateUseCase: DeletePlateUseCase,
    private val deleteAllPlatesUseCase: DeleteAllPlatesUseCase
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


    fun savePlate(plate: PlateEntity) {
        viewModelScope.launch {
            savePlateUseCase(plate)
        }
    }

    // 3. Single plate delete karne ke liye
    fun deletePlate(plate: PlateEntity) {
        viewModelScope.launch {
            deletePlateUseCase(plate)
        }
    }

    // 4. Sab kuch clear karne ke liye
    fun clearAllPlates() {
        viewModelScope.launch {
            deleteAllPlatesUseCase()
        }
    }

    init {
        viewModelScope.launch {
            getPlatesUseCase().collect { entities ->
                val domainModels = entities.map { it.toDomain() }
                _uiState.update { it.copy(savedPlates = domainModels) }
            }
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
                selectedProvince = if (vehicle == VehicleType.DIPLOMATIC) Province.ISLAMABAD else it.selectedProvince,
                frontPlate = null,
                backPlate = null
            )
        }
    }
    fun updatePreview(newNumber: String) {
        _uiState.update { currentState ->
            val currentFront = currentState.frontPlate ?: return@update currentState
            val updatedConfig = currentFront.config.copy(
                registrationNumber = newNumber
            )
            currentState.copy(
                registrationNumber = newNumber,
                frontPlate = currentFront.copy(config = updatedConfig)
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
                formatHint = config.formatHint,
                frontPlate = null,
                backPlate = null
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
    fun getProvinceDefaultConfig(province: Province?, side: PlateSide): PlateConfig? {
        if (province == null) return null
        val vehicle = _uiState.value.selectedVehicle ?: VehicleType.PRIVATE_CAR

        val previewData = getPlateConfigUseCase(
            vehicleType = vehicle,
            province = province,
            regNumber = "AAA-0000"
        )
        return when(side) {
            PlateSide.FRONT -> previewData.frontPlate
            PlateSide.BACK -> previewData.backPlate
        }
    }
    // PlateViewModel.kt
    fun isGenerateButtonEnabled(): Boolean {
        val state = _uiState.value
        val config = currentConfig ?: return false

        val regNumber = state.registrationNumber
        val letters = regNumber.filter { it.isLetter() }
        val numbers = regNumber.filter { it.isDigit() }

        // Validation Logic
        val isLettersValid = letters.length in config.minLetterCount..config.maxLetterCount
        val isNumbersValid = numbers.length in config.minNumberCount..config.maxNumberCount

        return isLettersValid && isNumbersValid
    }
    fun generatePlatePreview(regNumber: String, onComplete: () -> Unit) {
        val currentState = _uiState.value
        val vehicle = currentState.selectedVehicle
        val province = currentState.selectedProvince

        if (vehicle != null && province != null && regNumber.isNotBlank()) {
            _uiState.update { it.copy(loading = true) }

            val previewData = getPlateConfigUseCase(
                vehicleType = vehicle,
                province = province,
                regNumber = regNumber
            )

            _uiState.update {
                it.copy(
                    registrationNumber = regNumber,
                    frontPlate = PlateModel(PlateSide.FRONT, previewData.frontPlate),
                    backPlate = PlateModel(PlateSide.BACK, previewData.backPlate),
                    currentStep = PlateStep.Preview,
                    loading = false
                )
            }
            // Data update hone ke baad navigate karo
            onComplete()
        }
    }

    // PlateViewModel.kt mein:

    @OptIn(ExperimentalTime::class)
    fun exportPlate(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ) {
        val timestamp = clock.getCurrentMillis()
        viewModelScope.launch {
            _uiState.update { it.copy(exporting = true) }
            try {
                val currentState = _uiState.value
                // 1. Export Action
                val result = exportPlateUseCase(
                    frontImageData, backImageData, format,
                    currentState.registrationNumber,
                    currentState.selectedVehicle?.name ?: "VEHICLE"
                )

                when (result) {
                    is ExportResult.Success -> {
                        // 2. Persistence Action
                        val newPlate = RecentPlateItem(
                            id = timestamp.toString(),
                            plateNumber = currentState.registrationNumber,
                            category = currentState.selectedVehicle?.name ?: "Unknown",
                            province = currentState.selectedProvince?.name ?: "Unknown",
                            timestamp = timestamp,
                            plateImageRes = result.filePath
                        )
                        savePlateUseCase(newPlate.toEntity())

                        _uiState.update { it.copy(exporting = false, exportSuccess = true) }
                    }
                    is ExportResult.Error -> {
                        _uiState.update { it.copy(exporting = false, exportError = result.message) }
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(exporting = false, exportError = e.message) }
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