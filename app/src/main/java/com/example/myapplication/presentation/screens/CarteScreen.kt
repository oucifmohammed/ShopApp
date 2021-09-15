package com.example.myapplication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.myapplication.domain.models.CartProduct
import com.example.myapplication.presentation.components.BottomBar
import com.example.myapplication.presentation.components.MyCartProductCard
import com.example.myapplication.presentation.viewmodels.CartViewModel
import com.example.myapplication.util.ProcessUiState
import com.example.myapplication.util.Resource
import com.example.myapplication.util.Status
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun CarteScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {

    val products: Resource<List<CartProduct>> by viewModel.userCartProducts.observeAsState(
        Resource.loading(null)
    )

    val orderResult: ProcessUiState? by viewModel.makeOrderResult.observeAsState()

    CarteScreenContent(
        viewModel = viewModel,
        navController = navController,
        productsList = products,
        orderResult = orderResult
    )

}

@ExperimentalMaterialApi
@Composable
fun CarteScreenContent(
    viewModel: CartViewModel,
    navController: NavController,
    productsList: Resource<List<CartProduct>>,
    orderResult: ProcessUiState?
) {
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetState = bottomState,
        sheetShape = RoundedCornerShape(topStart = 13.dp, topEnd = 13.dp),
        sheetContent = {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = 0.65f)
                    .padding(20.dp)
            ) {
                Column {
                    val totalProductsPrice = viewModel.calculateProductsTotalPrice()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Your Order", style = MaterialTheme.typography.h2)

                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    bottomState.hide()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp, 24.dp),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Products Total", style = MaterialTheme.typography.h3)

                        Text(text = "$totalProductsPrice DA", style = MaterialTheme.typography.h3)
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Shipping Charges", style = MaterialTheme.typography.h3)

                        Text(text = "500 DA", style = MaterialTheme.typography.h3)
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Divider(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(15.dp))

                    val total = totalProductsPrice + 500.0

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total", style = MaterialTheme.typography.h3)
                        Text(text = "$total DA", style = MaterialTheme.typography.h3)
                    }
                }

                if (viewModel.makeOrderLoadingState.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                } else {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .height(56.dp),
                        onClick = {
                            viewModel.makeOrder()
                        }
                    ) {
                        Text(text = "Confirm Your Order")
                    }
                }
            }

        },
        sheetBackgroundColor = MaterialTheme.colors.surface
    ) {

        val context = LocalContext.current

        Scaffold(
            bottomBar = {
                BottomBar(navController = navController)
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 24.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Your Cart",
                            style = MaterialTheme.typography.h1
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .clickable {
                                    coroutineScope.launch {
                                        if (productsList.data!!.isEmpty()) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "You can't make order, because you don't have any products in your cart yet.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        } else {
                                            bottomState.show()
                                        }
                                    }
                                },
                            text = "Order",
                            style = MaterialTheme.typography.h3,
                            color = MaterialTheme.colors.primary,
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {


                        when (productsList.status) {
                            Status.SUCCESS -> {

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .padding(bottom = 60.dp),
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
        )

        orderResult?.let { orderResult ->
            when (orderResult) {
                is ProcessUiState.Error -> {
                    Toast.makeText(
                        LocalContext.current,
                        "${orderResult.message}, please try later.",
                        Toast.LENGTH_SHORT
                    ).show()
                    coroutineScope.launch {
                        bottomState.hide()
                    }

                    if (viewModel.openDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialog.value = false
                            },
                            text = {
                                Text(text = "${orderResult.message}, please try later.")
                            },
                            confirmButton = {
                                TextButton(onClick = { viewModel.openDialog.value = false }) {
                                    Text("OK", color = MaterialTheme.colors.onSurface)
                                }
                            },
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    }
                }
                is ProcessUiState.Success -> {

                    coroutineScope.launch {
                        bottomState.hide()
                    }

                    if (viewModel.openDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                openDialog.value = false
                            },
                            text = {
                                Text(text = "${orderResult.message}, the order will arrive within one day. " +
                                        "Payment when receiving")
                            },
                            confirmButton = {
                                TextButton(onClick = { viewModel.openDialog.value = false }) {
                                    Text("OK", color = MaterialTheme.colors.onSurface)
                                }
                            },
                            backgroundColor = MaterialTheme.colors.surface,
                            contentColor = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }

}