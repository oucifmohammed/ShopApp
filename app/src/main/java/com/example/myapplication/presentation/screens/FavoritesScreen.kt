package com.example.myapplication.presentation.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.presentation.components.BottomBar

@Composable
fun FavoritesScreen(
    navController: NavController
) {

    FavoritesScreenContent(navController = navController)
}

@Composable
fun FavoritesScreenContent(
    navController: NavController,
) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {

    }
}