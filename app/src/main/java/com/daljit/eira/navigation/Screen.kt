package com.daljit.eira.navigation

/**
 * Enum class representing the different screens in the app
 */
enum class Screen(val route: String) {
    QRScanner("qr_scanner"),
    WiFiCredentials("wifi_credentials"),
    IoTDeviceControl("device_control")
} 