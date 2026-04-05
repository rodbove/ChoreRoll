package com.choreroll.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary = Pink,
    onPrimary = White,
    primaryContainer = PinkLight.copy(alpha = 0.2f),
    onPrimaryContainer = PinkDark,
    secondary = Gold,
    onSecondary = TextDark,
    secondaryContainer = Gold.copy(alpha = 0.2f),
    onSecondaryContainer = GoldDark,
    tertiary = PinkDark,
    tertiaryContainer = PinkLight.copy(alpha = 0.15f),
    surface = WarmCream,
    onSurface = TextDark,
    surfaceVariant = WarmCreamAlt,
    onSurfaceVariant = TextMuted,
    background = WarmCream,
    onBackground = TextDark,
    outline = Border,
    outlineVariant = Border.copy(alpha = 0.5f),
    error = PinkDark,
    onError = White,
    inverseSurface = TextDark,
    inverseOnSurface = WarmCream,
)

private val DarkColorScheme = darkColorScheme(
    primary = PinkLight,
    onPrimary = NearBlack,
    primaryContainer = PinkDark.copy(alpha = 0.4f),
    onPrimaryContainer = PinkLight,
    secondary = Gold,
    onSecondary = NearBlack,
    secondaryContainer = GoldDark.copy(alpha = 0.3f),
    onSecondaryContainer = Gold,
    tertiary = PinkLight,
    tertiaryContainer = PinkDark.copy(alpha = 0.3f),
    surface = DarkSurface,
    onSurface = WarmCream,
    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = Border,
    background = NearBlack,
    onBackground = WarmCream,
    outline = TextMuted,
    outlineVariant = TextMuted.copy(alpha = 0.3f),
    error = PinkLight,
    onError = NearBlack,
    inverseSurface = WarmCream,
    inverseOnSurface = TextDark,
)

private val ChoreRollTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.3.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
)

@Composable
fun ChoreRollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ChoreRollTypography,
        content = content
    )
}
