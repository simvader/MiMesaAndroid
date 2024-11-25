package com.example.mimesa
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttHelper(
    private val brokerUrl: String,
    private val clientId: String
) {
    private lateinit var mqttClient: MqttClient

    fun connect(
        username: String = BuildConfig.HIVEMQ_USERNAME,
        password: String = BuildConfig.HIVEMQ_PASSWORD,
        onMessageReceived: (topic: String, message: String) -> Unit
    ) {
        try {
            mqttClient = MqttClient(brokerUrl, clientId, null)
            val options = MqttConnectOptions().apply {
                isCleanSession = true
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    userName = username
                    this.password = password.toCharArray()
                }
            }
            mqttClient.setCallback(object : MqttCallback {
                override fun connectionLost(cause: Throwable?) {
                    // Handle disconnection
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    onMessageReceived(topic ?: "", message?.toString() ?: "")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    // Handle delivery complete
                }
            })
            mqttClient.connect(options)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String) {
        if (mqttClient.isConnected) {
            mqttClient.subscribe(topic)
        }
    }

    fun publish(topic: String, message: String) {
        if (mqttClient.isConnected) {
            mqttClient.publish(topic, MqttMessage(message.toByteArray()))
        }
    }

    fun disconnect() {
        if (mqttClient.isConnected) {
            mqttClient.disconnect()
        }
    }
}
