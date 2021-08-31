package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchByCategory @Inject constructor(
    private val repository: Repository
) {

    suspend fun invoke(category: String,name: String?): Resource<List<Product>> {

        return repository.searchProductsByCategory(category,name)
    }
}