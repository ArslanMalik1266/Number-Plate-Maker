package com.platepk.maker.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponseDto<T>(
    val success: Boolean,
    val data: List<T>,
    val meta: MetaDto? = null
)

@Serializable
data class MetaDto(
    val total: Int,
    val per_page: Int,
    val current_page: Int,
    val last_page: Int,
    val from: Int,
    val to: Int
)