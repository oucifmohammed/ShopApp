package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ListenToRecentProductAddition @Inject constructor(
    val repository: Repository
){

    suspend fun invoke(): Flow<Resource<List<Product>>> {

        return repository.listenToRecentProductAddition()
    }
}