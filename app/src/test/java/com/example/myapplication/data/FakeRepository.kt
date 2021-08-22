package com.example.myapplication.data

import com.example.myapplication.domain.Repository
import com.example.myapplication.domain.models.Product
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import java.util.*

class FakeRepository : Repository {

    private val usersList = mutableListOf<User>()

    private fun feedTheSource() {

        usersList.add(
            User(
                UUID.randomUUID().toString(),
                "Oucif Mohammed",
                "oucifmohamed8@gmail.com",
                "hello1234"
            )
        )

        usersList.add(
            User(
                UUID.randomUUID().toString(),
                "Oucif Zakaria",
                "oucifzakaria@gmail.com",
                "hello123"
            )
        )
    }

    init {
        feedTheSource()
    }

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): ProcessUiState {

        val result = usersList.find { user ->
            user.email == email
        }

        result?.let {
            return ProcessUiState.Error("This account already exists")
        }

        val user = User(id = UUID.randomUUID().toString(), userName = username, password = password,email = "")
        usersList.add(user)

        return ProcessUiState.Success("Registration completed successfully")
    }

    override suspend fun login(email: String, password: String): ProcessUiState {

        val result = usersList.find { user ->
            user.email == email && user.password == password
        }

        return result?.let { ProcessUiState.Success("logged in successfully") }
            ?: ProcessUiState.Error("You don't have an account yet")
    }

    override suspend fun searchProductsByName(name: String): Resource<List<Product>> {
        TODO("Not yet implemented")
    }
}