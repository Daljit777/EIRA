package com.daljit.eira.ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

// Shimmer colors
private val LightShimmer = Color(0xFFEEEEEE)
private val DarkShimmer = Color(0xFFDDDDDD)

/**
 * Predefined animation specs for consistent animations throughout the app
 */
object AnimationSpecs {
    // Duration constants
    const val SHORT_DURATION = 150
    const val MEDIUM_DURATION = 300
    const val LONG_DURATION = 500
    
    // Easing
    val FastOutSlowInEasing: Easing = androidx.compose.animation.core.FastOutSlowInEasing
    val LinearOutSlowInEasing: Easing = androidx.compose.animation.core.LinearOutSlowInEasing
    val FastOutLinearInEasing: Easing = androidx.compose.animation.core.FastOutLinearInEasing
    val LinearEasing: Easing = androidx.compose.animation.core.LinearEasing
    
    // Animation specs
    val FastFadeIn: EnterTransition = fadeIn(
        animationSpec = tween(
            durationMillis = SHORT_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    
    val FastFadeOut: ExitTransition = fadeOut(
        animationSpec = tween(
            durationMillis = SHORT_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    
    val SlideInFromBottom: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> fullHeight }
    
    val SlideOutToBottom: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> fullHeight }
    
    val SlideInFromTop: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> -fullHeight }
    
    val SlideOutToTop: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> -fullHeight }
    
    val SlideInFromLeft: EnterTransition = slideInHorizontally(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> -fullWidth }
    
    val SlideOutToLeft: ExitTransition = slideOutHorizontally(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> -fullWidth }
    
    val SlideInFromRight: EnterTransition = slideInHorizontally(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> fullWidth }
    
    val SlideOutToRight: ExitTransition = slideOutHorizontally(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> fullWidth }
    
    val ScaleIn: EnterTransition = scaleIn(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        ),
        initialScale = 0.8f
    )
    
    val ScaleOut: ExitTransition = scaleOut(
        animationSpec = tween(
            durationMillis = MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        ),
        targetScale = 0.8f
    )
}

/**
 * Common enter transitions
 */
@ExperimentalAnimationApi
object EnterTransitions {
    val FadeIn: EnterTransition = fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    
    val SlideInFromBottom: EnterTransition = slideInVertically(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> fullHeight }
    
    val SlideInFromRight: EnterTransition = slideInHorizontally(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> fullWidth }
    
    val FadeInWithScale: EnterTransition = fadeIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) + scaleIn(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        ),
        initialScale = 0.9f
    )
}

/**
 * Common exit transitions
 */
@ExperimentalAnimationApi
object ExitTransitions {
    val FadeOut: ExitTransition = fadeOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    )
    
    val SlideOutToBottom: ExitTransition = slideOutVertically(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullHeight -> fullHeight }
    
    val SlideOutToLeft: ExitTransition = slideOutHorizontally(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) { fullWidth -> -fullWidth }
    
    val FadeOutWithScale: ExitTransition = fadeOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        )
    ) + scaleOut(
        animationSpec = tween(
            durationMillis = AnimationSpecs.MEDIUM_DURATION,
            easing = FastOutSlowInEasing
        ),
        targetScale = 0.9f
    )
}

/**
 * Utility class for animations used throughout the app
 */
object AnimationUtils {
    
    /**
     * Creates a pulsing animation for a composable
     */
    @Composable
    fun PulseAnimation(
        pulseFraction: Float = 0.1f,
        duration: Int = 1000,
        content: @Composable (animationModifier: Modifier) -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val scale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1f + pulseFraction,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "scale"
        )
        
        content(Modifier.scale(scale))
    }
    
    /**
     * Creates a floating animation for a composable
     */
    @Composable
    fun FloatingAnimation(
        floatHeight: Float = 10f,
        duration: Int = 2000,
        content: @Composable (animationModifier: Modifier) -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "float")
        val translationY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = -floatHeight,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "translationY"
        )
        
        content(Modifier.graphicsLayer { this.translationY = translationY })
    }
    
    /**
     * Creates a shimmer animation for loading states
     */
    @Composable
    fun ShimmerAnimation(
        content: @Composable (animationModifier: Modifier) -> Unit
    ) {
        val shimmerColors = listOf(
            LightShimmer,
            DarkShimmer,
            LightShimmer
        )
        
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnim by transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1200,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        )
        
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 1000f, translateAnim - 1000f),
            end = Offset(translateAnim, translateAnim)
        )
        
        content(Modifier.background(brush))
    }
    
    /**
     * Creates a wave animation for a composable
     */
    @Composable
    fun WaveAnimation(
        waveWidth: Float = 20f,
        duration: Int = 2000,
        content: @Composable (animationModifier: Modifier) -> Unit
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "wave")
        val phase by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2 * PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "phase"
        )
        
        val waveTranslationY = sin(phase) * waveWidth
        
        content(Modifier.graphicsLayer { translationY = waveTranslationY })
    }
    
    /**
     * Creates a fade-in animation for a composable
     */
    @Composable
    fun FadeInAnimation(
        duration: Int = 500,
        delay: Int = 0,
        content: @Composable () -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        
        LaunchedEffect(Unit) {
            delay(delay.toLong())
            visible = true
        }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(duration))
        ) {
            content()
        }
    }
    
    /**
     * Creates a slide-in animation for a composable
     */
    @Composable
    fun SlideInAnimation(
        duration: Int = 500,
        delay: Int = 0,
        initialOffsetY: Int = 300,
        content: @Composable () -> Unit
    ) {
        var visible by remember { mutableStateOf(false) }
        
        LaunchedEffect(Unit) {
            delay(delay.toLong())
            visible = true
        }
        
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { initialOffsetY },
                animationSpec = tween(duration)
            ) + fadeIn(animationSpec = tween(duration))
        ) {
            content()
        }
    }
}

/**
 * Creates a pulsing animation modifier
 * 
 * @param pulseFraction The fraction to pulse by (e.g. 0.05f for 5% size change)
 * @param duration The duration of one pulse cycle in milliseconds
 * @param repeatMode The repeat mode for the animation
 * @return A modifier with the pulse animation applied
 */
@Composable
fun Modifier.pulse(
    pulseFraction: Float = 0.05f,
    duration: Int = 1000,
    repeatMode: RepeatMode = RepeatMode.Reverse
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + pulseFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = repeatMode
        ),
        label = "pulseScale"
    )
    
    return this.scale(scale)
}

/**
 * Creates a floating animation modifier
 * 
 * @param offsetY The vertical offset to float by in dp
 * @param duration The duration of one float cycle in milliseconds
 * @param repeatMode The repeat mode for the animation
 * @return A modifier with the floating animation applied
 */
@Composable
fun Modifier.float(
    offsetY: Float = 5f,
    duration: Int = 2000,
    repeatMode: RepeatMode = RepeatMode.Reverse
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "float")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = offsetY,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = repeatMode
        ),
        label = "floatOffset"
    )
    
    return this.offset { IntOffset(0, offset.roundToInt()) }
}

/**
 * Creates a breathing animation modifier that combines pulsing and alpha changes
 * 
 * @param breatheFraction The fraction to breathe by (e.g. 0.05f for 5% size change)
 * @param alphaFraction The fraction to change alpha by (e.g. 0.2f for 20% alpha change)
 * @param duration The duration of one breathe cycle in milliseconds
 * @return A modifier with the breathing animation applied
 */
@Composable
fun Modifier.breathe(
    breatheFraction: Float = 0.05f,
    alphaFraction: Float = 0.2f,
    duration: Int = 2000
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f + breatheFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheScale"
    )
    
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f - alphaFraction,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheAlpha"
    )
    
    return this.scale(scale).alpha(alpha)
}

/**
 * Creates a shimmer effect animation
 * 
 * @param duration The duration of one shimmer cycle in milliseconds
 * @return A modifier with the shimmer animation applied
 */
@Composable
fun Modifier.shimmer(
    duration: Int = 1500
): Modifier {
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by shimmerTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )
    
    return this.offset { IntOffset(shimmerOffset.roundToInt(), 0) }
} 