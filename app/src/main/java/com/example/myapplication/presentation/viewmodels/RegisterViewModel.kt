package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.CreateAccount
import com.example.myapplication.util.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccount: CreateAccount
) : ViewModel() {

    var email = mutableStateOf("")

    var fullName = mutableStateOf("")

    var password = mutableStateOf("")

    var confirmPassword = mutableStateOf("")

    private val _registrationResult = MutableLiveData<RegistrationState>()
    val registrationResult: LiveData<RegistrationState> = _registrationResult

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) {

        viewModelScope.launch {

            _registrationResult.value = RegistrationState.InProgress()

            val result = createAccount.register(
                email,
                username,
                password,
                confirmPassword
            )

            _registrationResult.value = result
        }
    }

    fun setEmail(email: String) {
        this.email.value = email
    }

    fun setFullName(fullName: String) {
        this.fullName.value = fullName
    }

    fun setPassword(password: String) {
        this.password.value = password
    }

    fun setConfirmPassword(confirmPassword: String) {
        this.confirmPassword.value = confirmPassword
    }
}