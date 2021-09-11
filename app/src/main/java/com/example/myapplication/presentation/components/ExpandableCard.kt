package com.example.myapplication.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.domain.models.Order
import com.example.myapplication.presentation.util.Screen

@ExperimentalMaterialApi
@Composable
fun ExpandableCard(
    ordersList: List<Order>,
    navController: NavController
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        },
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Orders", style = MaterialTheme.typography.h3)

                IconButton(
                    modifier = Modifier
                        .rotate(rotationState.value),
                    onClick = { expandedState = !expandedState }
                ) {
                    Icon(
                        tint = MaterialTheme.colors.primary,
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (expandedState) {

                if (ordersList.isEmpty()) {
                    Text(text = "You don't have orders yet.", style = MaterialTheme.typography.h4)
                } else {

                    LazyColumn {
                        items(ordersList) { order ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(5f),
                                    text = "Order ${order.orderNumber}",
                                    style = MaterialTheme.typography.h4
                                )

                                Text(
                                    modifier = Modifier.weight(3f),
                                    text = "${order.totalPrice} DA",
                                    style = MaterialTheme.typography.h4
                                )

                                IconButton(
                                    modifier = Modifier
                                        .weight(2f),
                                    onClick = {
                                        navController.navigate("${Screen.OrderProductsScreen.route}/${order.id}")
                                    }
                                ) {
                                    Icon(
                                        tint = MaterialTheme.colors.primary,
                                        imageVector = Icons.Default.NavigateNext,
                                        contentDescription = "Drop-Down Arrow"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                }
            }
        }
    }
}