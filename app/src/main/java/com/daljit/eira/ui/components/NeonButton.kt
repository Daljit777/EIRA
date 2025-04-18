package com.daljit.eira.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daljit.eira.ui.theme.ButtonShape
import com.daljit.eira.ui.theme.NeonBlue
import com.daljit.eira.ui.theme.NeonGreen
import com.daljit.eira.ui.theme.NeonOrange
import com.daljit.eira.ui.theme.NeonPink

/**
 * A neon-styled button with glow effect
 * 
 * @param text The text to display on the button
 * @param onClick The callback to be invoked when the button is clicked
 * @param modifier The modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param primaryColor The primary color of the button
 * @param secondaryColor The secondary color for gradient effect
 */
@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primaryColor: Color = NeonGreen,
    secondaryColor: Color = primaryColor
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animation for the glow effect
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 0.5f,
        animationSpec = tween(durationMillis = 150),
        label = "glowAlpha"
    )
    
    // Pulsating animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val buttonColor = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(primaryColor, secondaryColor)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color.Gray, Color.Gray)
        )
    }
    
    Box(
        modifier = modifier
            .shadow(
                elevation = if (enabled) 8.dp else 0.dp,
                shape = ButtonShape,
                spotColor = if (enabled) primaryColor.copy(alpha = 0.5f) else Color.Transparent
            )
    ) {
        // Glow effect
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(8.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = primaryColor.copy(alpha = glowAlpha * 0.5f),
                            style = Stroke(width = 8.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                        )
                    }
            )
        }
        
        // Button content
        Box(
            modifier = Modifier
                .clip(ButtonShape)
                .background(buttonColor)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = Color.White),
                    enabled = enabled,
                    onClick = onClick
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.7f),
                            secondaryColor.copy(alpha = 0.7f)
                        )
                    ),
                    shape = ButtonShape
                )
                .padding(horizontal = 24.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * A more intense neon button with stronger glow effect
 */
@Composable
fun IntenseNeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animation for the glow effect
    val glowAlpha by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0.7f,
        animationSpec = tween(durationMillis = 150),
        label = "glowAlpha"
    )
    
    // Pulsating animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    
    val buttonGradient = Brush.horizontalGradient(
        colors = listOf(NeonGreen, NeonOrange)
    )
    
    Box(
        modifier = modifier
            .shadow(
                elevation = if (enabled) 16.dp else 0.dp,
                shape = ButtonShape,
                spotColor = if (enabled) NeonGreen.copy(alpha = 0.7f) else Color.Transparent
            )
    ) {
        // Glow effect
        if (enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .blur(12.dp)
                    .drawBehind {
                        drawRoundRect(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    NeonGreen.copy(alpha = glowAlpha * pulseAlpha * 0.7f),
                                    NeonOrange.copy(alpha = glowAlpha * pulseAlpha * 0.7f)
                                )
                            ),
                            style = Stroke(width = 12.dp.toPx()),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx())
                        )
                    }
            )
        }
        
        // Button content
        Box(
            modifier = Modifier
                .clip(ButtonShape)
                .background(buttonGradient)
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = Color.White),
                    enabled = enabled,
                    onClick = onClick
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            NeonGreen.copy(alpha = 0.9f),
                            NeonOrange.copy(alpha = 0.9f)
                        )
                    ),
                    shape = ButtonShape
                )
                .padding(horizontal = 32.dp, vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * A small icon button with neon glow effect
 * 
 * @param onClick The callback to invoke when the button is clicked
 * @param modifier Modifier to be applied to the button
 * @param enabled Whether the button is enabled
 * @param primaryColor The primary color of the button
 * @param content The content to display inside the button
 */
@Composable
fun NeonIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primaryColor: Color = NeonBlue,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animation for glow intensity
    val glowIntensity by animateFloatAsState(
        targetValue = if (isPressed) 1f else 0.6f,
        animationSpec = tween(
            durationMillis = if (isPressed) 100 else 300,
            easing = FastOutSlowInEasing
        ),
        label = "glowIntensity"
    )
    
    // Animation for button scale
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(
            durationMillis = if (isPressed) 100 else 300,
            easing = FastOutSlowInEasing
        ),
        label = "buttonScale"
    )
    
    val buttonAlpha = if (enabled) 1f else 0.5f
    
    Box(
        modifier = modifier
            .size(48.dp)
            .scale(scale)
            .alpha(buttonAlpha),
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(glowIntensity * 0.5f)
                .blur(8.dp)
                .clip(RoundedCornerShape(50))
                .background(primaryColor)
        )
        
        // Button background
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(50))
                .background(primaryColor.copy(alpha = 0.2f))
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = primaryColor),
                    enabled = enabled,
                    onClick = onClick
                )
                .drawBehind {
                    // Draw border
                    drawCircle(
                        color = primaryColor,
                        style = Stroke(width = 2.dp.toPx()),
                        radius = size.minDimension / 2
                    )
                }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        alpha = if (isPressed) 0.7f else 1f
                    }
            ) {
                content()
            }
        }
    }
} 