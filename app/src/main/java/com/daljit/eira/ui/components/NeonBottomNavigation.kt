package com.daljit.eira.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.daljit.eira.ui.navigation.Routes
import com.daljit.eira.ui.theme.*

data class NavItem(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: String,
    val glowColor: Color = NeonBlue
)

private val navItems = listOf(
    NavItem(
        route = Routes.Home.route,
        icon = Icons.Rounded.Home,
        selectedIcon = Icons.Filled.Home,
        label = "Home",
        glowColor = NeonGreen
    ),

    NavItem(
        route = Routes.QRScanner.route,
        icon = Icons.Rounded.QrCodeScanner,
        selectedIcon = Icons.Filled.QrCodeScanner,
        label = "Scan",
        glowColor = NeonPurple
    ),
    NavItem(
        route = Routes.WifiSetup.route,
        icon = Icons.Rounded.Wifi,
        selectedIcon = Icons.Filled.Wifi,
        label = "Wi-Fi",
        glowColor = NeonOrange
    ),
    NavItem(
        route = Routes.Settings.route,
        icon = Icons.Rounded.Settings,
        selectedIcon = Icons.Filled.Settings,
        label = "Settings",
        glowColor = NeonPink
    )
)

@Composable
fun NeonBottomNavigation(
    selectedRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color.Transparent,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            GlassDark.copy(alpha = 0.95f),
                            GlassDark.copy(alpha = 0.85f)
                        )
                    )
                )
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 0.dp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                navItems.forEach { item ->
                    val selected = selectedRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onNavigate(item.route) },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .drawBehind {
                                        if (selected) {
                                            drawCircle(
                                                color = item.glowColor.copy(alpha = 0.2f),
                                                radius = size.width / 1.5f
                                            )
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (selected) item.glowColor else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selected) item.glowColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Transparent,
                            selectedTextColor = Color.Transparent,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color.Transparent,
                            unselectedTextColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
} 