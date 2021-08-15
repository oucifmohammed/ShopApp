package com.example.myapplication.presentation.util

import androidx.annotation.DrawableRes
import com.example.myapplication.R

sealed class Screen(
    val route: String, val title: String = ""
    , @DrawableRes val icon: Int = 0
) {

    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object HomeScreen : Screen("home_screen","Home", R.drawable.home_icon)
    object FavoritesScreen : Screen("favorites_screen","Favorites",R.drawable.favorites_icon)
    object CarteScreen : Screen("carte_screen","Shopping",R.drawable.shopping_icon)
    object ProfileScreen : Screen("profile_screen","Profile",R.drawable.profile_icon)
}
