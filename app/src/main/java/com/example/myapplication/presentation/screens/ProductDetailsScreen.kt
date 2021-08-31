package com.example.myapplication.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.presentation.viewmodels.ProductDetailsViewModel
import com.example.myapplication.util.ProcessUiState
import java.util.*

@Composable
fun ProductDetailsScreen(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailsViewModel = hiltViewModel()
) {

    viewModel.displayProductDetails(productId)

    ProductDetailsScreenContent(
        productId = productId,
        navController = navController,
        viewModel = viewModel
    )

    observerAddToCartResult(LocalContext.current,viewModel, LocalLifecycleOwner.current)
}

@Composable
fun ProductDetailsScreenContent(
    productId: String,
    navController: NavController,
    viewModel: ProductDetailsViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        if (viewModel.result.value == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.primary
            )
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFF))
                        .fillMaxHeight(0.5f)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            request = ImageRequest
                                .Builder(LocalContext.current)
                                .placeholder(R.color.grey)
                                .data(viewModel.result.value!!.data!!.image)
                                .build()
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RectangleShape),
                        contentDescription = "",
                    )

                    IconButton(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 9.dp),
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .align(Alignment.TopStart),
                            tint = MaterialTheme.colors.primary,
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = viewModel.result.value!!.data!!.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                            style = MaterialTheme.typography.h2,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(11.dp))

                        Text(
                            text =
                            if (viewModel.result.value!!.data!!.promotionPrice == 0f)
                                "Price: ${viewModel.result.value!!.data!!.originalPrice} DA"
                            else
                                "Price: ${viewModel.result.value!!.data!!.promotionPrice} DA",
                            style = MaterialTheme.typography.h3
                        )
                    }

                    Button(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(56.dp),
                        onClick = {
                            viewModel.addToCartProduct(productId)
                        }
                    ) {
                        Text(text = "Add to Cart")
                    }
                }
            }
        }
    }
}

fun observerAddToCartResult(
    context: Context,
    viewModel: ProductDetailsViewModel,
    lifecycleOwner: LifecycleOwner
) {
    viewModel.addToCartProductResult.observe(lifecycleOwner, { UiState ->
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