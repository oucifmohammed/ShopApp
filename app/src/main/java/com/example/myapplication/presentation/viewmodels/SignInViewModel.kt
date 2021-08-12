package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.SignIn
import com.example.myapplication.util.RegistrationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signIn: SignIn
): ViewModel(){

    val email = mutableStateOf("")

    var password  = mutableStateOf("")

    private val _signInResult = MutableLiveData<RegistrationState>()
    val signInResult: LiveData<RegistrationState> = _signInResult

    fun login(email: String, password: String) {

        viewModelScope.launch {
            _signInResult.value = RegistrationState.InProgress()

            val result = signIn.login(email,password)

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