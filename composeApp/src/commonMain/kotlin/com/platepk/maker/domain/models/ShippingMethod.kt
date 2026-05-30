package com.platepk.maker.domain.models

data class ShippingMethodDomain(
    val id: Int,
    val title: String,
    val deliveryTime: String,
    val price: Double,
    val description: String?
)