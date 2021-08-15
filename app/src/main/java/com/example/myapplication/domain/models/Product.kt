package com.example.myapplication.domain.models

data class Product(
    val id: String,
    val name: String,
    val image: String,
    val category: String,
    val originalPrice: Float,
    val promotionPrice: Float
)
