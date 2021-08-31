package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogOut @Inject constructor(
    private val repository: Repository
){

    suspend fun invoke() {
        repository.logOut()
    }
}