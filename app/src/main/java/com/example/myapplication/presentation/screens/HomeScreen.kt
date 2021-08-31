package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.HomeSmallProductsCard
import com.example.myapplication.presentation.components.ProductPromotionCard
import com.example.myapplication.presentation.components.StandardProductCard
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
                .padding(bottom = 50.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 20.dp, end = 15.dp),
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
                        onClick = {
                            navController.navigate(Screen.SearchScreen.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(33.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colors.primary,
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                        )
                    }

                    Spacer(modifier = Modifier.width(5.dp))

                    IconButton(
                        onClick = {
                            navController.navigate(Screen.InformationScreen.route)
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(33.dp)
                                .align(Alignment.CenterVertically),
                            tint = MaterialTheme.colors.primary,
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Promotions",
                style = MaterialTheme.typography.h2,
            )

            when (productPromotionsList.status) {
                Status.SUCCESS -> {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp)) {
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
                                .padding(top = 14.dp)
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
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .align(CenterHorizontally),
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Favorites",
                style = MaterialTheme.typography.h2,
            )

            when (favoriteProductList.status) {
                Status.SUCCESS -> {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp)) {
                        items(items = favoriteProductList.data!!) { product ->
                            HomeSmallProductsCard(
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
                                .padding(top = 14.dp)
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
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .align(CenterHorizontally),
                        color = MaterialTheme.colors.primary
                    )
                }
            }

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = "Recent",
                style = MaterialTheme.typography.h2,
            )

            when (recentProductList.status) {
                Status.SUCCESS -> {
                    LazyRow(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 15.dp)) {
                        items(items = recentProductList.data!!) { product ->
                            HomeSmallProductsCard(
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
                                .padding(top = 14.dp)
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
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .align(CenterHorizontally),
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}
