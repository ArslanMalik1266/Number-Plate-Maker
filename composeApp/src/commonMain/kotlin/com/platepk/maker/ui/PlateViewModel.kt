package com.platepk.maker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.platepk.maker.data.local.entity.PlateEntity
import com.platepk.maker.domain.models.ExportFormat
import com.platepk.maker.domain.models.PlateConfig
import com.platepk.maker.domain.models.PlateInputConfig
import com.platepk.maker.domain.models.PlateModel
import com.platepk.maker.domain.models.PlateSide
import com.platepk.maker.domain.models.PlateStep
import com.platepk.maker.domain.models.PlateUiState
import com.platepk.maker.domain.models.Province
import com.platepk.maker.domain.models.RecentPlateItem
import com.platepk.maker.domain.models.SettingsUiState
import com.platepk.maker.domain.models.VehicleType
import com.platepk.maker.domain.usecases.DeleteAllPlatesUseCase
import com.platepk.maker.domain.usecases.DeletePlateUseCase
import com.platepk.maker.domain.usecases.EnforcePlateInputUseCase
import com.platepk.maker.domain.usecases.ExportPlateUseCase
import com.platepk.maker.domain.usecases.GetPlateConfigUseCase
import com.platepk.maker.domain.usecases.GetPlateInputConfigUseCase
import com.platepk.maker.domain.usecases.GetPlatesUseCase
import com.platepk.maker.domain.usecases.GetThemeSettingsUseCase
import com.platepk.maker.domain.usecases.SavePlateUseCase
import com.platepk.maker.domain.usecases.ToggleThemeUseCase
import com.platepk.maker.mapper.toDomain
import com.platepk.maker.mapper.toEntity
import com.platepk.maker.util.PlatformClock
import com.platepk.maker.util.readFileBytes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlateViewModel(
    private val clock: PlatformClock,
    private val getPlateConfigUseCase: GetPlateConfigUseCase,
    private val exportPlateUseCase: ExportPlateUseCase,
    private val getPlateInputConfigUseCase: GetPlateInputConfigUseCase,
    private val enforcePlateInputUseCase: EnforcePlateInputUseCase,
    private val getPlatesUseCase: GetPlatesUseCase,
    private val savePlateUseCase: SavePlateUseCase,
    private val deletePlateUseCase: DeletePlateUseCase,
    private val deleteAllPlatesUseCase: DeleteAllPlatesUseCase,
    private val toggleThemeUseCase: ToggleThemeUseCase,
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlateUiState())
    val uiState: StateFlow<PlateUiState> = _uiState.asStateFlow()
    private val _settingsState = MutableStateFlow(SettingsUiState())
    val settingsState = _settingsState.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
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

    fun onThemeSelected(index: Int) {
        viewModelScope.launch {
            toggleThemeUseCase(index == 1)
        }
    }

    init {
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }

        viewModelScope.launch {
            getPlatesUseCase().collect { entities ->
                println("DEBUG_FETCH: Database se ${entities.size} plates mili hain")
                entities.forEach { plate ->
                    println("DEBUG_FETCH: Plate ID: ${plate.id}, Reg: ${plate.registrationNumber}, Front: ${plate.frontImagePath}")
                    println("DEBUG_FETCH: Plate ID: ${plate.id}, Reg: ${plate.registrationNumber}, back: ${plate.backImagePath}")
                }
                val domainModels = entities.map { it.toDomain() }
                _uiState.update { it.copy(savedPlates = domainModels) }
                _settingsState.update { it.copy(savedCount = entities.size) }
            }
        }
        viewModelScope.launch {
            getThemeSettingsUseCase().collect { isDark ->
                _settingsState.update {
                    it.copy(
                        isDarkMode = isDark,
                        selectedThemeIndex = if (isDark) 1 else 0
                    )
                }
            }
        }
        val defaultVehicle = VehicleType.PRIVATE_CAR
        val defaultProvince = Province.PUNJAB

        // Initialize the state with these values
        _uiState.update {
            it.copy(
                selectedVehicle = defaultVehicle,
                selectedProvince = defaultProvince,
                currentStep = PlateStep.InputNumber
            )
        }
    }

    // --- UI Events (User Actions) ---

    fun setInitialSelections(vehicle: VehicleType, province: Province) {
        _uiState.update {
            it.copy(
                selectedVehicle = vehicle,
                selectedProvince = province
            )
        }
    }

    fun onVehicleSelected(vehicle: VehicleType) {
        viewModelScope.launch {
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
        viewModelScope.launch { // Launch use karein
            // Heavy logic background mein
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
        return when (side) {
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
        val timestamp = clock.getCurrentMillis()

        val currentState = _uiState.value
        val vehicle = currentState.selectedVehicle
        val province = currentState.selectedProvince
        viewModelScope.launch {
            try {
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
            } catch (e: Exception) {
                // Agar file saving ya database mein koi issue aaye, toh yahan handle hoga
                _uiState.update {
                    it.copy(
                        loading = false,
                        exportError = "Failed to save plate: ${e.message}"
                    )
                }
                println("DEBUG_SAVE_ERROR: ${e.message}")
            }
        }
    }

    fun savePlateImages(frontBytes: ByteArray, backBytes: ByteArray) {  // ✅ ByteArray
        val currentState = _uiState.value
        val vehicle = currentState.selectedVehicle ?: return
        val province = currentState.selectedProvince ?: return
        val timestamp = clock.getCurrentMillis()
        val regNumber = currentState.registrationNumber

        viewModelScope.launch {
            val frontPath = exportPlateUseCase.savePlateLocally(
                frontBytes, "${regNumber}_${timestamp}_front"
            )
            val backPath = exportPlateUseCase.savePlateLocally(
                backBytes, "${regNumber}_${timestamp}_back"
            )

            if (frontPath.isEmpty() || backPath.isEmpty()) return@launch

            val newPlate = RecentPlateItem(
                id = "0",
                plateNumber = regNumber,
                category = vehicle.name,
                province = province.name,
                timestamp = timestamp,
                plateImageRes = frontPath,
                plateImageBackRes = backPath,
                pdfPath = null
            )
            savePlateUseCase(newPlate.toEntity())
            _uiState.update {
                it.copy(frontImagePath = frontPath, backImagePath = backPath)
            }
            println("DEBUG_SAVED: front=$frontPath back=$backPath")
        }
    }

    // PlateViewModel.kt mein:

    fun exportPlate(
        frontImageData: ByteArray,
        backImageData: ByteArray,
        format: ExportFormat
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(exporting = true, exportError = null) }
            try {
                val currentState = _uiState.value
                val result = exportPlateUseCase(
                    frontImageData, backImageData, format,
                    currentState.registrationNumber,
                    currentState.selectedVehicle?.name ?: "VEHICLE"
                )
                result.fold(
                    onSuccess = {
                        _uiState.update { it.copy(exporting = false, exportSuccess = true) }
                    },
                    onFailure = { e ->
                        _uiState.update { it.copy(exporting = false, exportError = e.message) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(exporting = false, exportError = e.message) }
            }
        }
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
        val shouldClearPlates = currentState.currentStep is PlateStep.Preview
        _uiState.update {
            it.copy(
                currentStep = previousStep,
                frontPlate = if (shouldClearPlates) null else it.frontPlate,
                backPlate = if (shouldClearPlates) null else it.backPlate
            )
        }
    }

    fun resetExportState() {
        _uiState.update {
            it.copy(
                exportSuccess = false,
                exportError = null,

                )
        }
    }

    fun clearRegistrationFields() {
        _uiState.update {
            it.copy(
                registrationNumber = "",
                letterInput = "",
                numberInput = ""
            )
        }
    }

    fun getStats(): Triple<Int, Int, Int> {
        val plates = _uiState.value.savedPlates
        val total = plates.size
        val provinces = plates.map { it.province }.distinct().size
        val oneWeekAgo = clock.getCurrentMillis() - (7 * 24 * 60 * 60 * 1000L)
        val thisWeek = plates.count { it.timestamp >= oneWeekAgo }
        return Triple(total, provinces, thisWeek)
    }

    fun resetSelection() {
        _uiState.update {
            it.copy(
                selectedVehicle = null,
                selectedProvince = null,
                registrationNumber = "",
                currentStep = PlateStep.VehicleSelection,
                frontPlate = null,
                backPlate = null
            )
        }
    }

    fun exportFromHistory(
        frontPath: String,
        backPath: String,
        format: ExportFormat,
        registrationNumber: String,
        vehicleType: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(exporting = true, exportError = null) }
            try {
                val frontBytes = readFileBytes(frontPath)  // ✅ expect/actual
                val backBytes = readFileBytes(backPath)

                val result = exportPlateUseCase(
                    frontBytes, backBytes, format,
                    registrationNumber,
                    vehicleType
                )
                result.fold(
                    onSuccess = {
                        _uiState.update { it.copy(exporting = false, exportSuccess = true) }
                    },
                    onFailure = { e ->
                        _uiState.update { it.copy(exporting = false, exportError = e.message) }
                    }
                )
            } catch (e: Exception) {
                _uiState.update { it.copy(exporting = false, exportError = e.message) }
                println("DEBUG_EXPORT_HISTORY_ERROR: ${e.message}")
            }
        }
    }

}