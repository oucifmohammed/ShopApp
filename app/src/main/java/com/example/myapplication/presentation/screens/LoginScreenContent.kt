package com.example.myapplication.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.myapplication.presentation.viewmodels.SignInViewModel
import com.example.myapplication.util.RegistrationState

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    LoginScreenContent(navController = navController, viewModel = viewModel)

    observerLoginResult(LocalContext.current, viewModel, LocalLifecycleOwner.current, navController)
}

@Composable
fun LoginScreenContent(
    navController: NavController,
    viewModel: SignInViewModel
) {

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

            StandardTextField(
                text = viewModel.email.value,
                hint = "Email",
                keyboardType = KeyboardType.Email,
                onChange = {
                    viewModel.setEmail(it)
                })

            PasswordTextField(
                password = viewModel.password.value,
                hint = "Password",
                onChange = {
                    viewModel.setPassword(it)
                }
            )

            Button(
                onClick = {
                    viewModel.login(viewModel.email.value, viewModel.password.value)
                },
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

fun observerLoginResult(
    context: Context,
    viewModel: SignInViewModel,
    lifecycleOwner: LifecycleOwner,
    navController: NavController
) {
    viewModel.signInResult.observe(lifecycleOwner, { UiState ->
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