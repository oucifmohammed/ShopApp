package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.util.ProcessUiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleAddButton @Inject constructor(
    val repository: Repository
){

    suspend fun invoke(productId: String): ProcessUiState {
        return repository.addToCartProducts(productId)
    }
}