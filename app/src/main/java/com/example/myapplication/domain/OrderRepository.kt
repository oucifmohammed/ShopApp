package com.example.myapplication.domain

import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource

interface OrderRepository {

    suspend fun addOrder(order: Order): ProcessUiState

    suspend fun getUserOrders(): Resource<List<Order>>

    suspend fun getOrderProducts(orderId: String): Resource<List<Product>>
}