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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.presentation.components.SizesCard
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
        navController = navController,
        viewModel = viewModel
    )

    observerAddToCartResult(LocalContext.current, viewModel, LocalLifecycleOwner.current)
}

@Composable
fun ProductDetailsScreenContent(
    navController: NavController,
    viewModel: ProductDetailsViewModel
) {

    val context = LocalContext.current

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
                            .size(48.dp)
                            .padding(top = 16.dp, start = 16.dp),
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
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
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = viewModel.result.value!!.data!!.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                            style = MaterialTheme.typography.h2
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text =
                            if (viewModel.result.value!!.data!!.promotionPrice == 0f)
                                "Price: ${viewModel.result.value!!.data!!.originalPrice} DA"
                            else
                                "Price: ${viewModel.result.value!!.data!!.promotionPrice} DA",
                            style = MaterialTheme.typography.h3
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row {
                            Text(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                text = "Sizes:",
                                style = MaterialTheme.typography.h3
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {

                                viewModel.result.value!!.data!!.sizes.map { category ->
                                    SizesCard(
                                        category = category,
                                        isSelected = category == viewModel.selectedCategory.value,
                                        onSelectedCategoryChanged = {
                                            viewModel.onSelectedCategoryChanged(it)
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(56.dp),
                        onClick = {

                            if(viewModel.selectedCategory.value == null) {
                                Toast.makeText(context, "Please choose a size.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            viewModel.addToCartProduct(
                                viewModel.result.value.let {
                                    CartProduct(
                                        id = UUID.randomUUID().toString(),
                                        parentProductId = it!!.data!!.id,
                                        imageUrl = it.data!!.image,
                                        name = it.data.name,
                                        size = viewModel.selectedCategory.value!!
                                    )
                                }
                            )
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