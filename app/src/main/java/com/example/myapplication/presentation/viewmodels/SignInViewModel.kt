package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.SignIn
import com.example.myapplication.util.ProcessUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SignIn
): ViewModel(){

    val email = mutableStateOf("")
    val password  = mutableStateOf("")
    val loading = mutableStateOf(false)

    private val _signInResult = MutableLiveData<ProcessUiState>()
    val signInResult: LiveData<ProcessUiState> = _signInResult

    fun login(email: String, password: String) {

        viewModelScope.launch {

            loading.value = true

            val result = signIn.invoke(email,password)

            loading.value = false
            _signInResult.value = result
        }
    }

    fun setEmail(email: String) {
        this.email.value = email
    }

    fun setPassword(password: String) {
        this.password.value = password
    }
}