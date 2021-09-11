package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.OrderRepository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetOrderProducts @Inject constructor(
    private val repository: OrderRepository
){

    suspend fun invoke(orderId: String): Resource<List<Product>> {
        return repository.getOrderProducts(orderId)
    }
}