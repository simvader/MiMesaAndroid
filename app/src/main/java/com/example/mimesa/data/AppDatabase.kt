package com.example.mimesa.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartItem::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}