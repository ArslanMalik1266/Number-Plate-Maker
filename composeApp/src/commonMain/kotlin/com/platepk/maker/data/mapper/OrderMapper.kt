package com.platepk.maker.data.mapper

import com.platepk.maker.data.remote.dto.OrderDataDto
import com.platepk.maker.data.remote.dto.OrderRequest
import com.platepk.maker.domain.models.Order

fun Order.toRequest(): OrderRequest {
    return OrderRequest(
        full_name = this.fullName,
        number = this.contactNumber,
        email = this.email,
        province = this.province,
        city = this.city,
        area = this.area,
        address = this.address,
        postal_code = this.postalCode,
        reg_number = this.regNumber,
        vehicle_type = this.vehicleType,
        vehicle_province = this.vehicleProvince,
        plate_type = this.plateType,
        shipping_method = this.shippingMethod,
        add_ons = this.addOns ?: emptyList(),

    )
}