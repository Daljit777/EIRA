package com.daljit.eira.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.daljit.eira.data.HomeCategory
import com.daljit.eira.data.IoTDevice
import com.daljit.eira.ui.components.GlassmorphismCard
import com.daljit.eira.ui.components.NeonSwitch
import com.daljit.eira.ui.navigation.Routes
import com.daljit.eira.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    // State for devices
    var devices by remember {
        mutableStateOf(
            listOf(
                // 1BHK Devices
                IoTDevice("1", "Living Room Light", HomeCategory.ONE_BHK, true, true, "Light", "2 min ago"),
                IoTDevice("2", "Bedroom AC", HomeCategory.ONE_BHK, false, true, "AC", "5 min ago"),
                IoTDevice("3", "Hall Fan", HomeCategory.ONE_BHK, true, true, "Fan", "8 min ago"),
                IoTDevice("4", "Hall TV", HomeCategory.ONE_BHK, false, true, "TV", "10 min ago"),
                IoTDevice("5", "Kitchen Light", HomeCategory.ONE_BHK, true, true, "Light", "15 min ago"),
                IoTDevice("6", "Kitchen Microwave", HomeCategory.ONE_BHK, false, false, "Appliance", "1 hour ago"),
                
                // 2BHK Devices
                IoTDevice("7", "Master Bedroom Light", HomeCategory.TWO_BHK, true, true, "Light", "3 min ago"),
                IoTDevice("8", "Master Bedroom AC", HomeCategory.TWO_BHK, false, true, "AC", "7 min ago"),
                IoTDevice("9", "Second Bedroom Light", HomeCategory.TWO_BHK, true, true, "Light", "12 min ago"),
                IoTDevice("10", "Second Bedroom AC", HomeCategory.TWO_BHK, false, true, "AC", "20 min ago"),
                IoTDevice("11", "Hall TV", HomeCategory.TWO_BHK, true, true, "TV", "25 min ago"),
                IoTDevice("12", "Hall Light", HomeCategory.TWO_BHK, false, true, "Light", "30 min ago"),
                IoTDevice("13", "Kitchen Light", HomeCategory.TWO_BHK, true, true, "Light", "35 min ago"),
                IoTDevice("14", "Kitchen Refrigerator", HomeCategory.TWO_BHK, false, false, "Appliance", "1 hour ago"),
                
                // 3BHK Devices
                IoTDevice("15", "Master Bedroom Light", HomeCategory.THREE_BHK, true, true, "Light", "4 min ago"),
                IoTDevice("16", "Master Bedroom AC", HomeCategory.THREE_BHK, false, true, "AC", "9 min ago"),
                IoTDevice("17", "Second Bedroom Light", HomeCategory.THREE_BHK, true, true, "Light", "14 min ago"),
                IoTDevice("18", "Second Bedroom AC", HomeCategory.THREE_BHK, false, true, "AC", "22 min ago"),
                IoTDevice("19", "Third Bedroom Light", HomeCategory.THREE_BHK, true, true, "Light", "28 min ago"),
                IoTDevice("20", "Third Bedroom Fan", HomeCategory.THREE_BHK, false, false, "Fan", "40 min ago"),
                IoTDevice("21", "Hall TV", HomeCategory.THREE_BHK, true, true, "TV", "45 min ago"),
                IoTDevice("22", "Hall Light", HomeCategory.THREE_BHK, false, true, "Light", "50 min ago"),
                IoTDevice("23", "Kitchen Light", HomeCategory.THREE_BHK, true, true, "Light", "55 min ago"),
                IoTDevice("24", "Kitchen Appliance", HomeCategory.THREE_BHK, false, false, "Appliance", "2 hours ago")
            )
        )
    }
    
    // State for refresh animation
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshRotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "refreshRotation"
    )
    
    // State for selected category
    var selectedCategory by remember { mutableStateOf<HomeCategory?>(null) }
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Function to refresh devices
    fun refreshDevices() {
        scope.launch {
            isRefreshing = true
            delay(1500) // Simulate network delay
            
            // Randomize some device states to simulate refresh
            devices = devices.map { 
                if (Math.random() > 0.7) {
                    it.copy(isOnline = Math.random() > 0.2)
                } else {
                    it
                }
            }
            
            snackbarHostState.showSnackbar(
                message = "Devices refreshed",
                duration = SnackbarDuration.Short
            )
            
            isRefreshing = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "EIRA Smart Home",
                        style = MaterialTheme.typography.headlineMedium,
                        color = NeonBlue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                actions = {
                    // QR Scanner button
                    IconButton(
                        onClick = { navController.navigate(Routes.QRScanner.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "QR Scanner",
                            tint = NeonPurple
                        )
                    }
                    
                    // Settings button
                    IconButton(
                        onClick = { navController.navigate(Routes.Settings.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = NeonPink
                        )
                    }
                    
                    // Refresh button with animation
                    IconButton(
                        onClick = { refreshDevices() },
                        enabled = !isRefreshing
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Devices",
                            tint = NeonGreen,
                            modifier = Modifier.rotate(refreshRotation)
                        )
                    }
                }
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
            // Loading indicator
            if (isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.TopCenter),
                    color = NeonGreen
                )
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header section with summary
                item {
                    DeviceSummaryCard(
                        devices = devices,
                        onViewAllClick = { navController.navigate(Routes.IoTDeviceControl.route) }
                    )
                }
                
                // Category filter chips
                item {
                    CategoryFilterChips(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = if (selectedCategory == category) null else category
                        }
                    )
                }
                
                // Devices grouped by category
                val filteredCategories = if (selectedCategory != null) {
                    listOf(selectedCategory!!)
                } else {
                    HomeCategory.values().toList()
                }
                
                filteredCategories.forEach { category ->
                    val categoryDevices = devices.filter { it.category == category }
                    if (categoryDevices.isNotEmpty()) {
                        item {
                            CategoryHeader(category)
                        }
                        
                        items(categoryDevices) { device ->
                            DeviceItem(
                                device = device,
                                onToggle = { isOn ->
                                    // Only toggle if device is online
                                    if (device.isOnline) {
                                        devices = devices.map {
                                            if (it.id == device.id) it.copy(isOn = isOn) else it
                                        }
                                        
                                        // Show feedback
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "${device.name} turned ${if (isOn) "ON" else "OFF"}",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    } else {
                                        // Show error message if device is offline
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "${device.name} is offline",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            )
                        }
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
fun DeviceSummaryCard(
    devices: List<IoTDevice>,
    onViewAllClick: () -> Unit
) {
    GlassmorphismCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onViewAllClick),
        gradientColors = listOf(
            NeonBlue.copy(alpha = 0.2f),
            NeonPurple.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Smart Home Dashboard",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(
                    icon = Icons.Filled.Devices,
                    iconTint = NeonBlue,
                    value = devices.size.toString(),
                    label = "Total Devices"
                )
                
                SummaryItem(
                    icon = Icons.Filled.PowerSettingsNew,
                    iconTint = NeonGreen,
                    value = devices.count { it.isOn && it.isOnline }.toString(),
                    label = "Active"
                )
                
                SummaryItem(
                    icon = Icons.Filled.SignalWifiStatusbarConnectedNoInternet4,
                    iconTint = NeonPink,
                    value = devices.count { !it.isOnline }.toString(),
                    label = "Offline"
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onViewAllClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NeonBlue
                ),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("View All Devices")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun SummaryItem(
    icon: ImageVector,
    iconTint: Color,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .drawBehind {
                    drawCircle(
                        color = iconTint.copy(alpha = 0.2f),
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
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChips(
    selectedCategory: HomeCategory?,
    onCategorySelected: (HomeCategory) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        HomeCategory.values().forEach { category ->
            val isSelected = selectedCategory == category
            val categoryColor = when(category) {
                HomeCategory.ONE_BHK -> NeonBlue
                HomeCategory.TWO_BHK -> NeonGreen
                HomeCategory.THREE_BHK -> NeonPurple
            }
            
            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = { 
                    Text(
                        text = category.name.replace("_", " "),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.Transparent,
                    labelColor = Color.White,
                    selectedContainerColor = categoryColor.copy(alpha = 0.3f),
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = categoryColor.copy(alpha = 0.5f),
                    selectedBorderColor = categoryColor
                ),
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = categoryColor
                        )
                    }
                } else null
            )
        }
    }
}

@Composable
fun CategoryHeader(category: HomeCategory) {
    val categoryColor = when(category) {
        HomeCategory.ONE_BHK -> NeonBlue
        HomeCategory.TWO_BHK -> NeonGreen
        HomeCategory.THREE_BHK -> NeonPurple
    }
    
    val categoryIcon = when(category) {
        HomeCategory.ONE_BHK -> Icons.Default.Home
        HomeCategory.TWO_BHK -> Icons.Default.Apartment
        HomeCategory.THREE_BHK -> Icons.Default.LocationCity
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon with background
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            categoryColor.copy(alpha = 0.7f),
                            categoryColor.copy(alpha = 0.3f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = categoryIcon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = category.name.replace("_", " "),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
    
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        color = categoryColor.copy(alpha = 0.3f),
        thickness = 2.dp
    )
}

@Composable
fun DeviceItem(
    device: IoTDevice,
    onToggle: (Boolean) -> Unit
) {
    val deviceTypeColor = when(device.type.lowercase()) {
        "light" -> NeonGreen
        "ac" -> NeonBlue
        "fan" -> NeonOrange
        "tv" -> NeonPink
        else -> Color.Gray
    }
    
    val deviceIcon = when(device.type.lowercase()) {
        "light" -> Icons.Default.LightbulbCircle
        "ac" -> Icons.Default.AcUnit
        "fan" -> Icons.Default.Air
        "tv" -> Icons.Default.Tv
        else -> Icons.Default.DevicesOther
    }
    
    // Animation for card elevation
    val elevation by animateDpAsState(
        targetValue = if (device.isOn && device.isOnline) 8.dp else 2.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardElevation"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = GlassDark.copy(alpha = 0.5f),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Device icon with glow effect
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Transparent)
                        .drawBehind {
                            drawCircle(
                                color = if (device.isOn && device.isOnline)
                                    deviceTypeColor.copy(alpha = 0.3f)
                                else
                                    deviceTypeColor.copy(alpha = 0.1f),
                                radius = size.maxDimension / 1.5f
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = deviceIcon,
                        contentDescription = null,
                        tint = if (device.isOn && device.isOnline) 
                            deviceTypeColor
                        else 
                            deviceTypeColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(28.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Device info
                Column {
                    Text(
                        text = device.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Status row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Online/Offline indicator
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (device.isOnline) 
                                        deviceOnline.copy(alpha = if (device.isOn) 1f else 0.7f)
                                    else 
                                        deviceOffline
                                )
                        )
                        
                        Text(
                            text = if (device.isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (device.isOnline) 
                                Color.White
                            else 
                                Color.White.copy(alpha = 0.6f)
                        )
                        
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                        
                        // Last activity
                        Text(
                            text = device.lastActivity,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                }
            }
            
            // Toggle switch
            NeonSwitch(
                checked = device.isOn,
                onCheckedChange = { onToggle(it) },
                enabled = device.isOnline,
                activeColor = deviceTypeColor,
                glowColor = when(device.type.lowercase()) {
                    "light" -> NeonGlowGreen
                    "ac" -> NeonGlowBlue
                    "fan" -> NeonGlowOrange
                    "tv" -> NeonGlowPink
                    else -> Color.Gray.copy(alpha = 0.5f)
                }
            )
        }
    }
}
