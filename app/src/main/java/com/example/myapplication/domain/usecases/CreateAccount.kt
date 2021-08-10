package com.example.myapplication.domain.usecases

import android.util.Patterns
import com.example.myapplication.domain.Repository
import com.example.myapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateAccount @Inject constructor(private val repository: Repository) {

    suspend fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): Resource<String> {

        if (username.trim().isEmpty() || email.trim().isEmpty() || confirmPassword.trim()
                .isEmpty() || password.trim().isEmpty()
        ) {
            return Resource.error("You need to fill all the fields", null)
        }

        if (confirmPassword != password) {
            return Resource.error("check the confirmation of the password", null)
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Resource.error("There is an error in email field", null)
        }

        return repository.register(email, username, password)
    }
}