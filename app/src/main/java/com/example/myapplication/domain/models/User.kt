package com.example.myapplication.domain.models

data class User(
    val id: String = "",
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val photoUrl: String = "https://firebasestorage.googleapis.com/v0/b/snplc-91bf1.appspot.com/o/default_profile_picture.png?alt=media&token=3b9853b5-1949-4ece-ab9f-c22cdf758d12"
)