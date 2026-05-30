package com.platepk.maker.data.remote.api

import com.platepk.maker.data.network.NetworkConfig
import com.platepk.maker.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PlateApiServiceImpl(
    private val client: HttpClient
) : PlateApiService {

    private val baseUrl = NetworkConfig.BASE_URL

    override suspend fun getPlateTypes(): BaseResponseDto<PlateTypeDto> {
        return client.get("$baseUrl/plate-types").body()
    }

    override suspend fun getShippingMethods(): BaseResponseDto<ShippingMethodDto> {
        return client.get("$baseUrl/shipping-methods").body()
    }

    override suspend fun getAddsOns(): BaseResponseDto<AddsOnDto> {
        return client.get("$baseUrl/add-ons").body()
    }
    override suspend fun submitOrder(request: OrderRequest): HttpResponse {
        return client.post("$baseUrl/order-forms") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}