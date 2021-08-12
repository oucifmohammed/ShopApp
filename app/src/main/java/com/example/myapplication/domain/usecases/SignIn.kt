package com.example.myapplication.domain.usecases

import com.example.myapplication.domain.Repository
import com.example.myapplication.util.EMAIL_ADDRESS_PATTERN
import com.example.myapplication.util.RegistrationState
import javax.inject.Inject

class SignIn @Inject constructor(private val repository: Repository){

    suspend fun login(email: String, password: String): RegistrationState {

        if(email.trim().isEmpty() || password.trim().isEmpty()) {
            return RegistrationState.Error("You need to fill all the fields")
        }

        if (!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return RegistrationState.Error("There is an error in email field")
        }

        return repository.login(email,password)
    }
}
