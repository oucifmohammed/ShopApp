package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToRecentProduct @Inject constructor(
    val productRepository: ProductRepository
) {

    suspend fun invoke(productId: String) {
        productRepository.addProductToRecentList(productId)
    }
}