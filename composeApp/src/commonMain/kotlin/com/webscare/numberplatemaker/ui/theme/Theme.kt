package com.webscare.numberplatemaker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ✅ Light Mode Colors
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF01703C),           // Gov Green
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF144D29),

    secondary = Color(0xFF0038A8),         // Sindh Blue
    onSecondary = Color.White,

    background = Color(0xFFF4F6F5),        // App Background (light)
    onBackground = Color(0xFF1A1A1A),      // Soft Black (light mode text)

    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    surfaceVariant = Color(0xFFE0E3E3),
    onSurfaceVariant = Color(0xFF70757A),  // Subtitle Gray (light)

    error = Color(0xFFD32F2F),             // Red Color
    onError = Color.White,
)

// ✅ Dark Mode Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),           // Lighter green for dark mode
    onPrimary = Color(0xFF003A1F),
    primaryContainer = Color(0xFF00522B),
    onPrimaryContainer = Color(0xFFB2E5BC),

    secondary = Color(0xFF5E92F3),         // Lighter blue for dark mode
    onSecondary = Color(0xFF001A41),

    background = Color(0xFF0A0D11),        // Dark background
    onBackground = Color(0xFFE3E3E3),      // Light text for dark mode

    surface = Color(0xFF151B21),
    onSurface = Color(0xFFE3E3E3),

    surfaceVariant = Color(0xFF3F4948),
    onSurfaceVariant = Color(0xFFBFC9C8),  // Subtitle Gray (dark)

    error = Color(0xFFEF5350),             // Lighter red for dark mode
    onError = Color(0xFF1A1A1A),
)

@Composable
fun NumberPlateMakerTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

// ✅ Custom Extension Properties
val ColorScheme.appBackground: Color
    @Composable get() = background

val ColorScheme.subtitleGray: Color
    @Composable get() = onSurfaceVariant

val ColorScheme.softBlack: Color
    @Composable get() = onBackground

val ColorScheme.redColor: Color
    @Composable get() = error

val ColorScheme.gradientGreenLight: Color
    @Composable get() = if (this == LightColorScheme) {
        Color(0xFF0C8A53)
    } else {
        Color(0xFF4CAF50)  // Dark mode mein adjust
    }

val ColorScheme.gradientGreenDark: Color
    @Composable get() = if (this == LightColorScheme) {
        Color(0xFF046637)
    } else {
        Color(0xFF00522B)  // Dark mode mein adjust
    }