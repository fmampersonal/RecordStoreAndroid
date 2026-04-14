package com.example.recordstore.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define custom color values for the app
val Green = Color(0xFF68B04D) // Main color (Green)
val White = Color(0xFFFFFFFF) // Secondary color (White)
val Yellow = Color(0xFFFCBA03) // Accent color (Yellow)

// Dark mode color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Green, // Main color for dark mode
    secondary = White, // Secondary color for dark mode
    tertiary = Yellow // Accent color for dark mode
)

// Light mode color scheme
private val LightColorScheme = lightColorScheme(
    primary = Green, // Main color for light mode
    secondary = White, // Secondary color for light mode
    tertiary = Yellow // Accent color for light mode
    /* You can override more default colors here if needed
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

/**
 * A custom theme for the BusPass application that supports both light and dark modes
 * with dynamic color schemes available for devices running Android 12+ (API level 31+).
 *
 * This composable function wraps the MaterialTheme composable and applies the appropriate
 * color scheme based on the system's current theme (light or dark) and whether dynamic color
 * is available and enabled.
 *
 * @param darkTheme A Boolean indicating whether dark mode is enabled. By default, it checks the system's current theme.
 * @param dynamicColor A Boolean that controls whether dynamic color (based on the system's wallpaper) is used. Defaults to true and works only on Android 12+.
 * @param content The composable content that will be wrapped by the theme. It is where your app's UI will be defined.
 */
@Composable
fun RecordStoreAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Checks if the system is in dark theme
    dynamicColor: Boolean = true, // Enables dynamic color for Android 12+ (API level 31+)
    content: @Composable () -> Unit // The content that will be themed
) {
    // Determine the color scheme based on the current theme and dynamic color preference
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme // Use the dark theme color scheme
        else -> LightColorScheme // Use the light theme color scheme
    }

    // Apply the MaterialTheme with the appropriate color scheme
    MaterialTheme(
        colorScheme = colorScheme, // Pass the determined color scheme
        typography = Typography, // Use the default typography defined elsewhere in the project
        content = content // The content that will use the theme
    )
}
