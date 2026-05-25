package com.platepk.maker.data.local.AppDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.platepk.maker.data.local.dao.PlateDao
import com.platepk.maker.data.local.entity.PlateEntity


@Database(entities = [PlateEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plateDao(): PlateDao
}
