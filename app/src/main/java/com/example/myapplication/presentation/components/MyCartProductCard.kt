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
import com.example.myapplication.domain.models.Product

@Composable
fun MyCartProductCard(
    product: Product,
    onDeleteButton: (Product) -> Unit
) {
    Card(
        shape = RoundedCornerShape(13.dp),
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .height(88.dp),
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
                            .data(product.image)
                            .build()
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF9FAFF))
                        .size(57.dp),
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.body1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (product.promotionPrice == 0f)
                            "Price: ${product.originalPrice} DA"
                        else
                            "Price: ${product.promotionPrice} DA",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            IconButton(
                onClick = {
                    onDeleteButton(product)
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
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