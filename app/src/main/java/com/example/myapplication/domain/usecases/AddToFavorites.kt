package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToFavorites @Inject constructor(
    val productRepository: ProductRepository
    )
{
    suspend fun invoke(product: Product): Resource<Boolean> {
        return productRepository.toggleLikeButton(product)
    }
}