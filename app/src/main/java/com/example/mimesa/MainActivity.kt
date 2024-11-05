package com.example.mimesa

import android.media.tv.TvContract.Channels.Logo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mimesa.ui.theme.MiMesaTheme
import com.example.mimesa.ui.composables.MenuScreen
import com.example.mimesa.ui.composables.PaymentScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiMesaTheme {
                MimesaApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMimesaApp() {
    MimesaApp()
}

@Composable
fun MimesaApp() {
    val navController = rememberNavController()
    // Example categories for the menu
    val categories = listOf("Food", "Drinks")

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(
                categories = categories,
                onAddToCart = { itemName ->
                    // Handle adding item to cart logic here
                    println("Added $itemName to cart")
                },
                onRemoveFromCart = { itemName ->
                    // Handle removing item from cart logic here
                    println("Removed $itemName from cart")
                },
                onHelpRequest = {
                    // Handle help request logic here
                    println("Help requested")
                },
                onRequestOrder = {
                    // Handle order request logic here
                    println("Order requested")
                },
                onNavigateToCart = {
                    navController.navigate("cart")
                },
                navController
            )
        }
        composable("cart") {
            CartScreen(
                onBackToMenu = { navController.navigateUp() },
                onCheckout = {navController.navigate("checkout")}
            )
        }

        composable("checkout") {
            PaymentScreen(listOf("a", "b"), 10.0, {}, navController)
        }

    }
}


@Composable
fun CartScreen(
    onBackToMenu: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text(
            text = "Cart",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Replace this placeholder content with actual cart items
        LazyColumn {
            items(3) { index ->
                Text(
                    text = "Cart Item $index",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        Button(
            onClick = onBackToMenu,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text("Back to Menu")
        }


        Button(
            onClick = onCheckout,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text("Checkout")
        }
    }
}