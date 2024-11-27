package com.example.mimesa.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Menu : Routes("menu")
    object Cart : Routes("cart")
    object Checkout : Routes("checkout")
    object Register : Routes("register")
    object Map : Routes("map")
    object Mqtt : Routes("mqtt")
}