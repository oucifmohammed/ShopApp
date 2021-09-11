package com.example.myapplication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.animations.LoadingRecipeListShimmer
import com.example.myapplication.presentation.components.CategoryCard
import com.example.myapplication.presentation.components.SearchBar
import com.example.myapplication.presentation.components.StandardProductCard
import com.example.myapplication.presentation.util.Constants.SEARCH_PRODUCT_IMAGE_HEIGHT
import com.example.myapplication.presentation.util.Screen
import com.example.myapplication.presentation.viewmodels.SearchViewModel
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val products: Resource<List<Product>> by viewModel.productList.observeAsState(
        Resource.loading(null)
    )

    SearchScreenContent(navController, products, viewModel)
}

@Composable
fun SearchScreenContent(
    navController: NavController,
    productList: Resource<List<Product>>,
    viewModel: SearchViewModel
) {


    val favoriteProductsIds = viewModel.favoriteProductsIds.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .align(Alignment.End)
        ) {

            IconButton(
                modifier = Modifier
                    .padding(start = 9.dp)
                    .align(Alignment.CenterVertically),
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

            Text(
                modifier = Modifier
                    .padding(start = 11.dp)
                    .align(Alignment.CenterVertically),
                text = "Search",
                style = MaterialTheme.typography.h2
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

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

        Spacer(modifier = Modifier.height(10.dp))

        val list = listOf("Women", "Men", "Children")
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            list.map { category ->
                CategoryCard(
                    category = category,
                    isSelected = category == viewModel.selectedCategory.value,
                    onSelectedCategoryChanged = {
                        viewModel.onSelectedCategoryChanged(it)
                    },
                    onExecuteSearch = {
                        viewModel.searchByCategory()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {


            when (productList.status) {
                Status.SUCCESS -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        items(items = productList.data!!) { product ->
                            StandardProductCard(
                                product = product,
                                onSelect = {
                                    navController.navigate("${Screen.ProductDetailsScreen.route}/${product.id}")
                                },
                                onToggleLikeButton = {
                                    viewModel.toggleLikeButton(it)
                                },
                                isLiked = favoriteProductsIds.value?.contains(product.id)!!
                            )
                        }
                    }
                }
                Status.ERROR -> {

                    Text(
                        text = productList.message!!,
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.align(
                            Alignment.Center
                        ),
                        textAlign = TextAlign.Center
                    )
                }
                Status.LOADING -> {

                    productList.data?.let {
                        LoadingRecipeListShimmer(
                            imageHeight = SEARCH_PRODUCT_IMAGE_HEIGHT.dp
                        )
                    }
                }
            }
        }

    }

}