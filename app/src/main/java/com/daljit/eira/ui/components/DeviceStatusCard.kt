package com.daljit.eira.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daljit.eira.ui.animation.pulse
import com.daljit.eira.ui.theme.*

/**
 * A card displaying the status of an IoT device with glassmorphism effect
 * 
 * @param name The name of the device
 * @param icon The icon of the device
 * @param isOnline Whether the device is online
 * @param isPowered Whether the device is powered
 * @param color The color of the device
 * @param onClick Callback when the card is clicked
 * @param onPowerToggle Callback when the power button is toggled
 */
@Composable
fun DeviceStatusCard(
    name: String,
    icon: ImageVector,
    isOnline: Boolean,
    isPowered: Boolean,
    color: Color = NeonGreen,
    onClick: () -> Unit,
    onPowerToggle: () -> Unit
) {
    val statusColor = when {
        !isOnline -> OfflineColor
        isPowered -> OnColor
        else -> OffColor
    }
    
    val gradientColors = if (isPowered && isOnline) {
        listOf(
            color.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surface
        )
    }
    
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(Brush.verticalGradient(gradientColors))
                .padding(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Device icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(
                            if (isPowered && isOnline) {
                                color.copy(alpha = 0.1f)
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = if (isPowered && isOnline) color else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(28.dp)
                            .pulse(pulseFraction = if (isPowered && isOnline) 0.1f else 0f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Device name
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                // Status indicator and power button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Status indicator
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(statusColor)
                            .pulse(pulseFraction = if (isPowered && isOnline) 0.3f else 0f)
                    )
                    
                    // Power button
                    IconButton(
                        onClick = onPowerToggle,
                        enabled = isOnline,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isPowered) Icons.Default.Power else Icons.Default.PowerOff,
                            contentDescription = if (isPowered) "Turn Off" else "Turn On",
                            tint = if (isPowered && isOnline) color else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * A component displaying a sensor reading with an icon
 */
@Composable
private fun SensorReading(
    icon: ImageVector,
    value: String,
    label: String,
    isActive: Boolean,
    tint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isActive) tint else Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = if (isActive) Color.White else Color.Gray
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isActive) Color.White.copy(alpha = 0.7f) else Color.Gray
        )
    }
} 