package com.platepk.maker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShippingMethodDto(
    val id: Int,
    val title: String,
    val slug: String,
    @SerialName("delivery_time")
    val deliveryTime: String,
    val price: Double,
    val description: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
