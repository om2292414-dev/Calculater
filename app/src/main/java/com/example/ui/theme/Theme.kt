package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AmoledDarkColorScheme = darkColorScheme(
    primary = OrangeEquals,
    onPrimary = PureWhite,
    primaryContainer = CharcoalButton,
    onPrimaryContainer = PureWhite,
    secondary = OrangeEquals,
    onSecondary = PureWhite,
    secondaryContainer = CharcoalCard,
    onSecondaryContainer = PureWhite,
    background = AmoledBlack,
    onBackground = PureWhite,
    surface = AmoledBlack,
    onSurface = PureWhite,
    surfaceVariant = CharcoalSurface,
    onSurfaceVariant = TextSecondary,
    outline = CharcoalCardBorder,
    outlineVariant = DividerColor
)

@Composable
fun UltraCalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AmoledDarkColorScheme,
        typography = Typography,
        content = content
    )
}
