package com.webscare.numberplatemaker.mapper

import com.webscare.numberplatemaker.data.local.entity.PlateEntity
import com.webscare.numberplatemaker.domain.models.RecentPlateItem

fun PlateEntity.toDomain(): RecentPlateItem {
    return RecentPlateItem(
        id = this.id.toString(),
        plateNumber = this.registrationNumber,
        category = this.vehicleType,
        province = this.province,
        timestamp = this.issuedDate,
        plateImageRes = this.frontImagePath,
        plateImageBackRes = this.backImagePath,
        pdfPath = this.pdfFilePath?.ifEmpty { null }
    )
}

// Domain (UI) -> Entity (DB)
fun RecentPlateItem.toEntity(): PlateEntity {
    return PlateEntity(
        id = this.id.toLongOrNull() ?: 0L,
        registrationNumber = this.plateNumber,
        vehicleType = this.category,
        province = this.province,
        issuedDate = this.timestamp,
        frontImagePath = this.plateImageRes ?: "",
        backImagePath = this.plateImageBackRes ?: "",
        pdfFilePath = this.pdfPath ?: "",
        exportFormat = if (!this.pdfPath.isNullOrEmpty()) "PDF" else "PNG"
    )
}