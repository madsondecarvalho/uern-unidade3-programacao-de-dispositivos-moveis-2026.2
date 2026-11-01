package com.example.app_movies_unidade_3_uern.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CinemaBlueDark,
    onPrimary = BackgroundDark,
    primaryContainer = CinemaBlueContainerDark,
    onPrimaryContainer = TextPrimaryDark,
    secondary = AmberTextDark,
    onSecondary = BackgroundDark,
    tertiary = CinemaRubyLight,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
    outline = CinemaBlueDark.copy(alpha = 0.4f),
    outlineVariant = SurfaceVariantDark,
    error = CinemaRubyLight,
    errorContainer = CinemaRubyContainerDark
)

private val LightColorScheme = lightColorScheme(
    primary = CinemaBlue,
    onPrimary = SurfaceLight,
    primaryContainer = CinemaBlueContainerLight,
    onPrimaryContainer = TextPrimaryLight,
    secondary = AmberTextLight,
    onSecondary = SurfaceLight,
    tertiary = CinemaRuby,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    surfaceContainer = SurfaceContainerLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
    outline = CinemaBlue.copy(alpha = 0.35f),
    outlineVariant = SurfaceVariantLight,
    error = CinemaRuby,
    errorContainer = CinemaRubyContainerLight
)

@Composable
fun Appmoviesunidade3uernTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // dynamicColor desativado para garantir a coerência da paleta cinematográfica personalizada
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
