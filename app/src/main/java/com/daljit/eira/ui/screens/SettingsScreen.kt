package com.daljit.eira.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.daljit.eira.ui.navigation.Routes
import com.daljit.eira.ui.theme.*
import kotlinx.coroutines.launch

// Define NeonRed color
private val NeonRed = Color(0xFFFF3D51)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController = rememberNavController()
) {
    var darkModeEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var bluetoothEnabled by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(true) }
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonBlue
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            GradientStart,
                            GradientMiddle,
                            GradientEnd
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile Section
                item {
                    ProfileCard()
                }
                
                // Connectivity Section
                item {
                    SettingsSection(title = "Connectivity") {
                        SettingsButton(
                            icon = Icons.Filled.Wifi,
                            iconTint = NeonOrange,
                            title = "Wi-Fi Setup",
                            subtitle = "Configure your Wi-Fi connection",
                            onClick = {
                                navController.navigate(Routes.WifiSetup.route)
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsSwitch(
                            icon = Icons.Filled.Bluetooth,
                            iconTint = NeonBlue,
                            title = "Bluetooth",
                            subtitle = if (bluetoothEnabled) "Connected" else "Disconnected",
                            checked = bluetoothEnabled,
                            onCheckedChange = { 
                                bluetoothEnabled = it
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (it) "Bluetooth enabled" else "Bluetooth disabled",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
                
                // Preferences Section
                item {
                    SettingsSection(title = "Preferences") {
                        SettingsSwitch(
                            icon = Icons.Filled.DarkMode,
                            iconTint = NeonPurple,
                            title = "Dark Mode",
                            subtitle = "Use dark theme throughout the app",
                            checked = darkModeEnabled,
                            onCheckedChange = { 
                                darkModeEnabled = it
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (it) "Dark mode enabled" else "Dark mode disabled",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsSwitch(
                            icon = Icons.Filled.Notifications,
                            iconTint = NeonYellow,
                            title = "Notifications",
                            subtitle = "Receive alerts and updates",
                            checked = notificationsEnabled,
                            onCheckedChange = { 
                                notificationsEnabled = it
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (it) "Notifications enabled" else "Notifications disabled",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsButton(
                            icon = Icons.Filled.Language,
                            iconTint = NeonGreen,
                            title = "Language",
                            subtitle = "English (US)",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Language settings coming soon",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
                
                // Security Section
                item {
                    SettingsSection(title = "Security") {
                        SettingsSwitch(
                            icon = Icons.Filled.Fingerprint,
                            iconTint = NeonPink,
                            title = "Biometric Authentication",
                            subtitle = "Use fingerprint or face ID for login",
                            checked = biometricEnabled,
                            onCheckedChange = { 
                                biometricEnabled = it
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = if (it) "Biometric authentication enabled" else "Biometric authentication disabled",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsButton(
                            icon = Icons.Filled.Security,
                            iconTint = NeonRed,
                            title = "Security Settings",
                            subtitle = "Manage app permissions and security",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Security settings coming soon",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
                
                // Device Management Section
                item {
                    SettingsSection(title = "Device Management") {
                        SettingsButton(
                            icon = Icons.Filled.DeviceHub,
                            iconTint = NeonBlue,
                            title = "Connected Devices",
                            subtitle = "Manage your IoT devices",
                            onClick = {
                                navController.navigate(Routes.IoTDeviceControl.route)
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsButton(
                            icon = Icons.Filled.AddCircle,
                            iconTint = NeonGreen,
                            title = "Add New Device",
                            subtitle = "Connect a new IoT device",
                            onClick = {
                                navController.navigate(Routes.QRScanner.route)
                            }
                        )
                    }
                }
                
                // About & Support Section
                item {
                    SettingsSection(title = "About & Support") {
                        SettingsButton(
                            icon = Icons.Filled.Info,
                            iconTint = NeonBlue,
                            title = "App Info",
                            subtitle = "Version 1.0.0",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "EIRA IoT App v1.0.0",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                        
                        Divider(color = Color.White.copy(alpha = 0.1f))
                        
                        SettingsButton(
                            icon = Icons.Filled.SupportAgent,
                            iconTint = NeonPurple,
                            title = "Support",
                            subtitle = "Get help and contact support",
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Support center coming soon",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                }
                
                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            NeonBlue.copy(alpha = 0.2f),
                            NeonPurple.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile picture with glow effect
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(NeonBlue.copy(alpha = 0.1f))
                        .drawBehind {
                            drawCircle(
                                color = NeonBlue.copy(alpha = 0.2f),
                                radius = size.maxDimension / 1.5f
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = NeonBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Account info
                Column {
                    Text(
                        text = "John Doe",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "john.doe@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = { /* Edit profile action */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = NeonBlue
                        ),
                        border = ButtonDefaults.outlinedButtonBorder,
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Edit Profile", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Section title with animation
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = NeonBlue,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )
        
        // Section content card with glassmorphism effect
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = GlassDark.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsSwitch(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val transition = updateTransition(targetState = checked, label = "switch_transition")
    
    val iconScale by transition.animateFloat(
        label = "icon_scale",
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }
    ) { if (it) 1.2f else 1f }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Animated icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .drawBehind {
                    drawCircle(
                        color = iconTint.copy(alpha = if (checked) 0.2f else 0.1f),
                        radius = size.maxDimension / 2 * iconScale
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) iconTint else iconTint.copy(alpha = 0.7f),
                modifier = Modifier.graphicsLayer(scaleX = iconScale, scaleY = iconScale)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        
        // Custom switch
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = iconTint,
                checkedTrackColor = iconTint.copy(alpha = 0.3f),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
private fun SettingsButton(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with glow effect
        Box(
            modifier = Modifier
                .size(40.dp)
                .drawBehind {
                    drawCircle(
                        color = iconTint.copy(alpha = if (isPressed) 0.3f else 0.1f),
                        radius = size.maxDimension / 2
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = iconTint.copy(alpha = 0.7f)
        )
    }
    
    // Reset pressed state after a short delay
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(200)
            isPressed = false
        }
    }
} 