package com.webscare.numberplatemaker.data.local.DatabaseBuilder

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.webscare.numberplatemaker.data.local.AppDatabase.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

// Yeh function `commonMain` mein rahega
fun getRoomDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

// Platform specific builder (Expect)
expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>