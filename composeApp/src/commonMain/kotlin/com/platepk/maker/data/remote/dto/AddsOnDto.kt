package com.platepk.maker.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AddsOnDto(
    val id: Int,
    val title: String,
    val slug: String,
    val price: Double,
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)