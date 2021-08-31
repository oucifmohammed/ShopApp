package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddToFavorites @Inject constructor(
    val repository: Repository
    )
{
    suspend fun invoke(product: Product): Resource<Boolean> {
        return repository.toggleLikeButton(product)
    }
}