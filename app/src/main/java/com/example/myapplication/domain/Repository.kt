package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.domain.usecases.LogOut
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource

interface Repository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState

    suspend fun login(
        email: String,
        password: String
    ): ProcessUiState

    suspend fun searchProductsByName(name: String): Resource<List<Product>>

    suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState

    suspend fun getUserAccount(): User

    suspend fun logOut()
}