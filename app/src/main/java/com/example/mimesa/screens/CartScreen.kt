package com.example.mimesa.screens

import android.app.Activity
import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mimesa.ShoppingCartViewModelFactory
import com.example.mimesa.data.ShoppingCartViewModel
import com.example.mimesa.ui.composables.MimesaScaffold


@Composable
fun CartScreen(
    onBackToMenu: () -> Unit = {},
    onCheckout: () -> Unit = {},
    navController : NavController
) {
    val viewModelStoreOwner: ViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    val cartViewModel: ShoppingCartViewModel = viewModel<ShoppingCartViewModel>(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = ShoppingCartViewModelFactory(LocalContext.current.applicationContext as Application)
    )
    val cartItems by cartViewModel.cartItems.collectAsState(initial = emptyList())

    MimesaScaffold(
        title = "Cart",
        navController = navController,
        onHelpRequest = {},
        onRequestOrder = {},
        onNavigateToCart = {}
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            cartItems.forEach { cartItem ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(cartItem.name, modifier = Modifier.padding(8.dp))
                        Text("Price: ${cartItem.price}", modifier = Modifier.padding(8.dp))
                        Text("Quantity: ${cartItem.quantity}", modifier = Modifier.padding(8.dp))
                    }
                    Button(
                        onClick = { cartViewModel.removeFromCart(cartItem) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Text("Remove")
                    }
                }
            }
            // Add Checkout button at the bottom
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
}
