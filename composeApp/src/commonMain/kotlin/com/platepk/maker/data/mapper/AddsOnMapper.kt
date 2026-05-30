package com.platepk.maker.data.mapper

import com.platepk.maker.data.remote.dto.AddsOnDto
import com.platepk.maker.domain.models.AddsOn

fun AddsOnDto.toDomain(): AddsOn {
    return AddsOn(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description
    )
}

fun List<AddsOnDto>.toDomain(): List<AddsOn> {
    return this.map { it.toDomain() }
}