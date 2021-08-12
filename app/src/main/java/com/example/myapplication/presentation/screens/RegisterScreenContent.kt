package com.example.myapplication.presentation.screens

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.myapplication.presentation.components.PasswordTextField
import com.example.myapplication.presentation.components.StandardTextField
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.RegisterViewModel
import com.example.myapplication.util.RegistrationState

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    RegisterScreenContent(navController = navController, viewModel = viewModel)

    observerRegistrationResult(
        LocalContext.current,
        viewModel,
        LocalLifecycleOwner.current,
        navController
    )
}

@Composable
fun RegisterScreenContent(
    navController: NavController,
    viewModel: RegisterViewModel
) {

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

            StandardTextField(
                text = viewModel.fullName.value, hint = "Full Name",
                onChange = {
                    viewModel.setFullName(it)
                },
            )

            StandardTextField(
                text = viewModel.email.value,
                hint = "Email",
                keyboardType = KeyboardType.Email,
                onChange = {
                    viewModel.setEmail(it)
                }
            )

            PasswordTextField(
                password = viewModel.password.value,
                hint = "Password",
                onChange = {
                    viewModel.setPassword(it)
                }
            )

            PasswordTextField(
                password = viewModel.confirmPassword.value,
                hint = "Confirm Password",
                onChange = {
                    viewModel.setConfirmPassword(it)
                }
            )

            Button(
                onClick = {
                    viewModel.register(
                        viewModel.email.value,
                        viewModel.fullName.value,
                        viewModel.password.value,
                        viewModel.confirmPassword.value
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
    }
}

fun observerRegistrationResult(
    context: Context,
    viewModel: RegisterViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavController
) {
    viewModel.registrationResult.observe(lifecycleOwner, { UiState ->
        when (UiState) {
            is RegistrationState.Success -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            }
            is RegistrationState.Error -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
            is RegistrationState.InProgress -> {
                //Do nothing.
            }
        }
    })
}