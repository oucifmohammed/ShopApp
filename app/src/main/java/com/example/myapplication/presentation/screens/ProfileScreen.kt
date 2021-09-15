package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.domain.models.Order
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.ExpandableCard
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.ProfileViewModel
import com.example.myapplication.util.CropActivityResultContract
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

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

    val userOrdersResult: Resource<List<Order>> by viewModel.getUserOrdersResult.observeAsState(
        Resource.loading(null)
    )

    ProfileScreenContent(navController = navController, viewModel,userOrdersResult = userOrdersResult.data)
}


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun ProfileScreenContent(
    navController: NavController,
    viewModel: ProfileViewModel,
    userOrdersResult: List<Order>?
) {

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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp)
            ) {

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

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = viewModel.userName.value,
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(32.dp))

                userOrdersResult?.let {
                    ExpandableCard(ordersList = it,navController = navController)
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
                    .size(48.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    tint = Color.Red,
                    imageVector = Icons.Filled.Logout,
                    contentDescription = null,
                )
            }
        }

    }
}