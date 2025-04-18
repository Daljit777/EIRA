package com.daljit.eira.data

/**
 * Data class representing an IoT device
 * 
 * @param id Unique identifier for the device
 * @param name Display name of the device
 * @param category Category of the device
 * @param isOn Whether the device is turned on
 * @param isOnline Whether the device is connected to the network
 * @param type Type of device (e.g., Light, Thermostat, Camera)
 * @param lastActivity When the device was last active
 * @param properties Additional properties specific to the device type
 */
data class IoTDevice(
    val id: String,
    val name: String,
    val category: HomeCategory,
    var isOn: Boolean = false,
    var isOnline: Boolean = true,
    val type: String = "Unknown",
    val lastActivity: String = "Unknown",
    val properties: Map<String, String> = emptyMap()
)

enum class HomeCategory {
    ONE_BHK,
    TWO_BHK,
    THREE_BHK
} 