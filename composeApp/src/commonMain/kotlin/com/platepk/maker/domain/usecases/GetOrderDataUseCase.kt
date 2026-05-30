package com.platepk.maker.domain.usecases

import com.platepk.maker.data.remote.dto.OrderRequest
import com.platepk.maker.domain.models.*
import com.platepk.maker.domain.repo.OrderRepository

class GetOrderDataUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(): Triple<List<PlateType>, List<ShippingMethodDomain>, List<AddsOn>> {
        val plateTypes = repository.getPlateTypes().getOrDefault(emptyList())
        val shippingMethods = repository.getShippingMethods().getOrDefault(emptyList())
        val addsOns = repository.getAddsOns().getOrDefault(emptyList())
        println("DEBUG_USECASE_PLATE_TYPES: $plateTypes")
        return Triple(plateTypes, shippingMethods, addsOns)
    }
}