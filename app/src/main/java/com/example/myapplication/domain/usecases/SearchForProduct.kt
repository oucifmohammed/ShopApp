package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchForProduct @Inject constructor(
    private val repository: Repository
) {
    suspend fun invoke(searchQuery: String): Resource<List<Product>> {

        if (searchQuery.trim().isEmpty()) {
            return Resource.error(message = "You have to write the search query", data = null)
        }

        return repository.searchProductsByName(searchQuery)
    }
}