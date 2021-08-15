package com.example.myapplication.data.models

data class ProductDto(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val category: String = "",
    val originalPrice: Float = 0f,
    val promotion: Boolean = false,
    val promotionPrice: Float = 0f
)
