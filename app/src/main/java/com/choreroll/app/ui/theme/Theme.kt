package com.choreroll.app.ui.theme

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
    primaryContainer = PinkSoft,
    onPrimaryContainer = PinkDark,
    secondary = Gold,
    onSecondary = TextDark,
    secondaryContainer = GoldSoft,
    onSecondaryContainer = GoldDark,
    tertiary = PinkDark,
    surface = Cream,
    onSurface = TextDark,
    surfaceVariant = CreamAlt,
    onSurfaceVariant = TextMuted,
    background = White,
    onBackground = TextDark,
    outline = TextLight,
    outlineVariant = TextLight.copy(alpha = 0.3f),
    inverseSurface = TextDark,
    inverseOnSurface = Cream,
    surfaceContainerLowest = White,
    surfaceContainerLow = Cream,
    surfaceContainer = CreamAlt,
)

private val DarkColorScheme = darkColorScheme(
    primary = PinkLight,
    onPrimary = NearBlack,
    primaryContainer = PinkDark.copy(alpha = 0.3f),
    onPrimaryContainer = PinkLight,
    secondary = Gold,
    onSecondary = NearBlack,
    secondaryContainer = GoldDark.copy(alpha = 0.2f),
    onSecondaryContainer = Gold,
    tertiary = PinkLight,
    surface = DarkSurface,
    onSurface = White.copy(alpha = 0.92f),
    surfaceVariant = DarkSurfaceAlt,
    onSurfaceVariant = TextLight,
    background = NearBlack,
    onBackground = White.copy(alpha = 0.92f),
    outline = TextMuted,
    outlineVariant = TextMuted.copy(alpha = 0.2f),
    inverseSurface = Cream,
    inverseOnSurface = TextDark,
    surfaceContainerLowest = NearBlack,
    surfaceContainerLow = DarkSurface,
    surfaceContainer = DarkSurfaceAlt,
)

private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 34.sp,
        letterSpacing = (-1.5).sp,
        lineHeight = 40.sp
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
        letterSpacing = (-1).sp,
        lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 28.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        letterSpacing = (-0.2).sp,
        lineHeight = 22.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.2.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        letterSpacing = 0.4.sp,
        lineHeight = 16.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.3.sp
    ),
)

@Composable
fun ChoreRollTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
