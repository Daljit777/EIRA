package com.daljit.eira.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daljit.eira.ui.theme.*

/**
 * A simple pulsating loading animation
 */
@Composable
fun SimpleLoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = NeonGreen
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .scale(scale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            color,
                            color.copy(alpha = 0.3f)
                        )
                    )
                )
        )
    }
}

/**
 * A loading animation with text
 */
@Composable
fun LoadingWithText(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SimpleLoadingAnimation(
            modifier = Modifier.size(60.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * A row of three pulsating dots
 */
@Composable
fun PulsatingDots(
    modifier: Modifier = Modifier,
    color: Color = NeonGreen
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    
    val animations = List(3) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.2f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing,
                    delayMillis = index * 300
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "dot$index"
        )
    }
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        animations.forEach { animation ->
            val scale by animation
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .scale(scale)
                    .alpha(scale)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
} 