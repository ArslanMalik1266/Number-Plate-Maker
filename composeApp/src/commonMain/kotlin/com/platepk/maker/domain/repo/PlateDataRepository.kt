package com.platepk.maker.domain.repo

import com.platepk.maker.data.local.entity.PlateEntity
import kotlinx.coroutines.flow.Flow

interface PlateDataRepository {
    fun getAllPlates(): Flow<List<PlateEntity>>
    suspend fun insertPlate(plate: PlateEntity): Long
    suspend fun deletePlate(plate: PlateEntity)
    suspend fun deleteAllPlates()
}