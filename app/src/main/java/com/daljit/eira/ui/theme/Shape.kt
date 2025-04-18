package com.daljit.eira.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Standard shapes for Material Design 3
val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(20.dp)
)

// Custom shapes for specialized UI elements
val GlassmorphismShape = RoundedCornerShape(20.dp)
val DeviceCardShape = RoundedCornerShape(16.dp)
val ButtonShape = RoundedCornerShape(12.dp)
val IconButtonShape = RoundedCornerShape(12.dp)
val BottomNavShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
val FloatingActionButtonShape = RoundedCornerShape(16.dp)
val ChipShape = RoundedCornerShape(8.dp)
val SearchBarShape = RoundedCornerShape(24.dp)
val ModalShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
val StatusIndicatorShape = RoundedCornerShape(4.dp) 