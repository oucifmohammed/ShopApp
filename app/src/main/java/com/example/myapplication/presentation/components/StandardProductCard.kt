package com.example.myapplication.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.myapplication.domain.models.Product

@Composable
fun StandardProductCard(
    product: Product
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(vertical = 6.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
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
                        .clip(RoundedCornerShape(20.dp))
                        .height(225.dp),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Spacer(modifier = Modifier.width(13.dp))

                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .width(40.dp)
                            .background(Color(0xFFF5F5F5))
                    ) {
                        val image = Icons.Filled.FavoriteBorder

                        IconButton(
                            onClick = {},
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp,24.dp),
                                tint = Color(0xFF808080)
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

            Text(
                text = product.originalPrice.toString(),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
@Preview
fun StandardProductCardPreview() {
    StandardProductCard(
        product = Product(
            "123",
            "T-shirt",
            "https://firebasestorage.googleapis.com/v0/b/snplc-91bf1.appspot.com/o/default_profile_picture.png?alt=media&token=3b9853b5-1949-4ece-ab9f-c22cdf758d12",
            "Men",
            1000f,
            0f
        )
    )
}