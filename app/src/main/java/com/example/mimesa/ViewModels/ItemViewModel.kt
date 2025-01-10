package com.example.mimesa.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimesa.data.Item
import com.example.mimesa.data.api.ItemApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading
    val items : StateFlow<List<Item>> = _items

    private val apiService : ItemApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ItemApiService::class.java)


    fun fetchItems(){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getItems()
                if (response.isSuccessful) {
                    _items.value = response.body() ?: emptyList()
                    Log.d("ItemViewModel", "Fetch successful: ${_items.value.size} items loaded.")
                } else {
                    Log.e("ItemViewModel", "Fetch failed with error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Fetch failed with exception: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
                Log.d("ItemViewModel", "Loading state set to false.")
            }
        }
    }
}