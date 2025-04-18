package com.daljit.eira.utils

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Analyzer for QR codes using ML Kit
 * 
 * @param onQRCodeScanned Callback to be invoked when a QR code is successfully scanned
 * @param onError Callback to be invoked when an error occurs during scanning
 */
class QRCodeAnalyzer(
    private val onQRCodeScanned: (String) -> Unit,
    private val onError: (String) -> Unit
) : ImageAnalysis.Analyzer {
    
    private val scanner = BarcodeScanning.getClient()
    private val isScanning = AtomicBoolean(false)
    private val TAG = "QRCodeAnalyzer"
    private val THROTTLE_TIMEOUT_MS = 1500L
    private var lastScanTime = 0L
    
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        // Skip processing if we're still processing a previous image
        if (isScanning.get()) {
            imageProxy.close()
            return
        }
        
        // Throttle scanning to prevent rapid callbacks
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastScanTime < THROTTLE_TIMEOUT_MS) {
            imageProxy.close()
            return
        }
        
        // Mark that we're processing an image
        isScanning.set(true)
        
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            isScanning.set(false)
            onError("Failed to acquire image")
            return
        }
        
        try {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        // Process the first valid QR code found
                        for (barcode in barcodes) {
                            if (barcode.format == Barcode.FORMAT_QR_CODE) {
                                barcode.rawValue?.let { qrData ->
                                    if (qrData.isNotEmpty()) {
                                        lastScanTime = System.currentTimeMillis()
                                        onQRCodeScanned(qrData)
                                        return@addOnSuccessListener
                                    }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Barcode scanning failed: ${e.message}", e)
                    onError("QR code scanning failed: ${e.message ?: "Unknown error"}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                    isScanning.set(false)
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing image for QR scanning", e)
            onError("Error processing image: ${e.message ?: "Unknown error"}")
            imageProxy.close()
            isScanning.set(false)
        }
    }
    
    /**
     * Releases resources used by the QR code analyzer
     */
    fun shutdown() {
        try {
            scanner.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error shutting down barcode scanner", e)
        }
    }
} 