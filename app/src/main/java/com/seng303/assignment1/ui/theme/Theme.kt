package com.seng303.assignment1.ui.theme

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
    primary = DarkButton,
    onPrimary = DarkText,
    surface = DarkBackground,
    onSecondary = DarkOnSecondary,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceVariant = DarkSurfaceVariant,
    tertiary = DarkTertiary,
    onTertiaryContainer = DarkText,
    inversePrimary = clear_dark,
    background = DarkBackground,
    inverseOnSurface = Color.Black,
    onErrorContainer = DarkText,
    secondaryContainer = DarkSecondary,
    onSecondaryContainer = LightText,
    onTertiary = Color.DarkGray,
    surfaceContainerLow = Color.LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = LightButton,
    onSecondary = LightOnSecondary,
    tertiary = LightTertiary,
    background = LightBackground,
    surface = LightBackground,
    onSurface = Color.Black,
    inverseOnSurface = Color.White,
    onSurfaceVariant = LightOnSurfaceVariant,
    onErrorContainer = LightText,
    secondaryContainer = LightSecondary,
    onSecondaryContainer = LightText,
    onTertiary = Color.Gray,
    surfaceContainerLow = Color.hsv(203F, 0.12F, 1F)


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun NotecardAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}