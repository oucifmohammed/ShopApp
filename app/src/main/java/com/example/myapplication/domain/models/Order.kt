package com.example.myapplication.domain.models

data class Order(
    val id: String,
    val userId: String = "",
    val orderNumber: Int = 0,
    val orderProductsList: List<String> = listOf(),
    val totalPrice: Double
)
