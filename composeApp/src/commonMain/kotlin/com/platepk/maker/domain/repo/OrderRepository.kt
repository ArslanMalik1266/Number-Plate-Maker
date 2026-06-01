package com.platepk.maker.domain.repo

import com.platepk.maker.data.remote.dto.OrderDto
import com.platepk.maker.data.remote.dto.OrderRequest
import com.platepk.maker.domain.models.AddsOn
import com.platepk.maker.domain.models.Order
import com.platepk.maker.domain.models.PlateType
import com.platepk.maker.domain.models.ShippingMethodDomain

interface OrderRepository {
    suspend fun getPlateTypes(): Result<List<PlateType>>
    suspend fun getShippingMethods(): Result<List<ShippingMethodDomain>>
    suspend fun getAddsOns(): Result<List<AddsOn>>

    suspend fun submitOrder(request: Order, imageBytes: ByteArray?): Result<Unit>
}