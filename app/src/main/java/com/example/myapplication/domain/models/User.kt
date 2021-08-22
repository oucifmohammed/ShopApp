package com.example.myapplication.domain.models

data class User(
    val id: String,
    val userName: String,
    val email: String,
    val password: String,
    val photoUrl: String,
    val favoriteProducts: List<String>,
    val recentProducts: List<String>
)