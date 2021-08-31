package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserAccount @Inject constructor(
    val repository: Repository
){

    suspend fun invoke(): User {
        return repository.getUserAccount()
    }
}