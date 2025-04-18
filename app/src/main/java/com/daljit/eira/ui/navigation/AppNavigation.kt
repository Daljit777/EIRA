package com.daljit.eira.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.daljit.eira.ui.components.NeonBottomNavigation
import com.daljit.eira.ui.components.wifi.WifiCredentialsScreen
import com.daljit.eira.ui.screens.HomeScreen
import com.daljit.eira.ui.screens.IoTDeviceControlScreen
import com.daljit.eira.ui.screens.QRScannerScreen
import com.daljit.eira.ui.screens.SettingsScreen

/**
 * Navigation routes for the app
 */
sealed class Routes {
    object Home : Routes() {
        const val route = "home"
    }
    object IoTDeviceControl : Routes() {
        const val route = "iot_device_control"
    }
    object QRScanner : Routes() {
        const val route = "qr_scanner"
    }
    object Settings : Routes() {
        const val route = "settings"
    }
    object WifiSetup : Routes() {
        const val route = "wifi_setup"
    }
}

/**
 * Main navigation component for the app
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NeonBottomNavigation(
                selectedRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Routes.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Routes.Home.route) {
                    HomeScreen(navController = navController)
                }
                
                composable(Routes.IoTDeviceControl.route) {
                    IoTDeviceControlScreen()
                }
                
                composable(Routes.QRScanner.route) {
                    QRScannerScreen()
                }
                
                composable(Routes.Settings.route) {
                    SettingsScreen()
                }
                
                composable(Routes.WifiSetup.route) {
                    WifiCredentialsScreen(
                        onConnect = { ssid, password ->
                            // Handle Wi-Fi connection
                        },
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
} 