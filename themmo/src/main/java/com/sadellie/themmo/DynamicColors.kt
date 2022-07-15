package com.sadellie.themmo

import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/**
 * Makes color brighter
 *
 * @param ratio Ratio from 0 to 1. 1 means white.
 * @return Brighter version of this color
 */
@Stable
private fun Color.shiftTo255(ratio: Float): Color {
    return this.copy(
        alpha,
        red + (1.0f - red) * ratio,
        green + (1.0f - green) * ratio,
        blue + (1.0f - blue) * ratio,
    )
}

/**
 * Makes color darker
 *
 * @param ratio Ratio from 0 to 1. 1 means black.
 * @return Darker version of this color
 */
@Stable
private fun Color.shiftTo0(ratio: Float): Color {
    return this.copy(
        alpha,
        red * (1.0f - ratio),
        green * (1.0f - ratio),
        blue * (1.0f - ratio),
    )
}

/**
 * Decides which colors fits the best for the color that is used as a background
 */
@Stable
private fun Color.getAppropriateTextColor(): Color {
    return if (luminance() > 0.5) Color.Black else Color.White
}

/**
 * Extract primary color from device wallpaper.
 *
 * @param context Context
 * @return Primary color of current wallpaper image.
 */
@RequiresApi(Build.VERSION_CODES.O_MR1)
fun extractWallpaperPrimary(context: Context): WallpaperColors? {
    return WallpaperManager.getInstance(context)
        .getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
}

/**
 * Custom Monet implementation. Generates light colors.
 *
 * @param primary Color from which colorScheme will be generated from
 * @return Light colorScheme
 */
fun dynamicLightThemmo(
    primary: Color
): ColorScheme {
    val secondary: Color = primary.shiftTo255(0.5f)
    val background = primary.shiftTo255(0.9f)
    val onBackground = background.getAppropriateTextColor()

    return lightColorScheme(
        primary = primary,
        onPrimary = primary.getAppropriateTextColor(),
        onPrimaryContainer = primary.shiftTo0(0.7f),
        secondaryContainer = secondary,
        onSecondaryContainer = secondary.getAppropriateTextColor(),
        background = background,
        onBackground = onBackground,
        surface = background,
        onSurface = onBackground,
        surfaceVariant = primary.shiftTo255(0.5f),
        onSurfaceVariant = primary.shiftTo0(0.80f),
        outline = primary.shiftTo0(0.8f),
    )
}

/**
 * Custom Monet implementation. Generates dark colors.
 *
 * @param primary Color from which colorScheme will be generated from
 * @return Dark colorScheme
 */
fun dynamicDarkThemmo(primary: Color): ColorScheme {
    val secondary: Color = primary.shiftTo0(0.7f)
    val background = primary.shiftTo0(0.9f)
    val onBackground = background.getAppropriateTextColor()

    return darkColorScheme(
        primary = primary,
        onPrimary = primary.getAppropriateTextColor(),
        onPrimaryContainer = primary.shiftTo255(0.7f),
        secondaryContainer = secondary,
        onSecondaryContainer = secondary.getAppropriateTextColor(),
        background = background,
        onBackground = onBackground,
        surface = background,
        onSurface = onBackground,
        surfaceVariant = primary.shiftTo0(0.5f),
        onSurfaceVariant = primary.shiftTo255(0.80f),
        outline = primary.shiftTo255(0.8f),
    )
}
