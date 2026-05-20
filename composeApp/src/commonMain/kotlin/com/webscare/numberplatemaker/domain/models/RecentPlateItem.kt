package com.webscare.numberplatemaker.domain.models

data class RecentPlateItem(
    val id: String,
    val plateNumber: String,
    val category: String,
    val province: String,
    val timestamp: String,
    val plateImageRes: String? = null
)