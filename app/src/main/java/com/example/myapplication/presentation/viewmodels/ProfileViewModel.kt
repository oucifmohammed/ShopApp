package com.example.myapplication.presentation.viewmodels

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.usecases.EditProfile
import com.example.myapplication.domain.usecases.GetUserAccount
import com.example.myapplication.domain.usecases.GetUserOrders
import com.example.myapplication.domain.usecases.LogOut
import com.example.myapplication.util.Constants.DEFAULT_USER_IMAGE
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val editProfile: EditProfile,
    private val getUserAccount: GetUserAccount,
    private val logOut: LogOut,
    val getUserOrders: GetUserOrders
) : ViewModel() {

    val userName = mutableStateOf("")
    val email = mutableStateOf("")
    val profilePhoto = mutableStateOf("")
    val loading = mutableStateOf(false)

    lateinit var previousUserName: String
    lateinit var previousEmail: String
    lateinit var previousProfilePhoto: String

    private val _updateResult = MutableLiveData<ProcessUiState>()
    val updateResult: LiveData<ProcessUiState> = _updateResult

    private val _getUserOrdersResult = MutableLiveData<Resource<List<Order>>>()
    val getUserOrdersResult: LiveData<Resource<List<Order>>> = _getUserOrdersResult

    init {
        displayUserdata()
        getUserOrders()
    }

    private fun displayUserdata() = viewModelScope.launch {

        val user = withContext(Dispatchers.IO) {
            getUserAccount.invoke()
        }

        setUserName(user.userName)
        setEmail(user.email)
        profilePhoto.value = user.photoUrl

        previousUserName = user.userName
        previousEmail = user.email
        previousProfilePhoto = user.photoUrl
    }

    fun setUserName(username: String) {
        userName.value = username
    }

    fun setEmail(email: String) {
        this.email.value = email
    }

    fun updateProfile() = viewModelScope.launch {

        loading.value = true

        val result =
            if (profilePhoto.value == DEFAULT_USER_IMAGE || previousProfilePhoto == profilePhoto.value) {
                previousProfilePhoto = profilePhoto.value
                editProfile.invoke(userName.value, email.value, null)
            }
            else
                editProfile.invoke(
                    userName.value,
                    email.value,
                    Uri.parse(profilePhoto.value)
                )

        loading.value = false
        _updateResult.value = result
    }

    fun getUserOrders() = viewModelScope.launch {
        val result = getUserOrders.invoke()
        _getUserOrdersResult.value = result
    }

    fun logOut() = viewModelScope.launch {
        logOut.invoke()
    }
}