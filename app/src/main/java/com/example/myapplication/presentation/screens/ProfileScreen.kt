package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.StandardTextField
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.ProfileViewModel
import com.example.myapplication.util.CropActivityResultContract
import com.example.myapplication.util.ProcessUiState

@ExperimentalComposeUiApi
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {


    ProfileScreenContent(navController = navController, viewModel)

    val context = LocalContext.current
    viewModel.updateResult.observe(LocalLifecycleOwner.current, { UiState ->
        when (UiState) {
            is ProcessUiState.Success -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
            is ProcessUiState.Error -> {
                Toast.makeText(context, UiState.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    })

}


@ExperimentalComposeUiApi
@Composable
fun ProfileScreenContent(
    navController: NavController,
    viewModel: ProfileViewModel,
) {

    val localFocusManager = LocalFocusManager.current
    val keyBoardController = LocalSoftwareKeyboardController.current

    val launcher = rememberLauncherForActivityResult(contract = CropActivityResultContract(1, 1)) {
        viewModel.profilePhoto.value = it.toString()
    }

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {

                }

                Image(
                    painter = rememberImagePainter(
                        request = ImageRequest
                            .Builder(LocalContext.current)
                            .placeholder(R.color.grey)
                            .data(viewModel.profilePhoto.value)
                            .build()
                    ),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .height(225.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            launcher.launch(null)
                        },
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.height(8.dp))

                StandardTextField(
                    text = viewModel.userName.value,
                    onChange = {
                        viewModel.setUserName(it)
                    },
                    keyboardActions = KeyboardActions(
                        onNext = {
                            localFocusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    imeAction = ImeAction.Next,
                )

                Spacer(modifier = Modifier.height(8.dp))

                StandardTextField(
                    text = viewModel.email.value,
                    onChange = {
                        viewModel.setEmail(it)
                    },
                    keyboardType = KeyboardType.Email,
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
                        viewModel.updateProfile()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                ) {
                    Text(text = "Edit", style = MaterialTheme.typography.button)
                }

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

            IconButton(
                onClick = {
                    viewModel.logOut()
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp),
                    tint = Color.Red,
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,

                )
            }
        }

    }
}