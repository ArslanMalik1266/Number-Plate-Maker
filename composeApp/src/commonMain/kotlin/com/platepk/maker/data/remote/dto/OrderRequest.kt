package com.platepk.maker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val full_name: String,
    val number: String,
    val email: String,
    val province: String,
    val city: String,
    val area: String,
    val address: String,
    val postal_code: String,
    val reg_number: String,
    val vehicle_type: String,
    val vehicle_province: String,
    val plate_type: String?,
    val shipping_method: String?,
    val add_ons: List<String>,
)
