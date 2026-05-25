package com.platepk.maker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.platepk.maker.data.local.entity.PlateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlateDao {
    @Query("SELECT * FROM saved_plates ORDER BY id DESC")
    fun getAllPlates(): Flow<List<PlateEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlate(plate: PlateEntity): Long
    @Delete
    suspend fun deletePlate(plate: PlateEntity)
    @Query("DELETE FROM saved_plates")
    suspend fun deleteAllPlates()
}