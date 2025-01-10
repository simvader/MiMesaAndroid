package com.example.mimesa.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingCartViewModel(application: Application) : AndroidViewModel(application) {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> get() = _cartItems


    fun addToCart(item: Item) {
        viewModelScope.launch{
            val existingCartItem = _cartItems.value.find { it.id == item.id }
            if (existingCartItem != null ){
                _cartItems.value = _cartItems.value.map {
                    if (it.id ==item.id) it.copy(quantity = it.quantity +1) else it
                }
            }else{
                _cartItems.value += CartItem(item.id, item.name, item.price, 1)
            }
        }
    }

    fun decreaseQuantity(item: CartItem){
        viewModelScope.launch {
            if (item.quantity > 1){
                _cartItems.value = _cartItems.value.map {
                    if (it.id == item.id) it.copy(quantity = it.quantity -1) else it
                }
            }else{
                _cartItems.value = _cartItems.value.filter { it.id != item.id }
            }
        }
    }

    fun removeFromCart(item: CartItem){
        viewModelScope.launch {
            _cartItems.value = _cartItems.value.filter { it.id != item.id }
        }
    }
}