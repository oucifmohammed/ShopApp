package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.OrderRepository
import com.example.myapplication.domain.models.Order
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserOrders @Inject constructor(
    private val orderRepository: OrderRepository
){

    suspend fun invoke(): Resource<List<Order>> {
        return orderRepository.getUserOrders()
    }
}