package com.example.myapplication.domain

import com.example.myapplication.util.Resource

interface Repository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<String>
}