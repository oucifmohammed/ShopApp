package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubscribeToPromotionsTopic @Inject constructor(
    val userRepository: UserRepository
){

    suspend fun invoke() {
        userRepository.subscribeToPromotionTopic()
    }
}