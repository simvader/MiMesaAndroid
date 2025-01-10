package com.example.mimesa

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mimesa.data.ShoppingCartViewModel

class ShoppingCartViewModelFactory (private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingCartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShoppingCartViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}