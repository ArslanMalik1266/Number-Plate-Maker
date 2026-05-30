package com.platepk.maker.data.mapper

import com.platepk.maker.data.remote.dto.ShippingMethodDto
import com.platepk.maker.domain.models.ShippingMethod
import com.platepk.maker.domain.models.ShippingMethodDomain

fun ShippingMethodDto.toDomain(): ShippingMethodDomain {
    return ShippingMethodDomain(
        id = this.id,
        title = this.title,
        deliveryTime = this.deliveryTime,
        price = this.price,
        description = this.description
    )
}
fun List<ShippingMethodDto>.toDomain(): List<ShippingMethodDomain> {
    return this.map { it.toDomain() }
}