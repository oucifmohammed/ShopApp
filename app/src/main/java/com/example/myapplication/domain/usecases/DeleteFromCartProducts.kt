package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.util.ProcessUiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteFromCartProducts @Inject constructor(
    val productRepository: ProductRepository
){

    suspend fun invoke(cartProductId: String): ProcessUiState{
        return productRepository.deleteFromCartProducts(cartProductId)
    }
}