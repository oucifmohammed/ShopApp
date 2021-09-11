package com.example.myapplication.data.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class CartProductDto(
    val id: String = "",
    val parentProductId: String = "",
    val imageUrl: String = "",
    val name: String = "",
    val size: String = "",
    @get:Exclude var price: Double = 0.0
)
