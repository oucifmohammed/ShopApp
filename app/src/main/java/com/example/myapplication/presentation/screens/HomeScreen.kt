package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.HomeSmallProductsCard
import com.example.myapplication.presentation.components.ProductPromotionCard
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.HomeViewModel
import com.example.myapplication.util.ProductCardType
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    viewModel.subscribeToPromotionsTopic()

    val favoriteProducts: Resource<List<Product>> by viewModel.userFavoriteProducts.observeAsState(
        Resource.loading(null)
    )

    val recentProducts: Resource<List<Product>> by viewModel.userRecentProducts.observeAsState(
        Resource.loading(null)
    )

    val productPromotions: Resource<List<Product>> by viewModel.productPromotions.observeAsState(
        Resource.loading(null)
    )

    HomeScreenContent(
        favoriteProductList = favoriteProducts,
        viewModel = viewModel,
        navController = navController,
        recentProductList = recentProducts,
        productPromotionsList = productPromotions
    )
}

@Composable
fun HomeScreenContent(
    favoriteProductList: Resource<List<Product>>,
    recentProductList: Resource<List<Product>>,
    productPromotionsList: Resource<List<Product>>,
    navController: NavController,
    viewModel: HomeViewModel
) {

    val favoriteProductsIds = viewModel.favoriteProductsIds.observeAsState()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 60.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            ) {
                Text(
                    text = "Welcome",
                    style = MaterialTheme.typography.h1
                )

                Row(
                    modifier = Modifier
                        .align(CenterEnd),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = {
                            navController.navigate(Screen.SearchScreen.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colors.primary,
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        modifier = Modifier.size(48.dp),
                        onClick = {
                            navController.navigate(Screen.InformationScreen.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colors.primary,
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Promotions",
                style = MaterialTheme.typography.h2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (productPromotionsList.status) {
                Status.SUCCESS -> {
                    LazyRow {
                        items(items = productPromotionsList.data!!) { product ->
                            ProductPromotionCard(
                                product = product,
                                onSelect = {
                                    viewModel.addToRecentList(product.id)
                                    navController.navigate("${Screen.ProductDetailsScreen.route}/${product.id}")
                                },
                                onToggleLikeButton = {
                                    viewModel.toggleLikeButton(product)
                                },
                                isLiked = favoriteProductsIds.value?.contains(product.id)!!
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    productPromotionsList.data?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(CenterHorizontally),
                            text = productPromotionsList.message!!,
                            style = MaterialTheme.typography.subtitle1
                        )
                    } ?: Toast.makeText(
                        LocalContext.current,
                        productPromotionsList.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                Status.LOADING -> {
                    Spacer(modifier = Modifier.height(325.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Favorites",
                style = MaterialTheme.typography.h2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (favoriteProductList.status) {
                Status.SUCCESS -> {
                    LazyRow {
                        items(items = favoriteProductList.data!!) { product ->
                            HomeSmallProductsCard(
                                modifier = Modifier.width(270.dp).padding(horizontal = 16.dp),
                                product = product,
                                onSelect = {
                                    viewModel.addToRecentList(product.id)
                                    navController.navigate("${Screen.ProductDetailsScreen.route}/${product.id}")
                                },
                                onToggleLikeButton = {
                                    viewModel.toggleLikeButton(product)
                                },
                                cardType = ProductCardType.FAVORITEPRODUCT
                            )
                        }
                    }
                }
                Status.ERROR -> {

                    favoriteProductList.data?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(CenterHorizontally),
                            text = favoriteProductList.message!!,
                            style = MaterialTheme.typography.subtitle1
                        )
                    } ?: Toast.makeText(
                        LocalContext.current,
                        favoriteProductList.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                Status.LOADING -> {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = "Recent",
                style = MaterialTheme.typography.h2,
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (recentProductList.status) {
                Status.SUCCESS -> {
                    LazyRow {
                        items(items = recentProductList.data!!) { product ->
                            HomeSmallProductsCard(
                                modifier = Modifier.width(270.dp).padding(horizontal = 16.dp),
                                product = product,
                                onSelect = {
                                    navController.navigate("${Screen.ProductDetailsScreen.route}/${product.id}")
                                },
                                onToggleLikeButton = {
                                    // Do nothing
                                },
                                cardType = ProductCardType.RECENTPRODUCT
                            )
                        }
                    }
                }
                Status.ERROR -> {

                    recentProductList.data?.let {
                        Text(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .align(CenterHorizontally),
                            text = recentProductList.message!!,
                            style = MaterialTheme.typography.subtitle1
                        )
                    } ?: Toast.makeText(
                        LocalContext.current,
                        recentProductList.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                Status.LOADING -> {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }
        }
    }
}
