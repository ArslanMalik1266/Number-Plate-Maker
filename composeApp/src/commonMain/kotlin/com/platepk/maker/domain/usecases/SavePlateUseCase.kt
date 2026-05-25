package com.platepk.maker.domain.usecases

import com.platepk.maker.data.local.entity.PlateEntity
import com.platepk.maker.domain.repo.PlateDataRepository

class SavePlateUseCase(private val repository: PlateDataRepository) {
    suspend operator fun invoke(plate: PlateEntity) {
        // Yahan aap print statements lagayen
        println("DEBUG_DB: Saving Plate to Room ----------------")
        println("DEBUG_DB: Reg Number: ${plate.registrationNumber}")
        println("DEBUG_DB: Category: ${plate.vehicleType}")
        println("DEBUG_DB: Province: ${plate.province}")
        println("DEBUG_DB: Front Image Path: ${plate.frontImagePath ?: "NULL"}")
        println("DEBUG_DB: Back Image Path: ${plate.backImagePath ?: "NULL"}")
        println("DEBUG_DB: Timestamp: ${plate.issuedDate}")
        println("DEBUG_DB: -------------------------------------")

        // Data base mein save karein
        repository.insertPlate(plate)
    }
}