package com.platepk.maker.data.repository

import com.platepk.maker.data.mapper.toDomain
import com.platepk.maker.data.mapper.toRequest
import com.platepk.maker.data.remote.api.PlateApiService // Aapka API service
import com.platepk.maker.data.remote.dto.OrderRequest
import com.platepk.maker.domain.models.AddsOn
import com.platepk.maker.domain.models.Order
import com.platepk.maker.domain.models.PlateType
import com.platepk.maker.domain.models.ShippingMethodDomain
import com.platepk.maker.domain.repo.OrderRepository
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

class OrderRepositoryImpl(
    private val apiService: PlateApiService
) : OrderRepository {

    override suspend fun getPlateTypes(): Result<List<PlateType>> {
        return try {
            val response = apiService.getPlateTypes()
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getShippingMethods(): Result<List<ShippingMethodDomain>> {
        return try {
            val response = apiService.getShippingMethods()
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAddsOns(): Result<List<AddsOn>> {
        return try {
            val response = apiService.getAddsOns()
            Result.success(response.data.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitOrder(order: Order, imageBytes: ByteArray?): Result<Unit> {
        return try {
            val request = order.toRequest()
            val response = apiService.submitOrder(request, imageBytes)

            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                println("DEBUG_SERVER_ERROR: $errorBody")
                Result.failure(Exception("Server error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}