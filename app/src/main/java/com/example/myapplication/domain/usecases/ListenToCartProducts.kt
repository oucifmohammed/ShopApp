package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListenToCartProducts @Inject constructor(
    val productRepository: ProductRepository
){

    suspend fun invoke(): Flow<Resource<List<CartProduct>>> {
        return productRepository.listenToCartProducts()
    }
}