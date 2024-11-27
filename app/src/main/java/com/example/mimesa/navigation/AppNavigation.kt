package com.example.mimesa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mimesa.BuildConfig
import com.example.mimesa.MqttHelper
import com.example.mimesa.ViewModels.MqttViewModel
import com.example.mimesa.screens.CartScreen
import com.example.mimesa.screens.LoginScreen
import com.example.mimesa.screens.MapScreen
import com.example.mimesa.screens.MenuScreen
import com.example.mimesa.screens.MqttScreen
import com.example.mimesa.screens.PaymentScreen
import com.example.mimesa.screens.RegisterScreen
import com.google.firebase.database.FirebaseDatabase

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val mqttHelper = remember {
        MqttHelper(brokerUrl = BuildConfig.HIVEMQ_URL, clientId = BuildConfig.HIVEMQ_CLIENT)
    }
    NavHost(navController = navController, startDestination = Routes.Login.route) {
        val categories = listOf("Food", "Drinks")
        val rtDB = FirebaseDatabase.getInstance()
        val dbRef = rtDB.getReference("message")
        val mqttViewModel = MqttViewModel(mqttHelper)


        composable(Routes.Menu.route) {
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
                // Push a "Help" notification to Firebase
                val helpNotification = mapOf(
                    "type" to "help_request",
                    "message" to "User has requested help",
                    "timestamp" to System.currentTimeMillis()
                )
                dbRef.child("notifications").push().setValue(helpNotification)
                    .addOnSuccessListener {
                        println("Help notification sent successfully")
                    }
                    .addOnFailureListener {
                        println("Failed to send help notification: ${it.message}")
                    }
            },
            onRequestOrder = {
                // Handle order request logic here
                println("Order requested")
            },
            onNavigateToCart = {
                navController.navigate("cart")
            },
            onNavigateToMap = {
                navController.navigate("map")
            },
            onNavigateToMqtt = {
                navController.navigate("mqtt")
            },
            navController
            )
        }
        composable(Routes.Cart.route) { CartScreen(
            onBackToMenu = { navController.navigateUp() },
            onCheckout = {navController.navigate("checkout")}
        ) }
        composable(Routes.Checkout.route) { PaymentScreen(10.0, {}, navController)}
        composable(Routes.Login.route) { LoginScreen(
            onLoginSuccess = {
                navController.navigate("menu")
            },
            onRegisterClicked = {
                navController.navigate("register")
            }
        ) }
        composable(Routes.Register.route) { RegisterScreen(
            onRegisterSuccess = {
                navController.navigate("menu")
            }
        ) }
        composable(Routes.Map.route) { MapScreen() }
        composable(Routes.Mqtt.route) { MqttScreen(mqttViewModel) }
    }
}