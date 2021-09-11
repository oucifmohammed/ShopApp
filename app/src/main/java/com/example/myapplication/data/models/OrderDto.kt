package com.example.myapplication.data.models

data class OrderDto(
    val id: String = "",
    var userId: String = "",
    var orderNumber: Int = 0 ,
    var orderProductsList: List<String> = listOf(),
    val totalPrice: Double = 0.0
)
