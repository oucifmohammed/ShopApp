package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.presentation.components.PasswordTextField
import com.example.myapplication.presentation.components.StandardTextField
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.RegisterViewModel
import com.example.myapplication.util.Resource

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val email = remember {
        mutableStateOf("")
    }

    val fullName = remember {
        mutableStateOf("")
    }

    val password = remember {
        mutableStateOf("")
    }

    val confirmPassword = remember {
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
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Sign up",
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(top = 12.dp)
            )

            StandardTextField(text = fullName, hint = "Full Name")

            StandardTextField(
                text = email,
                hint = "Email",
                keyboardType = KeyboardType.Email
            )

            PasswordTextField(
                text = password,
                hint = "Password",
            )

            PasswordTextField(
                text = confirmPassword,
                hint = "Confirm Password",
            )

            Button(
                onClick = {
                    viewModel.register(
                        email.value,
                        fullName.value,
                        password.value,
                        confirmPassword.value
                    )
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp),
            ) {
                Text(text = "Register", style = MaterialTheme.typography.button)
            }

            Text(
                text = buildAnnotatedString {
                    append("Already have an account?")
                    append(" ")

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Login")
                    }
                },
                modifier = Modifier
                    .padding(top = 50.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.popBackStack()
                    },

                style = MaterialTheme.typography.body1
            )
        }

        when (viewModel.registrationResult.value?.status) {
            Resource.Status.SUCCESS -> {
                viewModel.registrationResult.value?.let {
                    Toast.makeText(LocalContext.current, "${it.message}", Toast.LENGTH_SHORT).show()
                }

                navController.navigate(Screen.HomeScreen.route)
            }
            Resource.Status.ERROR -> {
                viewModel.registrationResult.value?.let {
                    Toast.makeText(LocalContext.current, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            Resource.Status.LOADING -> {
                //Do nothing.
            }
        }
    }
}