package com.ypm14.simpatik.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Accent,
    background = Background,
    surface = SurfaceWhite,
    surfaceVariant = Background,
    onPrimary = SurfaceWhite,
    onSecondary = SurfaceWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = Danger,
    outline = TextTertiary.copy(alpha = 0.3f),
    surfaceTint = Primary.copy(alpha = 0.05f),
    outlineVariant = TextTertiary.copy(alpha = 0.15f),
)

private val DarkColors = darkColorScheme(
    primary = Accent,
    secondary = Secondary,
    tertiary = Accent,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = DarkSurface,
    onPrimary = DarkBackground,
    onSecondary = DarkText,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkTextSecondary,
    error = Danger,
    outline = DarkTextTertiary.copy(alpha = 0.3f),
    surfaceTint = Accent.copy(alpha = 0.08f),
    outlineVariant = DarkTextSecondary.copy(alpha = 0.15f),
)

@Composable
fun Simpatik14Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = SimpatikTypography,
        content = content
    )
}
