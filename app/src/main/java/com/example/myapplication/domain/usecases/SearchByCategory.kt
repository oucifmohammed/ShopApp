package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.ProductRepository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchByCategory @Inject constructor(
    private val productRepository: ProductRepository
) {

    suspend fun invoke(category: String,name: String?): Resource<List<Product>> {

        return productRepository.searchProductsByCategory(category,name)
    }
}