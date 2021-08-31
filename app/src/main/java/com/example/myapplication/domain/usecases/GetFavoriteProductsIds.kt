package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFavoriteProductsIds @Inject constructor(
    val repository: Repository
){

    suspend fun invoke(): Flow<List<String>> {
        return repository.getFavoriteProductsIds()
    }
}