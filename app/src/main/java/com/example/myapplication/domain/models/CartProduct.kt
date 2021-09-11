package com.example.myapplication.domain.models

data class CartProduct(
    val id: String,
    val parentProductId: String,
    val imageUrl: String,
    val name: String,
    val size: String,
    var price: Double = 0.0,
)