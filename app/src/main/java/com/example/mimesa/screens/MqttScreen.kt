package com.example.mimesa.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mimesa.ViewModels.MqttViewModel

@Composable
fun MqttScreen(viewModel: MqttViewModel) {
    val topic = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val receivedMessages by viewModel.receivedMessages.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        MqttInputField(value = topic.value, label = "Topic", onValueChange = { topic.value = it })
        MqttInputField(value = message.value, label = "Message", onValueChange = { message.value = it })

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { viewModel.subscribe(topic.value) }) {
                Text("Subscribe")
            }
            Button(onClick = { viewModel.publish(topic.value, message.value) }) {
                Text("Publish")
            }
        }

        MqttMessageList(receivedMessages)
    }

    // Establish connection on first launch
    LaunchedEffect(Unit) {
        viewModel.connect()
    }
}

@Composable
fun MqttInputField(value: String, label: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@Composable
fun MqttMessageList(messages: List<String>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(messages.size) { index ->
            Text(
                text = messages[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
    }
}