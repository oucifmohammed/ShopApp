package com.example.myapplication.domain

import android.net.Uri
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.ProcessUiState

interface UserRepository {

    suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState

    suspend fun login(
        email: String,
        password: String
    ): ProcessUiState

    suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState

    suspend fun getUserAccount(): User

    suspend fun logOut()

    suspend fun subscribeToPromotionTopic()

}