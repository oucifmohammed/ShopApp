package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteProductsIds @Inject constructor(
    val productRepository: ProductRepository
){

    suspend fun invoke(): Flow<List<String>> {
        return productRepository.getFavoriteProductsIds()
    }
}