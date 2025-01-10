package com.example.mimesa.data.api

import com.example.mimesa.data.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("items/")
    suspend fun getItems(): Response<List<Item>>
}