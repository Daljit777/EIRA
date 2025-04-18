package com.daljit.eira.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.daljit.eira.ui.theme.NeonGlowGreen
import com.daljit.eira.ui.theme.NeonGreen

/**
 * A custom switch with neon glow effect
 * 
 * @param checked Whether the switch is checked
 * @param onCheckedChange Callback to be invoked when the switch is clicked
 * @param modifier Modifier to be applied to the switch
 * @param enabled Whether the switch is enabled
 * @param activeColor The color of the switch when it's active
 * @param inactiveColor The color of the switch when it's inactive
 * @param glowColor The color of the neon glow
 */
@Composable
fun NeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeColor: Color = NeonGreen,
    inactiveColor: Color = Color.Gray,
    glowColor: Color = NeonGlowGreen
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    // Animate the thumb position
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset"
    )
    
    // Animate the glow intensity
    val glowAlpha by animateFloatAsState(
        targetValue = if (checked) 0.7f else 0.0f,
        animationSpec = tween(durationMillis = 200),
        label = "glowAlpha"
    )
    
    // Animate the scale of the thumb
    val thumbScale by animateFloatAsState(
        targetValue = if (checked) 1.1f else 1.0f,
        animationSpec = tween(durationMillis = 200),
        label = "thumbScale"
    )
    
    // Track background gradient
    val trackBrush = if (checked) {
        Brush.horizontalGradient(
            colors = listOf(
                activeColor.copy(alpha = 0.6f),
                activeColor
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                inactiveColor.copy(alpha = 0.6f),
                inactiveColor.copy(alpha = 0.3f)
            )
        )
    }
    
    // Track container
    Box(
        modifier = modifier
            .width(52.dp)
            .height(28.dp)
            .clip(CircleShape)
            .background(trackBrush)
            .border(
                width = 1.dp,
                color = if (checked) activeColor else inactiveColor.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 24.dp),
                enabled = enabled,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb
        Box(
            modifier = Modifier
                .padding(start = 2.dp)
                .offset(x = thumbOffset)
                .size(24.dp)
                .scale(thumbScale)
                .shadow(
                    elevation = if (checked) 8.dp else 4.dp,
                    shape = CircleShape,
                    spotColor = if (checked) glowColor.copy(alpha = glowAlpha) else Color.Transparent
                )
                .clip(CircleShape)
                .background(
                    if (checked) {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                activeColor.copy(alpha = 0.8f)
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                Color.LightGray
                            )
                        )
                    }
                )
        )
    }
}

/**
 * A custom switch with a more pronounced neon glow effect
 */
@Composable
fun GlowingNeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    glowColor: Color = NeonGlowGreen
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    // Animate the thumb position
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset"
    )
    
    // Animate the glow intensity
    val glowAlpha by animateFloatAsState(
        targetValue = if (checked) 0.8f else 0.0f,
        animationSpec = tween(durationMillis = 200),
        label = "glowAlpha"
    )
    
    // Track container
    Box(
        modifier = modifier
            .width(52.dp)
            .height(28.dp)
            .shadow(
                elevation = if (checked) 8.dp else 2.dp,
                shape = CircleShape,
                spotColor = if (checked) glowColor.copy(alpha = glowAlpha) else Color.Transparent
            )
            .clip(CircleShape)
            .background(
                if (checked) {
                    Brush.horizontalGradient(
                        colors = listOf(
                            activeColor.copy(alpha = 0.7f),
                            activeColor
                        )
                    )
                } else {
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Gray.copy(alpha = 0.3f),
                            Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }
            )
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 24.dp),
                enabled = enabled,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // Thumb with glow
        Box(
            modifier = Modifier
                .padding(start = 2.dp)
                .offset(x = thumbOffset)
                .size(24.dp)
                .shadow(
                    elevation = if (checked) 12.dp else 4.dp,
                    shape = CircleShape,
                    spotColor = if (checked) glowColor.copy(alpha = glowAlpha) else Color.Transparent
                )
                .clip(CircleShape)
                .background(
                    if (checked) {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                glowColor.copy(alpha = 0.8f)
                            )
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White,
                                Color.LightGray
                            )
                        )
                    }
                )
        )
    }
} 