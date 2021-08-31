package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToRecentProduct @Inject constructor(
    val repository: Repository
) {

    suspend fun invoke(productId: String) {
        repository.addProductToRecentList(productId)
    }
}