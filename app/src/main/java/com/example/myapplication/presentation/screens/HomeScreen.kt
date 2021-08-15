package com.example.myapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.SearchBar
import com.example.myapplication.presentation.components.StandardProductCard
import com.example.myapplication.presentation.viewmodels.HomeViewModel
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val products: Resource<List<Product>> by viewModel.productList.observeAsState(
        Resource.loading(null)
    )

    HomeScreenContent(
        productList = products,
        viewModel = viewModel,
        navController = navController
    )
}

@Composable
fun HomeScreenContent(
    productList: Resource<List<Product>>,
    navController: NavController,
    viewModel: HomeViewModel
) {

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                SearchBar(
                    text = viewModel.searchQuery.value,
                    hint = "What are you looking for?",
                    onChange = {
                        viewModel.setSearchQuery(it)
                    },
                    onSearch = {
                        viewModel.searchProduct(viewModel.searchQuery.value)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    when (productList.status) {
                        Status.SUCCESS -> {
                            LazyColumn(
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                items(items = productList.data!!) { product ->
                                    StandardProductCard(product = product)
                                }
                            }
                        }
                        Status.ERROR -> {
                            Text(
                                text = productList.message!!,
                                style = MaterialTheme.typography.h3,
                                modifier = Modifier.align(
                                    Alignment.Center
                                )
                            )
                        }
                        Status.LOADING -> {

                            productList.data?.let {
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
}