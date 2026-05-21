package com.webscare.numberplatemaker.domain.models

data class RecentPlateItem(
    val id: String,
    val plateNumber: String,
    val category: String,
    val province: String,
    val timestamp: Long,
    val plateImageRes: String? = null
)