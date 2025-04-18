package com.daljit.eira.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.daljit.eira.ui.theme.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    onQrCodeScanned: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var scannedData by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    
    // Remember camera executor to properly dispose it
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    // Cleanup camera resources when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }
    
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Check camera permission
    LaunchedEffect(cameraPermissionState) {
        hasCameraPermission = cameraPermissionState.status.isGranted
        if (!hasCameraPermission) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Function to trigger haptic feedback
    fun triggerHapticFeedback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(50)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "QR Scanner",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = NeonBlue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GradientStart,
                    titleContentColor = NeonBlue
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            triggerHapticFeedback()
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = NeonBlue
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
    Box(
        modifier = modifier
            .fillMaxSize()
                .padding(paddingValues)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        GradientStart,
                        GradientMiddle,
                        GradientEnd
                    )
                )
                ),
            contentAlignment = Alignment.Center
    ) {
            if (hasCameraPermission) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Camera Preview with centered QR scanning box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Always show camera preview, but control analysis with isScanning flag
                        CameraPreview(
                            onQrCodeDetected = { barcodes ->
                                if (barcodes.isNotEmpty() && isScanning) {
                                    barcodes[0].rawValue?.let { qrData ->
                                        scannedData = qrData
                                        isScanning = false
                                        showSuccessAnimation = true
                                        triggerHapticFeedback()
                                        
                                        // Reset success animation after delay
                                        coroutineScope.launch {
                                            delay(1500)
                                            showSuccessAnimation = false
                                            onQrCodeScanned(qrData)
                                        }
                                    }
                                }
                            },
                            isScanning = isScanning,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Overlay for when not scanning
                        androidx.compose.animation.AnimatedVisibility(
                            visible = !isScanning,
                            enter = fadeIn(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(GlassDark.copy(alpha = 0.7f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    // Success animation when QR code is detected
                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = showSuccessAnimation,
                                        enter = scaleIn(
                                            initialScale = 0.5f,
                                            animationSpec = tween(300, easing = EaseOutBack)
                                        ) + fadeIn(animationSpec = tween(300)),
                                        exit = scaleOut(
                                            targetScale = 1.5f,
                                            animationSpec = tween(300)
                                        ) + fadeOut(animationSpec = tween(300))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = NeonGreen,
                                            modifier = Modifier.size(120.dp)
                                        )
                                    }
                                    
                                    // Default placeholder
                                    androidx.compose.animation.AnimatedVisibility(
                                        visible = !showSuccessAnimation,
                                        enter = fadeIn(animationSpec = tween(500)),
                                        exit = fadeOut(animationSpec = tween(300))
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.QrCodeScanner,
                                                contentDescription = null,
                                                tint = NeonBlue,
                                                modifier = Modifier.size(100.dp)
                                            )
                                            
                                            Spacer(modifier = Modifier.height(16.dp))
                                            
                                            Text(
                                                text = "Position QR code in the center",
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.Center,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Scanning frame with neon effect - only show when scanning
                        androidx.compose.animation.AnimatedVisibility(
                            visible = isScanning,
                            enter = fadeIn(animationSpec = tween(300)),
                            exit = fadeOut(animationSpec = tween(300))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(250.dp)
                                    .border(
                                        width = 3.dp,
                                        brush = Brush.linearGradient(
                                            colors = listOf(NeonGreen, NeonBlue, NeonPurple)
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    )
                            ) {
                                // Scanning line animation
                                val infiniteTransition = rememberInfiniteTransition(label = "scannerAnimation")
                                val scanLinePosition by infiniteTransition.animateFloat(
                                    initialValue = 0f,
                                    targetValue = 250f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1500, easing = LinearEasing),
                                        repeatMode = RepeatMode.Restart
                                    ),
                                    label = "scanLinePosition"
                                )
                                
                                // Animated scan line with neon glow
                                Box(
                                    modifier = Modifier
                                        .offset(y = scanLinePosition.dp)
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(NeonGreen, NeonBlue, NeonPurple)
                                            )
                                        )
                                        .graphicsLayer {
                                            alpha = 0.8f
                                        }
                                )
                                
                                // Corner indicators with animated glow
                                val glowIntensity by infiniteTransition.animateFloat(
                                    initialValue = 0.5f,
                                    targetValue = 1f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(1000, easing = FastOutSlowInEasing),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "glowIntensity"
                                )
                                
                                // Top-left corner
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    NeonGreen.copy(alpha = glowIntensity * 0.7f),
                                                    NeonGreen.copy(alpha = 0f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(topStart = 24.dp)
                                        )
                                        .align(Alignment.TopStart)
                                )
                                
                                // Top-right corner
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    NeonBlue.copy(alpha = glowIntensity * 0.7f),
                                                    NeonBlue.copy(alpha = 0f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(topEnd = 24.dp)
                                        )
                                        .align(Alignment.TopEnd)
                                )
                                
                                // Bottom-left corner
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    NeonBlue.copy(alpha = glowIntensity * 0.7f),
                                                    NeonBlue.copy(alpha = 0f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(bottomStart = 24.dp)
                                        )
                                        .align(Alignment.BottomStart)
                                )
                                
                                // Bottom-right corner
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    NeonPurple.copy(alpha = glowIntensity * 0.7f),
                                                    NeonPurple.copy(alpha = 0f)
                                                )
                                            ),
                                            shape = RoundedCornerShape(bottomEnd = 24.dp)
                                        )
                                        .align(Alignment.BottomEnd)
                                )
                            }
                            
                            // Scanning indicator text with animation
                            val textAlpha by rememberInfiniteTransition(label = "textAnimation").animateFloat(
                                initialValue = 0.7f,
                                targetValue = 1f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "textAlpha"
                            )
                            
                            Text(
                                text = "Scanning...",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = NeonGreen.copy(alpha = textAlpha),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 24.dp)
                                    .background(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                    
                    // 2. Text field to show scanned QR data
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = GlassDark.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                    imageVector = Icons.Default.QrCode,
                                contentDescription = null,
                                    tint = NeonPurple,
                                    modifier = Modifier.size(24.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Text(
                                    text = "Scanned Result",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                
                                Spacer(modifier = Modifier.weight(1f))
                                
                                // Copy button
                                IconButton(
                                    onClick = { triggerHapticFeedback() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            color = NeonBlue.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = NeonBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                color = GlassDark,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = scannedData ?: "No QR code scanned yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                    
                    // 3. Scan button with neon effect
                    Button(
                        onClick = { 
                            isScanning = !isScanning
                            triggerHapticFeedback()
                        },
                    modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isScanning) NeonPink else NeonBlue
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        // Button animation
                        val buttonTransition = updateTransition(
                            targetState = isScanning,
                            label = "buttonTransition"
                        )
                        
                        val iconRotation by buttonTransition.animateFloat(
                            transitionSpec = { tween(300) },
                            label = "iconRotation"
                        ) { scanning ->
                            if (scanning) 0f else 360f
                        }
                        
                        val iconSize by buttonTransition.animateDp(
                            transitionSpec = { spring(Spring.DampingRatioMediumBouncy) },
                            label = "iconSize"
                        ) { scanning ->
                            if (scanning) 28.dp else 24.dp
                        }
                        
                        Icon(
                            imageVector = if (isScanning) Icons.Default.Stop else Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            modifier = Modifier
                                .size(iconSize)
                                .graphicsLayer {
                                    rotationY = iconRotation
                                }
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = if (isScanning) "Stop Scanning" else "Scan QR Code",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            } else {
                // Camera Permission Request with neon styling
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(24.dp))
                        .shadow(elevation = 12.dp),
                    color = GlassDark.copy(alpha = 0.8f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Animated icon
                        val cameraAnimation = rememberInfiniteTransition(label = "cameraAnimation")
                        val iconScale by cameraAnimation.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "iconScale"
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            NeonBlue.copy(alpha = 0.3f),
                                            Color.Transparent
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = NeonBlue,
                                modifier = Modifier
                                    .size(60.dp)
                                    .graphicsLayer {
                                        scaleX = iconScale
                                        scaleY = iconScale
                                    }
                            )
                        }
                        
                        Text(
                            text = "Camera Permission Required",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Text(
                            text = "We need camera permission to scan QR codes. Please grant the permission to continue.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.7f)
                        )
            
                        Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                        cameraPermissionState.launchPermissionRequest()
                                triggerHapticFeedback()
                            },
                modifier = Modifier
                    .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonBlue
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PermCameraMic,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                    Text(
                                text = "Grant Permission",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(
    onQrCodeDetected: (List<Barcode>) -> Unit,
    isScanning: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val processingBarcode = remember { AtomicBoolean(false) }
    
    // Create a shared executor that will be used by both preview and analysis
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    // Clean up resources when leaving the composition
    DisposableEffect(lifecycleOwner) {
        onDispose {
            try {
                // Get the camera provider and unbind all use cases
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
            } catch (e: Exception) {
                Log.e("QRScanner", "Failed to unbind camera use cases", e)
            }
        }
    }
    
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    
                    // Preview use case
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    
                    // Image analysis use case
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                                // Only process if scanning is enabled and not already processing
                                if (isScanning && !processingBarcode.get()) {
                                    val mediaImage = imageProxy.image
                                    if (mediaImage != null) {
                                        processingBarcode.set(true)
                                        val image = InputImage.fromMediaImage(
                                            mediaImage,
                                            imageProxy.imageInfo.rotationDegrees
                                        )
                                        
                                        // Process image with ML Kit
                                        val scanner = BarcodeScanning.getClient()
                                        scanner.process(image)
                                            .addOnSuccessListener { barcodes ->
                                                if (barcodes.isNotEmpty()) {
                                                    onQrCodeDetected(barcodes)
                                                }
                                                processingBarcode.set(false)
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.e("QRScanner", "Barcode scanning failed", exception)
                                                processingBarcode.set(false)
                                            }
                                            .addOnCompleteListener {
                                                imageProxy.close()
                                            }
                                    } else {
                                        imageProxy.close()
                                        processingBarcode.set(false)
                                    }
                                } else {
                                    // If not scanning, just close the image proxy
                                    imageProxy.close()
                                }
                            }
                        }
                    
                    try {
                        // Unbind all use cases before rebinding
                        cameraProvider.unbindAll()
                        
                        // Bind use cases to camera
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )
                    } catch (exc: Exception) {
                        Log.e("QRScanner", "Use case binding failed", exc)
                    }
                } catch (e: Exception) {
                    Log.e("QRScanner", "Camera initialization failed", e)
                }
            }, ContextCompat.getMainExecutor(ctx))
            
            previewView
        },
        update = { previewView ->
            // Update logic if needed when isScanning changes
            // This ensures the preview stays responsive
            previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        },
        modifier = modifier
    )
} 