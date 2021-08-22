package com.example.myapplication.util

sealed class ProcessUiState(val status: Status, val message: String) {

    class Error(message: String = "", status: Status = Status.ERROR) : ProcessUiState(status,message)

    class Success(message: String = "", status: Status = Status.SUCCESS) :
        ProcessUiState(status,message)
}
