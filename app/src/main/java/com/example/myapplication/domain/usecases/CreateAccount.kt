package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.util.EMAIL_ADDRESS_PATTERN
import com.example.myapplication.util.RegistrationState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateAccount @Inject constructor(private val repository: Repository) {

    suspend fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ): RegistrationState {

        if (username.trim().isEmpty() || email.trim().isEmpty() || confirmPassword.trim()
                .isEmpty() || password.trim().isEmpty()
        ) {
            return RegistrationState.Error("You need to fill all the fields")
        }

        if (confirmPassword != password) {
            return RegistrationState.Error("check the confirmation of the password")
        }

        if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return RegistrationState.Error("There is an error in email field")
        }

        return repository.register(email, username, password)
    }
}