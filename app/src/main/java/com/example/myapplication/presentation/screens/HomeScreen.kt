package com.example.myapplication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen() {

    Box(modifier = Modifier.fillMaxSize()) {

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
            },
            modifier = Modifier.align(Alignment.Center)
        ) {

        }
    }
}