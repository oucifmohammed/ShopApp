package com.example.myapplication.presentation.util

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.screens.*
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FirebaseAuth.getInstance().currentUser?.let { Screen.HomeScreen.route }
            ?: Screen.LoginScreen.route) {

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }

        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(route = Screen.FavoritesScreen.route) {
            FavoritesScreen(navController)
        }

        composable(route = Screen.CarteScreen.route) {
            CarteScreen(navController)
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }
    }

}