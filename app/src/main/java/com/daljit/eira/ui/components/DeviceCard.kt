package com.daljit.eira.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.daljit.eira.data.IoTDevice
import com.daljit.eira.ui.theme.*

/**
 * A card displaying IoT device information with glassmorphism effect
 * 
 * @param device The IoT device to display
 * @param onToggle Callback to be invoked when the device is toggled
 * @param modifier Modifier to be applied to the card
 */
@Composable
fun GlassmorphismDeviceCard(
    device: IoTDevice,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Determine device icon based on name
    val deviceIcon = remember(device.name) {
        when {
            device.name.contains("Light", ignoreCase = true) -> Icons.Outlined.Lightbulb
            device.name.contains("Thermostat", ignoreCase = true) -> Icons.Outlined.Thermostat
            device.name.contains("Camera", ignoreCase = true) -> Icons.Outlined.Videocam
            device.name.contains("TV", ignoreCase = true) -> Icons.Outlined.Tv
            device.name.contains("Lock", ignoreCase = true) -> Icons.Outlined.Lock
            else -> Icons.Outlined.DeviceHub
        }
    }
    
    // Determine status color
    val statusColor = remember(device.isOnline) {
        if (device.isOnline) NeonGreen else Color.Red
    }
    
    // Determine glow color based on device state
    val glowColor = remember(device.isOn, device.isOnline) {
        when {
            !device.isOnline -> Color.Red.copy(alpha = 0.5f)
            device.isOn -> NeonGlowGreen
            else -> Color.Transparent
        }
    }
    
    // Animate the glow intensity
    val glowAlpha by animateFloatAsState(
        targetValue = if (device.isOn && device.isOnline) 0.7f else 0.0f,
        animationSpec = tween(durationMillis = 500),
        label = "glowAlpha"
    )
    
    // Animate the icon rotation for active devices
    val iconRotation by animateFloatAsState(
        targetValue = if (device.isOn && device.isOnline) 5f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "iconRotation"
    )
    
    GlowingGlassmorphismCard(
        modifier = modifier.fillMaxWidth(),
        glowColor = glowColor,
        isActive = device.isOn && device.isOnline
    ) {
        Column {
            // Main content row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Device icon with background
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = if (device.isOn && device.isOnline) 8.dp else 2.dp,
                            shape = CircleShape,
                            spotColor = glowColor.copy(alpha = glowAlpha)
                        )
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = deviceIcon,
                        contentDescription = null,
                        tint = if (device.isOn && device.isOnline) 
                            MaterialTheme.colorScheme.onPrimaryContainer 
                        else 
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(iconRotation)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Device info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Status indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                                .shadow(
                                    elevation = if (device.isOnline) 4.dp else 0.dp,
                                    shape = CircleShape,
                                    spotColor = statusColor.copy(alpha = 0.5f)
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (device.isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // Device state
                        Text(
                            text = if (device.isOn) "ON" else "OFF",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (device.isOn) 
                                NeonGreen 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
                
                // Toggle switch
                GlowingNeonSwitch(
                    checked = device.isOn,
                    onCheckedChange = { onToggle() },
                    enabled = device.isOnline,
                    activeColor = NeonGreen,
                    glowColor = NeonGlowGreen
                )
                
                // Expand/collapse button
                IconButton(
                    onClick = { expanded = !expanded },
                    icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Expanded content
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Device details
                    DeviceDetailRow(
                        label = "Device ID",
                        value = device.id,
                        icon = Icons.Outlined.Info
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Last activity
                    DeviceDetailRow(
                        label = "Last Activity",
                        value = device.lastActivity, // This would come from the device in a real app
                        icon = Icons.Outlined.AccessTime
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Device type
                    DeviceDetailRow(
                        label = "Type",
                        value = device.type,
                        icon = deviceIcon
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Quick actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton(
                            icon = Icons.Outlined.Refresh,
                            label = "Refresh",
                            onClick = { /* Refresh device status */ }
                        )
                        
                        ActionButton(
                            icon = Icons.Outlined.Settings,
                            label = "Settings",
                            onClick = { /* Open device settings */ }
                        )
                        
                        ActionButton(
                            icon = Icons.Outlined.Delete,
                            label = "Remove",
                            onClick = { /* Remove device */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeviceDetailRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.width(80.dp)
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun IconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

private fun deviceTypeFromName(name: String): String {
    return when {
        name.contains("Light", ignoreCase = true) -> "Light"
        name.contains("Thermostat", ignoreCase = true) -> "Thermostat"
        name.contains("Camera", ignoreCase = true) -> "Camera"
        name.contains("TV", ignoreCase = true) -> "TV"
        name.contains("Lock", ignoreCase = true) -> "Lock"
        else -> "Generic Device"
    }
} 