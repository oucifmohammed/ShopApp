package com.example.myapplication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.presentation.components.BottomBar

@Composable
fun ProfileScreen(
    navController: NavController
) {

    ProfileScreenContent(navController = navController)
}

@Composable
fun ProfileScreenContent(
    navController: NavController,
) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {

    }
}