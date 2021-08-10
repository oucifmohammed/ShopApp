package com.example.myapplication.data

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Resource
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class FakeRepository: Repository {

    private val usersList = mutableListOf<User>()

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): Resource<String> {

        val user = User(id = Random.toString(), userName = username, password = password)
        usersList.add(user)

        return Resource.success("Registration completed successfully")
    }
}