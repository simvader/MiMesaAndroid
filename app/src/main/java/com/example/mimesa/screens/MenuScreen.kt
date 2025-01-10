package com.example.mimesa.screens


import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mimesa.ShoppingCartViewModelFactory
import com.example.mimesa.ViewModels.ItemViewModel
import com.example.mimesa.data.ShoppingCartViewModel
import com.example.mimesa.ui.composables.MimesaScaffold

@Composable
fun MenuScreen(
    categories: List<String>,
    onAddToCart: (String) -> Unit,
    onRemoveFromCart: (String) -> Unit,
    onHelpRequest: () -> Unit,
    onRequestOrder: () -> Unit,
    onNavigateToCart: () -> Unit,
    navController: NavController
) {

    val itemViewModel : ItemViewModel = viewModel()
    val viewModelStoreOwner: ViewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
    val cartViewModel : ShoppingCartViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = ShoppingCartViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    val items by itemViewModel.items.collectAsState()
    val isLoading by itemViewModel.isLoading.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    LaunchedEffect(Unit){
        itemViewModel.fetchItems()
    }

    MimesaScaffold(
        title = "Menu",
        navController = navController,
        onHelpRequest = onHelpRequest,
        onRequestOrder = onRequestOrder,
        onNavigateToCart = onNavigateToCart,
    ) { padding ->

        if(isLoading){
            CircularProgressIndicator()
        }else{
            Column(modifier = Modifier.padding(padding)){
                items.forEach { item ->
                    val cartItem = cartItems.find { it.id == item.id }
                    if (cartItem != null) {
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text(item.name, modifier = Modifier.padding(8.dp))
                                Text(item.price.toString(), modifier = Modifier.padding(8.dp))
                                Text(item.description, modifier = Modifier.padding(8.dp))
                            }

                            Row(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { cartViewModel.decreaseQuantity(cartItem) }
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Decrease Quantity"
                                    )
                                }

                                Text(
                                    "Qty: ${cartItem.quantity}",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )

                                Button(
                                    onClick = { cartViewModel.addToCart(item) }
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase Quantity"
                                    )
                                }

                                Button(
                                    onClick = { cartViewModel.removeFromCart(cartItem) }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remove from Cart"
                                    )
                                }
                            }
                        }
                    }else{
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        ) {
                            Column {
                                Text(item.name, modifier = Modifier.padding(8.dp))
                                Text(item.price.toString(), modifier = Modifier.padding(8.dp))
                                Text(item.description, modifier = Modifier.padding(8.dp))
                            }

                            Button(
                                onClick = { cartViewModel.addToCart(item) }
                            ) {
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}