package com.example.myapplication.data

import android.net.Uri
import com.example.myapplication.domain.UserRepository
import com.example.myapplication.domain.models.User
import com.example.myapplication.util.Constants
import com.example.myapplication.util.ProcessUiState
import java.util.*

class UserFakeRepository : UserRepository{

    private val usersList = mutableListOf<User>()
    val loggedInUsersList = mutableListOf<User>()
    val topicUsersList = mutableListOf<User>()

    private fun feedTheSource() {

        usersList.add(
            User(
                "1",
                "Oucif Mohammed",
                "oucifmohamed8@gmail.com",
                "hello1234",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf("1","2"),
                recentProducts = listOf("1","2"),
                cartProducts = listOf("2")
            )
        )

        usersList.add(
            User(
                "2",
                "Oucif Zakaria",
                "oucifzakaria@gmail.com",
                "hello123",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf(),
                recentProducts = listOf(),
                cartProducts = listOf()
            )
        )

        usersList.add(
            User(
                "3",
                "Oucif Maria",
                "oucifmaria@gmail.com",
                "hello12345",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf(),
                recentProducts = listOf(),
                cartProducts = listOf()
            )
        )

        loggedInUsersList.add(
            User(
                "1",
                "Oucif Mohammed",
                "oucifmohamed8@gmail.com",
                "hello1234",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf("1","2"),
                recentProducts = listOf("1","2"),
                cartProducts = listOf("2")
            )
        )

        loggedInUsersList.add(
            User(
                "2",
                "Oucif Zakaria",
                "oucifzakaria@gmail.com",
                "hello123",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf(),
                recentProducts = listOf(),
                cartProducts = listOf()
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

        val user = User(
            id = UUID.randomUUID().toString(),
            userName = username,
            password = password,
            email = "",
            photoUrl = Constants.DEFAULT_USER_IMAGE,
            favoriteProducts = listOf(),
            recentProducts = listOf(),
            cartProducts = listOf()
        )
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

    override suspend fun editProfile(username: String, email: String, uri: Uri?): ProcessUiState {
        usersList[0].userName = "Mohamed"

        return ProcessUiState.Success()
    }

    override suspend fun getUserAccount(): User {
        return usersList[0]
    }

    override suspend fun logOut() {
        loggedInUsersList.removeAt(0)
    }

    override suspend fun subscribeToPromotionTopic() {
        topicUsersList.add(
            User(
                "1",
                "Oucif Mohammed",
                "oucifmohamed8@gmail.com",
                "hello1234",
                photoUrl = Constants.DEFAULT_USER_IMAGE,
                favoriteProducts = listOf("1","2"),
                recentProducts = listOf("1","2"),
                cartProducts = listOf("2")
            )
        )
    }
}