package com.daljit.eira.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daljit.eira.model.IoTDevice
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for managing IoT devices
 */
class IoTDeviceViewModel : ViewModel() {
    
    // List of IoT devices
    private val _devices = mutableStateListOf<IoTDevice>()
    val devices: List<IoTDevice> get() = _devices
    
    // Loading state
    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value
    
    // Error state
    private val _error = mutableStateOf<String?>(null)
    val error: String? get() = _error.value
    
    init {
        loadDevices()
    }
    
    /**
     * Load sample IoT devices
     */
    fun loadDevices() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Simulate network delay
                delay(1000)
                
                // Clear existing devices
                _devices.clear()
                
                // Add sample devices
                _devices.addAll(
                    listOf(
                        IoTDevice("1", "Smart Light - Living Room", true, true),
                        IoTDevice("2", "Smart Light - Bedroom", false, true),
                        IoTDevice("3", "Smart Thermostat", true, true),
                        IoTDevice("4", "Security Camera", true, false),
                        IoTDevice("5", "Smart Lock - Front Door", false, true),
                        IoTDevice("6", "Smart TV", false, true)
                    )
                )
            } catch (e: Exception) {
                _error.value = "Failed to load devices: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Toggle the on/off state of a device
     * @param deviceId The ID of the device to toggle
     */
    fun toggleDevice(deviceId: String) {
        val device = _devices.find { it.id == deviceId } ?: return
        
        // Only toggle if device is online
        if (device.isOnline) {
            device.isOn = !device.isOn
            
            // In a real app, you would send a command to the actual device here
            viewModelScope.launch {
                // Simulate network delay
                _isLoading.value = true
                delay(500)
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Refresh the device list
     */
    fun refreshDevices() {
        loadDevices()
    }
} 