package com.example.mimesa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mimesa.ui.composables.CartScreen
import com.example.mimesa.ui.composables.LoginScreen
import com.example.mimesa.ui.theme.MiMesaTheme
import com.example.mimesa.ui.composables.MenuScreen
import com.example.mimesa.ui.composables.PaymentScreen
import com.example.mimesa.ui.composables.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiMesaTheme {
                MimesaApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMimesaApp() {
    MimesaApp()
}

@Composable
fun MimesaApp() {
    val navController = rememberNavController()
    val rtDB = FirebaseDatabase.getInstance()
    val dbRef = rtDB.getReference("message")
    // Example categories for the menu
    val categories = listOf("Food", "Drinks")


    NavHost(navController = navController, startDestination = "login") {
        composable("menu") {
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
                navController
            )
        }
        composable("cart") {
            CartScreen(
                onBackToMenu = { navController.navigateUp() },
                onCheckout = {navController.navigate("checkout")}
            )
        }

        composable("checkout") {
            PaymentScreen(listOf("a", "b"), 10.0, {}, navController)
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("menu")
                },
                onRegisterClicked = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("menu")
                }
            )
        }
    }
}

fun authenticateUser(
    email: String,
    password: String,
    onAuthenticated: () -> Unit,
    onAuthenticationError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onAuthenticated()
            } else {
                onAuthenticationError(task.exception?.message ?: "Authentication failed")
            }
        }
}


fun registerUser(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Error desconocido")
            }

        }
}


