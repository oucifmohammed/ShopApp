package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.UserRepository
import com.example.myapplication.util.EMAIL_ADDRESS_PATTERN
import com.example.myapplication.util.ProcessUiState
import javax.inject.Inject

class SignIn @Inject constructor(private val userRepository: UserRepository){

    suspend fun invoke(email: String, password: String): ProcessUiState {

        if(email.trim().isEmpty() || password.trim().isEmpty()) {
            return ProcessUiState.Error("You need to fill all the fields")
        }

        if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return ProcessUiState.Error("There is an error in email field")
        }

        return userRepository.login(email,password)
    }
}
