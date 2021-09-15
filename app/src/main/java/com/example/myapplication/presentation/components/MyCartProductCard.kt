package com.example.myapplication.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.domain.models.CartProduct

@Composable
fun MyCartProductCard(
    product: CartProduct,
    onDeleteButton: (CartProduct) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(70.dp),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 17.dp)
        ) {
            Row {
                Image(
                    painter = rememberImagePainter(
                        request = ImageRequest
                            .Builder(LocalContext.current)
                            .placeholder(R.color.grey)
                            .data(product.imageUrl)
                            .build()
                    ),
                    modifier = Modifier
                        .background(Color(0xFFF9FAFF))
                        .size(40.dp,40.dp),
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.h6
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Size: ${product.size}",
                        style = MaterialTheme.typography.body2
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Price: ${product.price} DA",
                        style = MaterialTheme.typography.body2,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }


            IconButton(
                onClick = {
                    onDeleteButton(product)
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp,48.dp)
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
    }
}