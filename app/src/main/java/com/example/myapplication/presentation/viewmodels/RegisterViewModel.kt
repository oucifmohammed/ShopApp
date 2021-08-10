package com.example.myapplication.presentation.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecases.CreateAccount
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val createAccount: CreateAccount
) : ViewModel() {

    private val _registrationResult: MutableState<Resource<String>?> = mutableStateOf(null)
    val registrationResult: State<Resource<String>?> = _registrationResult

    fun register(
        email: String,
        username: String,
        password: String,
        confirmPassword: String
    ) {

        viewModelScope.launch {

            _registrationResult.value = Resource.loading(null)

            val result = createAccount.register(
                email,
                username,
                password,
                confirmPassword
            )

            _registrationResult.value = result
        }
    }

}