package com.example.myapplication.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CategoryCard(
    category: String,
    isSelected: Boolean = false,
    onSelectedCategoryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit
) {

    Surface(
        modifier = Modifier
            .height(40.dp)
            .padding(end = 16.dp)
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedCategoryChanged(category)
                    onExecuteSearch()
                }
            ),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.primary
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = category,
            style = MaterialTheme.typography.button,
            color = Color.White
        )
    }
}