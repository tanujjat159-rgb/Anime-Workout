package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnimeDarkColorScheme = darkColorScheme(
    primary = FlameOrange,
    onPrimary = AnimeWhite,
    primaryContainer = AnimeSurfaceVariant,
    onPrimaryContainer = SuperSaiyanGold,
    secondary = AuraCyan,
    onSecondary = AnimeOnyx,
    secondaryContainer = AnimeSurfaceVariant,
    onSecondaryContainer = AuraCyanGlow,
    tertiary = SuperSaiyanGold,
    onTertiary = AnimeOnyx,
    background = AnimeOnyx,
    onBackground = AnimeWhite,
    surface = AnimeDarkSurface,
    onSurface = AnimeWhite,
    surfaceVariant = AnimeSurfaceVariant,
    onSurfaceVariant = AnimeGrey,
    error = CrimsonRage,
    onError = AnimeWhite
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AnimeDarkColorScheme,
        typography = Typography,
        content = content
    )
}
