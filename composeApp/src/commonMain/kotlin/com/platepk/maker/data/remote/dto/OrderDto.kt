package com.platepk.maker.data.remote.dto

data class OrderDto(
    val success: Boolean,
    val message: String,
    val data: OrderDataDto?
)

data class OrderDataDto(
    val id: Int,
    val full_name: String,
    val number: String,
    val email: String,
    val province: String,
    val city: String,
    val area: String,
    val address: String,
    val postal_code: String,
    val image: String?,
    val reg_number: String,
    val vehicle_type: String,
    val vehicle_province: String,
    val plate_type: String?,
    val shipping_method: String?,
    val add_ons: List<String>?,
    val status: String?,
    val created_at: String
)
