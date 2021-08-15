package com.example.myapplication.domain

import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.RegistrationState
import com.example.myapplication.util.Resource

interface Repository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): RegistrationState

    suspend fun login(
        email: String,
        password: String
    ): RegistrationState

    suspend fun searchProductsByName(name: String): Resource<List<Product>>
}