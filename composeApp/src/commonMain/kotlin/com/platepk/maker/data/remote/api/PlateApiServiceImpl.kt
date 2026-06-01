package com.platepk.maker.data.remote.api

import com.platepk.maker.data.network.NetworkConfig
import com.platepk.maker.data.remote.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

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
    override suspend fun submitOrder(
        request: OrderRequest,
        imageBytes: ByteArray?
    ): HttpResponse {
        return client.post("$baseUrl/order-forms") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("full_name", request.full_name)
                        append("number", request.number)
                        append("email", request.email)
                        append("province", request.province)
                        append("city", request.city)
                        append("area", request.area)
                        append("address", request.address)
                        append("postal_code", request.postal_code)
                        append("reg_number", request.reg_number)
                        append("vehicle_type", request.vehicle_type)
                        append("vehicle_province", request.vehicle_province)
                        request.plate_type?.let { append("plate_type", it) }
                        request.shipping_method?.let { append("shipping_method", it) }
                        request.add_ons.forEachIndexed { index, addon ->
                            append("add_ons[$index]", addon)
                        }
                        imageBytes?.let {
                            append(
                                "image",
                                it,
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/png")
                                    append(HttpHeaders.ContentDisposition, "filename=plate.png")
                                }
                            )
                        }
                    }
                )
            )
        }
    }
}