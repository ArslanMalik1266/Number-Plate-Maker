package com.webscare.numberplatemaker.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF01703C),           // Gov Green
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8F5E9),
    onPrimaryContainer = Color(0xFF144D29),

    secondary = Color(0xFF0038A8),         // Sindh Blue
    onSecondary = Color.White,

    background = Color(0xFFF4F6F5),        // App Background (light mode)
    onBackground = Color(0xFF1A1A1A),      // Soft Black

    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    surfaceVariant = Color(0xFFE0E3E3),
    onSurfaceVariant = Color(0xFF70757A),  // Subtitle Gray
)

// Dark Mode Colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50),           // Lighter green
    onPrimary = Color(0xFF003A1F),
    primaryContainer = Color(0xFF00522B),
    onPrimaryContainer = Color(0xFFB2E5BC),

    secondary = Color(0xFF5E92F3),         // Lighter blue
    onSecondary = Color(0xFF001A41),

    background = Color(0xFF121212),        // Dark background
    onBackground = Color(0xFFE3E3E3),      // Light text

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE3E3E3),

    surfaceVariant = Color(0xFF3F4948),
    onSurfaceVariant = Color(0xFFBFC9C8),
)