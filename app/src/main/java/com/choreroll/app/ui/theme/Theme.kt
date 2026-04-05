package com.choreroll.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = Teal80,
    secondary = Amber40,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    secondaryContainer = Amber80,
    tertiary = Green40,
    tertiaryContainer = Green80,
    surface = Gray95,
    onSurface = Gray10,
)

private val DarkColorScheme = darkColorScheme(
    primary = Teal80,
    onPrimary = Teal30,
    primaryContainer = Teal30,
    secondary = Amber80,
    onSecondary = Gray10,
    secondaryContainer = Amber40,
    tertiary = Green80,
    tertiaryContainer = Green40,
)

@Composable
fun ChoreRollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        content = content
    )
}
