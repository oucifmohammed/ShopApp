package com.example.myapplication.presentation.animations

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.presentation.components.ShimmerSearchProductCardItem

@Composable
fun LoadingRecipeListShimmer(
    imageHeight: Dp,
    padding: Dp = 16.dp
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {

        val cardWidthPx = with(LocalDensity.current) {
            (maxWidth).toPx()
        }

        val cardHeightPx = with(LocalDensity.current) {
            (imageHeight).toPx()
        }

        val gradientWidth = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition()

        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = cardWidthPx,
            animationSpec = infiniteRepeatable(
                animation = TweenSpec(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delay = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = cardHeightPx,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300,
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.9f)
        )


        LazyColumn {
            items(5) {
                ShimmerSearchProductCardItem(
                    colors = colors,
                    xShimmer = xCardShimmer.value,
                    yShimmer = yCardShimmer.value,
                    cardHeight = imageHeight,
                    gradientWidth = gradientWidth,
                    padding = padding,
                )
            }
        }

    }
}