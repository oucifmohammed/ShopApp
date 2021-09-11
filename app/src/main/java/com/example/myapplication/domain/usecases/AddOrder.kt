package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.OrderRepository
import com.example.myapplication.domain.models.Order
import com.example.myapplication.util.ProcessUiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddOrder @Inject constructor(
    private val orderRepository: OrderRepository
){

    suspend fun invoke(order: Order): ProcessUiState{
        return orderRepository.addOrder(order)
    }
}