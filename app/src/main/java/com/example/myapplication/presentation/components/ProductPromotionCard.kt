package com.example.myapplication.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.domain.models.Product
import com.example.myapplication.presentation.util.Constants.PROMOTION_CARD_WIDTH
import com.example.myapplication.presentation.util.Constants.PROMOTION_PRODUCT_IMAGE_HEIGHT

@Composable
fun ProductPromotionCard(
    product: Product,
    onSelect: (String) -> Unit,
    onToggleLikeButton: (Product) -> Unit,
    isLiked: Boolean
) {

    Card(
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .clickable {
                onSelect(product.id)
            }
            .padding(horizontal = 6.dp)
            .width(PROMOTION_CARD_WIDTH.dp),
        elevation = 4.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFF9FAFF))
                    .fillMaxWidth()
            ) {

                Image(
                    painter = rememberImagePainter(
                        request = ImageRequest
                            .Builder(LocalContext.current)
                            .placeholder(R.color.grey)
                            .data(product.image)
                            .build()
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RectangleShape)
                        .height(PROMOTION_PRODUCT_IMAGE_HEIGHT.dp),
                    contentDescription = "",
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {

                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .width(40.dp)
                            .background(Color(0xFFF5F5F5))
                    ) {

                        IconButton(
                            onClick = {
                                onToggleLikeButton(product)
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp, 24.dp),
                                tint = if (isLiked) Color.Red else Color(0xFF808080)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.subtitle1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.category,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row {
                Text(
                    text = "${product.originalPrice} DA",
                    style = MaterialTheme.typography.body1,
                    textDecoration = TextDecoration.LineThrough
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "${product.promotionPrice} DA",
                    style = MaterialTheme.typography.body1
                )
            }

        }
    }
}