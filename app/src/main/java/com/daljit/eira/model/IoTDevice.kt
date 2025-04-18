package com.daljit.eira.model

/**
 * Data class representing an IoT device
 * @param id Unique identifier for the device
 * @param name Display name of the device
 * @param isOn Whether the device is turned on or off
 * @param isOnline Whether the device is connected to the network
 */
data class IoTDevice(
    val id: String,
    val name: String,
    var isOn: Boolean = false,
    var isOnline: Boolean = true
) 