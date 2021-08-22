package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.CreateAccount
import com.example.myapplication.util.ProcessUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccount: CreateAccount
) : ViewModel() {

    val email = mutableStateOf("")
    val fullName = mutableStateOf("")
    val password = mutableStateOf("")
    val confirmPassword = mutableStateOf("")
    val loading = mutableStateOf(false)

    private val _registrationResult = MutableLiveData<ProcessUiState>()
    val registrationResult: LiveData<ProcessUiState> = _registrationResult

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) {

        viewModelScope.launch {

            loading.value = true

            val result = createAccount.register(
                email,
                username,
                password,
                confirmPassword
            )

            loading.value = false
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