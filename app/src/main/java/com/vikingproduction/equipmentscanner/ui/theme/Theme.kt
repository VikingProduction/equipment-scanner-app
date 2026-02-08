package com.vikingproduction.equipmentscanner.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val Blue600 = Color(0xFF1E88E5)
val Orange500 = Color(0xFFFF9800)
val Green600 = Color(0xFF43A047)
val Red500 = Color(0xFFE53935)

private val LightColors = lightColorScheme(
    primary = Blue600, onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB), onPrimaryContainer = Color(0xFF0D47A1),
    secondary = Orange500, onSecondary = Color.White,
    tertiary = Green600, tertiaryContainer = Color(0xFFC8E6C9), onTertiaryContainer = Color(0xFF1B5E20),
    background = Color(0xFFFAFAFA), surface = Color.White, surfaceVariant = Color(0xFFEEEEEE), error = Red500
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9), onPrimary = Color(0xFF0D47A1),
    primaryContainer = Color(0xFF1565C0), onPrimaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFFFFCC80), onSecondary = Color(0xFF4E342E),
    tertiary = Color(0xFFA5D6A7), tertiaryContainer = Color(0xFF2E7D32), onTertiaryContainer = Color(0xFFC8E6C9),
    background = Color(0xFF121212), surface = Color(0xFF1E1E1E), surfaceVariant = Color(0xFF2C2C2C), error = Color(0xFFEF9A9A)
)

@Composable
fun EquipmentScannerTheme(darkTheme: Boolean = isSystemInDarkTheme(), dynamicColor: Boolean = true, content: @Composable () -> Unit) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val ctx = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(ctx) else dynamicLightColorScheme(ctx)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}
