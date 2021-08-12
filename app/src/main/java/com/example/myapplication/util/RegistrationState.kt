package com.example.myapplication.util

sealed class RegistrationState(val status: Status) {
    class InProgress(val message: String = "", status: Status = Status.LOADING) :
        RegistrationState(status)

    class Error(val message: String = "", status: Status = Status.ERROR) : RegistrationState(status)
    class Success(val message: String = "", status: Status = Status.SUCCESS) :
        RegistrationState(status)
}
