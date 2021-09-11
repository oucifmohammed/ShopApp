package com.example.myapplication.presentation.util

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.presentation.screens.*
import com.google.firebase.auth.FirebaseAuth

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
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

        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController)
        }

        composable(route = Screen.CarteScreen.route) {
            CarteScreen(navController)
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }

        composable(
            route = "${Screen.ProductDetailsScreen.route}/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.StringType
                }
            )
        ) {
            val productId = it.arguments?.getString("productId")

            productId?.let { id ->
                ProductDetailsScreen(productId = id, navController = navController)
            }
        }

        composable(route = Screen.InformationScreen.route) {
            InformationScreen(navController)
        }

        composable(route = "${Screen.OrderProductsScreen.route}/{orderId}") {

            val orderId = it.arguments?.getString("orderId")

            orderId?.let { id ->
                OrderProductsScreen(id,navController)
            }
        }
    }

}