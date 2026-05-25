package com.platepk.maker.domain.models

data class RecentPlateItem(
    val id: String,
    val plateNumber: String,
    val category: String,
    val province: String,
    val timestamp: Long,
    val plateImageRes: String? = null,
    val plateImageBackRes: String? = null,
    val pdfPath: String? = null
)
