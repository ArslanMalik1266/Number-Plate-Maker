package com.webscare.numberplatemaker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_plates")
data class PlateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val registrationNumber: String,
    val vehicleType: String,
    val province: String,
    val issuedDate: Long,
    val frontImagePath: String?,
    val backImagePath: String?,
    val pdfFilePath: String?,
    val exportFormat: String
)