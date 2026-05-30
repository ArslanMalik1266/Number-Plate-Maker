package com.platepk.maker.domain.models

data class Order(
    val id: Int = 0,
    val fullName: String,
    val contactNumber: String,
    val email: String,
    val province: String,
    val city: String,
    val area: String,
    val address: String,
    val postalCode: String,
    val regNumber: String,
    val vehicleType: String,
    val vehicleProvince: String,
    val image: String?,
    val plateType: String?,
    val shippingMethod: String?,
    val addOns: List<String>?,
    val status: String? = null,
    val createdAt: String = ""
)
