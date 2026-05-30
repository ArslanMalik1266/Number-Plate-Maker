package com.platepk.maker.data.mapper

import com.platepk.maker.data.remote.dto.PlateTypeDto
import com.platepk.maker.domain.models.PlateType

fun PlateTypeDto.toDomain(): PlateType {
    return PlateType(
        id = this.id,
        title = this.title,
        price = this.price,
        description = this.description
    )
}
fun List<PlateTypeDto>.toDomain(): List<PlateType> {
    return this.map { it.toDomain() }
}
