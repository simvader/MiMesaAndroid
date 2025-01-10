package com.example.mimesa.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cartitem")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert
    suspend fun addOrUpdateCartItem(cartItem: CartItem)

    @Delete
    suspend fun removeCartItem(cartItem: CartItem)
}