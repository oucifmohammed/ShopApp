package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.UserRepository
import com.example.myapplication.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserAccount @Inject constructor(
    private val userRepository: UserRepository
){

    suspend fun invoke(): User {
        return userRepository.getUserAccount()
    }
}