package com.example.myapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.domain.models.Order
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.components.HomeSmallProductsCard
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.OrderProductsViewModel
import com.example.myapplication.util.ProductCardType
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status

@Composable
fun OrderProductsScreen(
    orderId: String,
    navController: NavController,
    viewModel: OrderProductsViewModel = hiltViewModel()
) {
    val orderProducts: Resource<List<Product>> by viewModel.orderProductsResult.observeAsState(
        Resource.loading(null)
    )
    viewModel.getProducts(orderId)

    OrderProductsScreenContent(products = orderProducts, navController)
}

@Composable
fun OrderProductsScreenContent(
    products: Resource<List<Product>>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp, end = 9.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .size(35.dp),
                    tint = MaterialTheme.colors.primary,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.width(11.dp))

            Text(
                text = "Products",
                style = MaterialTheme.typography.h2
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            when (products.status) {
                Status.SUCCESS -> {
                    LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp)) {
                        items(items = products.data!!) { product ->
                            HomeSmallProductsCard(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                product = product,
                                onSelect = {
                                    //Do nothing
                                },
                                onToggleLikeButton = {
                                    // Do nothing
                                },
                                cardType = ProductCardType.ORDERPRODUCT
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    Text(
                        text = products.message!!,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Status.LOADING -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                    )
                }
            }
        }
    }
}