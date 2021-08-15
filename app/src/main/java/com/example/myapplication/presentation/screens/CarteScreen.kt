package com.example.myapplication.presentation.screens

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.util.Screen

@Composable
fun CarteScreen(
    navController: NavController
) {

    CarteScreenContent(navController = navController)
}

@Composable
fun CarteScreenContent(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {

    }
}