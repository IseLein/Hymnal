package com.example.hymnal.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6DDBB6),
    onPrimary = Color(0xFF003828),
    primaryContainer = Color(0xFF00513C),
    onPrimaryContainer = Color(0xFF89F8D3),
    inversePrimary = Color(0xFF006C51),

    secondary = Color(0xFFB1CCC2),
    onSecondary = Color(0xFF1D352E),
    secondaryContainer = Color(0xFF334B44),
    onSecondaryContainer = Color(0xFFCDE8DE),

    tertiary = Color(0xFF85CCDF),
    onTertiary = Color(0xFF00344A),
    tertiaryContainer = Color(0xFF214B5F),
    onTertiaryContainer = Color(0xFFC1E8FC),

    background = Color(0xFF191C1B),
    onBackground = Color(0xFFE1E3E0),
    surface = Color(0xFF191C1B),
    onSurface = Color(0xFFE1E3E0),
    surfaceVariant = Color(0xFF3F4945),
    onSurfaceVariant = Color(0xFFBFC9C4),
    surfaceTint = Color(0xFF6DDBB6),

    inverseSurface = Color(0xFFE1E3E0),
    inverseOnSurface = Color(0xFF191C1B),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    outline = Color(0xFF89938F),
    outlineVariant = Color(0xFF3F4945),
    scrim = Color(0xFF000000),

    surfaceBright = Color(0xFF363939),
    surfaceContainer = Color(0xFF1D2021),
    surfaceContainerHigh = Color(0xFF282B2A),
    surfaceContainerHighest = Color(0xFF333635),
    surfaceContainerLow = Color(0xFF191C1B),
    surfaceContainerLowest = Color(0xFF0D0F0E),
    surfaceDim = Color(0xFF121413),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006C51),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF89F8D3),
    onPrimaryContainer = Color(0xFF002116),
    inversePrimary = Color(0xFF6DDBB6),

    secondary = Color(0xFF4B635B),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCDE8DE),
    onSecondaryContainer = Color(0xFF072019),

    tertiary = Color(0xFF3D6374),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC1E8FC),
    onTertiaryContainer = Color(0xFF001F2A),

    background = Color(0xFFFBFDF9),
    onBackground = Color(0xFF191C1B),
    surface = Color(0xFFFBFDF9),
    onSurface = Color(0xFF191C1B),
    surfaceVariant = Color(0xFFDBE5E0),
    onSurfaceVariant = Color(0xFF3F4945),
    surfaceTint = Color(0xFF006C51),

    inverseSurface = Color(0xFF2E3130),
    inverseOnSurface = Color(0xFFEFF1EE),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = Color(0xFF6F7975),
    outlineVariant = Color(0xFFBFC9C4),
    scrim = Color(0xFF000000),

    surfaceBright = Color(0xFFFBFDF9),
    surfaceContainer = Color(0xFFEEF0EE),
    surfaceContainerHigh = Color(0xFFE8EAE8),
    surfaceContainerHighest = Color(0xFFE2E4E2),
    surfaceContainerLow = Color(0xFFF3F5F3),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFDBDDDB),
)

@Composable
fun HymnalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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