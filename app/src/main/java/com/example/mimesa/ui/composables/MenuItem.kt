package com.example.mimesa.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    itemName: String,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = itemName)
        Row {
            IconButton(onClick = onRemoveFromCart) {
                Icon(Icons.Default.Delete, contentDescription = "Remove from cart")
            }
            IconButton(onClick = onAddToCart) {
                Icon(Icons.Default.Add, contentDescription = "Add to cart")
            }
        }
    }
}