package com.example.mimesa.ui.composables


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.navigation.NavController

@Composable
fun MenuScreen(
    categories: List<String>,
    onAddToCart: (String) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    onHelpRequest: () -> Unit,
    onRequestOrder: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToMqtt: () -> Unit,
    navController: NavController
) {
    MimesaScaffold(
        title = "Menu",
        navController = navController,
        onHelpRequest = onHelpRequest,
        onRequestOrder = onRequestOrder,
        onNavigateToCart = onNavigateToCart,
        onNavigteToMqtt = onNavigateToMqtt,
        onNavigateToMap = onNavigateToMap
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            Text(
                text = "Menu",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            categories.forEach { category ->
                Text(
                    text = category,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                LazyColumn {
                    items(5) { index ->
                        MenuItem(
                            itemName = "$category Item $index",
                            onAddToCart = { onAddToCart("$category Item $index") },
                            onRemoveFromCart = { onRemoveFromCart("$category Item $index") }
                        )
                    }
                }
            }
        }
    }
}