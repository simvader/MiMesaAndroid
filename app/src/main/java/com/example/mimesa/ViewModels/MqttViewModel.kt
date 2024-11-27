package com.example.mimesa.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimesa.MqttHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MqttViewModel(private val mqttHelper: MqttHelper) : ViewModel() {
    private val _receivedMessages = MutableStateFlow<List<String>>(emptyList())
    val receivedMessages = _receivedMessages.asStateFlow()

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            mqttHelper.connect { topic, message ->
                val newMessage = "[$topic]: $message"
                _receivedMessages.value = _receivedMessages.value + newMessage
            }
        }
    }

    fun publish(topic: String, message: String) {
        mqttHelper.publish(topic, message)
    }

    fun subscribe(topic: String) {
        mqttHelper.subscribe(topic)
    }
}
