package com.example.myapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.MyCartProductCard
import com.example.myapplication.presentation.components.StandardProductCard
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.CartViewModel
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status

@Composable
fun CarteScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {

    val products: Resource<List<Product>> by viewModel.userCartProducts.observeAsState(
        Resource.loading(null)
    )

    CarteScreenContent(
        viewModel = viewModel,
        navController = navController,
        productsList = products
    )
}

@Composable
fun CarteScreenContent(
    viewModel: CartViewModel,
    navController: NavController,
    productsList: Resource<List<Product>>
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp),
                text = "Cart products",
                style = MaterialTheme.typography.h1
            )

            Spacer(modifier = Modifier.height(15.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {


                when (productsList.status) {
                    Status.SUCCESS -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                        ) {
                            items(items = productsList.data!!) { product ->
                                MyCartProductCard(
                                    product = product,
                                    onDeleteButton = {
                                        viewModel.deleteCartProduct(product.id)
                                    }
                                )
                            }
                        }
                    }
                    Status.ERROR -> {

                        Text(
                            text = productsList.message!!,
                            style = MaterialTheme.typography.h3,
                            modifier = Modifier.align(
                                Alignment.Center
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                    Status.LOADING -> {

                        productsList.data?.let {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}