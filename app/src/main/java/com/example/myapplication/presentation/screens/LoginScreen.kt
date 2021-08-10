package com.example.myapplication.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.presentation.components.StandardTextField
import com.example.myapplication.presentation.components.PasswordTextField
import com.example.myapplication.presentation.util.Screen

@Composable
fun LoginScreen(
    navController: NavController
) {

    val email = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Login",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(top = 12.dp)
            )

            StandardTextField(text = email, hint = "Email", keyboardType = KeyboardType.Email)

            PasswordTextField(
                text = password,
                hint = "Password",
            )

            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp),
            ) {
                Text(text = "Login", style = MaterialTheme.typography.button)
            }

            Text(
                text = buildAnnotatedString {
                    append("New to this app?")
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Register")
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp, bottom = 6.dp)
                    .clickable {
                        navController.navigate(Screen.RegisterScreen.route)
                    },

                style = MaterialTheme.typography.body1,
            )

            Spacer(modifier = Modifier.fillMaxHeight())
        }
    }

}
