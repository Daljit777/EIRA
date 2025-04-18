package com.daljit.eira.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.daljit.eira.ui.theme.GlassDark
import com.daljit.eira.ui.theme.GlassLight
import com.daljit.eira.ui.theme.GlassmorphismShape
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.draw.shadow

/**
 * A card with a glassmorphism effect (frosted glass appearance)
 * 
 * @param modifier Modifier to be applied to the card
 * @param cornerRadius The radius of the card corners
 * @param gradientColors Colors for the gradient background
 * @param content The content of the card
 */
@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    gradientColors: List<Color> = emptyList(),
    content: @Composable BoxScope.() -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val glassColor = if (isDarkTheme) GlassDark else GlassLight
    
    val shape = if (cornerRadius != 20.dp) {
        androidx.compose.foundation.shape.RoundedCornerShape(cornerRadius)
    } else {
        GlassmorphismShape
    }
    
    val backgroundBrush = if (gradientColors.isNotEmpty()) {
        Brush.linearGradient(gradientColors)
    } else {
        Brush.verticalGradient(
            colors = if (isDarkTheme) {
                listOf(
                    glassColor.copy(alpha = 0.7f),
                    glassColor.copy(alpha = 0.3f)
                )
            } else {
                listOf(
                    glassColor.copy(alpha = 0.9f),
                    glassColor.copy(alpha = 0.6f)
                )
            }
        )
    }
    
    Box(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = shape,
                spotColor = if (isDarkTheme) 
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) 
                else 
                    Color.Gray.copy(alpha = 0.5f)
            )
            .clip(shape)
            .background(backgroundBrush)
            .padding(16.dp),
        content = content
    )
}

/**
 * A glassmorphism card with a neon glow effect
 */
@Composable
fun GlowingGlassmorphismCard(
    modifier: Modifier = Modifier,
    glowColor: Color,
    isActive: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val shadowColor = if (isActive) glowColor else Color.Transparent
    
    Box(
        modifier = modifier
            .shadow(
                elevation = if (isActive) 16.dp else 8.dp,
                shape = GlassmorphismShape,
                spotColor = shadowColor.copy(alpha = if (isActive) 0.7f else 0f)
            )
            .clip(GlassmorphismShape)
            .background(
                Brush.verticalGradient(
                    colors = if (isDarkTheme) {
                        listOf(
                            GlassDark.copy(alpha = 0.8f),
                            GlassDark.copy(alpha = 0.4f)
                        )
                    } else {
                        listOf(
                            GlassLight.copy(alpha = 0.9f),
                            GlassLight.copy(alpha = 0.7f)
                        )
                    }
                )
            )
            .padding(16.dp),
        content = content
    )
} 