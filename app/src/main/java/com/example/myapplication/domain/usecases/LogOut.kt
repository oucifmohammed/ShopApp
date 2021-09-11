package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogOut @Inject constructor(
    private val userRepository: UserRepository
){

    suspend fun invoke() {
        userRepository.logOut()
    }
}