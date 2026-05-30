package com.platepk.maker.data.remote.api

import com.platepk.maker.data.remote.dto.AddsOnDto
import com.platepk.maker.data.remote.dto.BaseResponseDto
import com.platepk.maker.data.remote.dto.OrderDto
import com.platepk.maker.data.remote.dto.OrderRequest
import com.platepk.maker.data.remote.dto.PlateTypeDto
import com.platepk.maker.data.remote.dto.ShippingMethodDto
import com.platepk.maker.domain.models.Order
import io.ktor.client.statement.HttpResponse

interface PlateApiService {
    suspend fun getPlateTypes(): BaseResponseDto<PlateTypeDto>
    suspend fun getShippingMethods(): BaseResponseDto<ShippingMethodDto>
    suspend fun getAddsOns(): BaseResponseDto<AddsOnDto>
    suspend fun submitOrder(request: OrderRequest): HttpResponse


}