package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisplayProductDetails @Inject constructor(
    val repository: Repository
){

    suspend fun invoke(productId: String): Resource<Product> {
        return repository.displayProductDetails(productId)
    }
}