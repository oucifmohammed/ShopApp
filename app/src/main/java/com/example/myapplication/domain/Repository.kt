package com.example.myapplication.domain

import com.example.myapplication.util.RegistrationState

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
}