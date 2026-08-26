package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryCyan,
    onPrimary = BrandIndigoDark,
    primaryContainer = PrimaryCyanVariant,
    onPrimaryContainer = Color.White,
    secondary = SecondaryCoral,
    onSecondary = Color.White,
    secondaryContainer = SecondaryCoralVariant,
    onSecondaryContainer = Color.White,
    tertiary = AccentGold,
    onTertiary = BrandIndigoDark,
    background = BrandIndigoDark,
    onBackground = TextPrimaryLight,
    surface = BrandSurfaceDark,
    onSurface = TextPrimaryLight,
    surfaceVariant = BrandCardDark,
    onSurfaceVariant = TextSecondaryLight,
    outline = BrandBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryCyanVariant,
    onPrimary = Color.White,
    primaryContainer = PrimaryCyan,
    onPrimaryContainer = BrandIndigoDark,
    secondary = SecondaryCoral,
    onSecondary = Color.White,
    secondaryContainer = SecondaryCoralVariant,
    onSecondaryContainer = Color.White,
    tertiary = AccentGoldDark,
    onTertiary = Color.White,
    background = Color(0xFFF0F4F8),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to vibrant dark game theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
