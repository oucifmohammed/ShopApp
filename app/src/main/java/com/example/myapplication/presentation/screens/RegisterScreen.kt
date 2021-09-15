package com.example.myapplication.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
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
import com.example.myapplication.util.ProcessUiState

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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreenContent(
    navController: NavController,
    viewModel: RegisterViewModel
) {

    val localFocusManager = LocalFocusManager.current
    val keyBoardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Sign up",
                style = MaterialTheme.typography.h2,
            )

            StandardTextField(
                text = viewModel.fullName.value, hint = "Full Name",
                onChange = {
                    viewModel.setFullName(it)
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                imeAction = ImeAction.Next
            )

            StandardTextField(
                text = viewModel.email.value,
                hint = "Email",
                keyboardType = KeyboardType.Email,
                onChange = {
                    viewModel.setEmail(it)
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                imeAction = ImeAction.Next
            )

            PasswordTextField(
                password = viewModel.password.value,
                hint = "Password",
                onChange = {
                    viewModel.setPassword(it)
                },
                keyboardActions = KeyboardActions(
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                imeAction = ImeAction.Next
            )

            PasswordTextField(
                password = viewModel.confirmPassword.value,
                hint = "Confirm Password",
                onChange = {
                    viewModel.setConfirmPassword(it)
                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        localFocusManager.clearFocus()
                        keyBoardController?.hide()
                    }
                ),
                imeAction = ImeAction.Done
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
                    .padding(top = 48.dp, bottom = 8.dp)
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        navController.popBackStack()
                    },

                style = MaterialTheme.typography.body1
            )

            if (viewModel.loading.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                }
            }
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
            is ProcessUiState.Success -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
                navController.navigate(Screen.HomeScreen.route) {
                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                }
            }
            is ProcessUiState.Error -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    })
}