package com.platepk.maker.domain.usecases

import com.platepk.maker.domain.models.Order
import com.platepk.maker.domain.repo.OrderRepository

class SubmitOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(order: Order): Result<Unit> {
        return repository.submitOrder(order)
    }
}