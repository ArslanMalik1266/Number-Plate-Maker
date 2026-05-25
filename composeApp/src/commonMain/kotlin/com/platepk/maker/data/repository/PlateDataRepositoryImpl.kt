package com.platepk.maker.data.repository

import com.platepk.maker.data.local.dao.PlateDao
import com.platepk.maker.data.local.entity.PlateEntity
import com.platepk.maker.domain.repo.PlateDataRepository
import kotlinx.coroutines.flow.Flow

class PlateDataRepositoryImpl(
    private val plateDao: PlateDao
) : PlateDataRepository {

    override fun getAllPlates(): Flow<List<PlateEntity>> = plateDao.getAllPlates()

    override suspend fun insertPlate(plate: PlateEntity): Long = plateDao.insertPlate(plate)

    override suspend fun deletePlate(plate: PlateEntity) = plateDao.deletePlate(plate)

    override suspend fun deleteAllPlates() = plateDao.deleteAllPlates()
}