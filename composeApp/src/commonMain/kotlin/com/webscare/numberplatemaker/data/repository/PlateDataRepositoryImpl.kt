package com.webscare.numberplatemaker.data.repository

import com.webscare.numberplatemaker.data.local.dao.PlateDao
import com.webscare.numberplatemaker.data.local.entity.PlateEntity
import com.webscare.numberplatemaker.domain.repo.PlateDataRepository
import kotlinx.coroutines.flow.Flow

class PlateDataRepositoryImpl(
    private val plateDao: PlateDao
) : PlateDataRepository {

    override fun getAllPlates(): Flow<List<PlateEntity>> = plateDao.getAllPlates()

    override suspend fun insertPlate(plate: PlateEntity): Long = plateDao.insertPlate(plate)

    override suspend fun deletePlate(plate: PlateEntity) = plateDao.deletePlate(plate)

    override suspend fun deleteAllPlates() = plateDao.deleteAllPlates()
}