package com.daljit.eira.ui.components.wifi

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daljit.eira.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ConnectionStatus {
    SUCCESS,
    ERROR
}

/**
 * Screen for entering Wi-Fi credentials
 * Handles secure input of SSID and password
 */
@Composable
fun WifiCredentialsScreen(
    onConnect: (String, String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var connectionStatus by remember { mutableStateOf<ConnectionStatus?>(null) }
    
    val scope = rememberCoroutineScope()
    
    Box(
        modifier = modifier
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
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = NeonBlue
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title with glow effect
            Text(
                text = "WiFi Setup",
                style = MaterialTheme.typography.headlineMedium,
                color = NeonBlue,
                modifier = Modifier.drawBehind {
                    drawCircle(
                        color = NeonBlue.copy(alpha = 0.2f),
                        radius = size.maxDimension / 2
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // SSID Field with glow effect
            OutlinedTextField(
                value = ssid,
                onValueChange = { ssid = it },
                label = { Text("WiFi Name (SSID)") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "SSID",
                        tint = NeonBlue
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = NeonBlue.copy(alpha = 0.5f),
                    focusedLabelColor = NeonBlue,
                    unfocusedLabelColor = NeonBlue.copy(alpha = 0.7f),
                    cursorColor = NeonBlue,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawCircle(
                            color = NeonBlue.copy(alpha = 0.1f),
                            radius = size.maxDimension
                        )
                    }
            )

            // Password Field with glow effect
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) 
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = NeonPink
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) 
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = NeonPink.copy(alpha = 0.7f)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonPink,
                    unfocusedBorderColor = NeonPink.copy(alpha = 0.5f),
                    focusedLabelColor = NeonPink,
                    unfocusedLabelColor = NeonPink.copy(alpha = 0.7f),
                    cursorColor = NeonPink,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawCircle(
                            color = NeonPink.copy(alpha = 0.1f),
                            radius = size.maxDimension
                        )
                    }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Connect Button with glow effect
            Button(
                onClick = {
                    if (ssid.isNotBlank() && password.isNotBlank()) {
                        isConnecting = true
                        connectionStatus = null
                        scope.launch {
                            try {
                                onConnect(ssid, password)
                                delay(1500) // Simulate connection attempt
                                connectionStatus = ConnectionStatus.SUCCESS
                            } catch (e: Exception) {
                                connectionStatus = ConnectionStatus.ERROR
                            } finally {
                                isConnecting = false
                            }
                        }
                    }
                },
                enabled = ssid.isNotBlank() && password.isNotBlank() && !isConnecting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = NeonGreen
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                NeonGreen.copy(alpha = 0.2f),
                                NeonGreen.copy(alpha = 0.3f),
                                NeonGreen.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .drawBehind {
                        drawCircle(
                            color = NeonGreen.copy(
                                alpha = if (isConnecting) 0.3f else 0.1f
                            ),
                            radius = size.maxDimension
                        )
                    }
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isConnecting) {
                        CircularProgressIndicator(
                            color = NeonGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.WifiPassword,
                            contentDescription = "Connect"
                        )
                    }
                    Text(
                        text = if (isConnecting) "Connecting..." else "Connect",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Status message
            AnimatedVisibility(
                visible = connectionStatus != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = when (connectionStatus) {
                        ConnectionStatus.SUCCESS -> "Connected successfully!"
                        ConnectionStatus.ERROR -> "Connection failed. Please try again."
                        null -> ""
                    },
                    color = when (connectionStatus) {
                        ConnectionStatus.SUCCESS -> NeonGreen
                        ConnectionStatus.ERROR -> NeonPink
                        null -> Color.White
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }
    }
} 