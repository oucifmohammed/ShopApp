package com.example.myapplication.data.models

import com.example.myapplication.util.Constants.DEFAULT_USER_IMAGE

data class UserDto(
    val id: String = "",
    var userName: String = "",
    val email: String = "",
    val photoUrl: String = DEFAULT_USER_IMAGE,
    val password: String = "",
    val favoriteProducts: List<String> = listOf(),
    val recentProducts: List<String> = listOf(),
    val cartProducts: List<String> = listOf()
)
