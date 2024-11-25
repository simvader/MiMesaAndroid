package com.example.mimesa.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MimesaScaffold(
    title: String,
    navController: NavController,
    onHelpRequest: () -> Unit = {},
    onRequestOrder: () -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    onNavigateToMap: () -> Unit = {},
    onNavigteToMqtt: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(onClick = onHelpRequest) {
                        Text("Help")
                    }
                    Button(onClick = onRequestOrder) {
                        Text("Order")
                    }
                    Button(onClick = onNavigateToCart) {
                        Text("Cart")
                    }
                    Button(onClick = onNavigateToMap) {
                        Text("Map")
                    }
                    Button(onClick = onNavigteToMqtt) {
                        Text("Mqtt")
                    }
                }
            )
        },
        content = content
    )
}