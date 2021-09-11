package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.util.ProcessUiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToCartProducts @Inject constructor(
    val productRepository: ProductRepository
){

    suspend fun invoke(cartProduct: CartProduct): ProcessUiState {
        return productRepository.addToCartProducts(cartProduct)
    }
}