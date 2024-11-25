package com.example.mimesa

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mimesa.ui.composables.CartScreen
import com.example.mimesa.ui.composables.LoginScreen
import com.example.mimesa.ui.theme.MiMesaTheme
import com.example.mimesa.ui.composables.MenuScreen
import com.example.mimesa.ui.composables.PaymentScreen
import com.example.mimesa.ui.composables.RegisterScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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
    val mqttHelper = remember {
        MqttHelper(brokerUrl = BuildConfig.HIVEMQ_URL, clientId = BuildConfig.HIVEMQ_CLIENT)
    }



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
                onNavigateToMap = {
                    navController.navigate("map")
                },
                onNavigateToMqtt = {
                    navController.navigate("mqtt")
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

        composable("map") {
            MapScreen(onBack = { navController.navigateUp() })
        }

        composable("mqtt") {
            MqttScreen(mqttHelper = mqttHelper)
        }
    }
}

@Composable
fun MapScreen(onBack: ()-> Unit){
    AndroidView(
        factory = { context ->
            val frameLayout = FrameLayout(context).apply {
                id = View.generateViewId() // Genera un ID único
            }

            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val mapFragment = SupportMapFragment.newInstance()

            // Configurar un callback para agregar el fragmento después de inflar la vista
            frameLayout.post {
                fragmentManager.beginTransaction()
                    .replace(frameLayout.id, mapFragment, "map_fragment")
                    .commitNow()

                // Configurar el mapa
                mapFragment.getMapAsync { googleMap ->
                    configureMap(googleMap)
                }
            }

            frameLayout
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun MqttScreen(mqttHelper: MqttHelper) {
    val topic = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val receivedMessages = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = topic.value,
            onValueChange = { topic.value = it },
            label = { Text("Topic") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = message.value,
            onValueChange = { message.value = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                mqttHelper.subscribe(topic.value)
            }) {
                Text("Subscribe")
            }
            Button(onClick = {
                mqttHelper.publish(topic.value, message.value)
            }) {
                Text("Publish")
            }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(receivedMessages.size) { index ->
                Text(text = receivedMessages[index])
            }
        }
    }

    LaunchedEffect(Unit) {
        mqttHelper.connect(onMessageReceived = { topic, message ->
            receivedMessages.add("[$topic]: $message")
        })
    }
}



private fun configureMap(map: GoogleMap) {
    val sydney = LatLng(-34.0, 151.0)
    map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))

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


