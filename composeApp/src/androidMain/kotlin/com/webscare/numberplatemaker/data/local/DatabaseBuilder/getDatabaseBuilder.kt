package com.webscare.numberplatemaker.data.local.DatabaseBuilder

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.webscare.numberplatemaker.data.local.AppDatabase.AppDatabase

lateinit var appContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = appContext.getDatabasePath("number_plates.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}