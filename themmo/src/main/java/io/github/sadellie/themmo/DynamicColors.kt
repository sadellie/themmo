package io.github.sadellie.themmo

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
import androidx.compose.ui.graphics.toArgb
import com.shopgun.android.materialcolorcreator.MaterialColorImpl
import com.shopgun.android.materialcolorcreator.Shade

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
fun dynamicLightThemmo(primary: Color): ColorScheme {
    val materialColor = MaterialColorImpl(primary.toArgb())
    val base = Color(materialColor.value)
    val shade50 = Color(materialColor.getColor(Shade.Shade50).value)
    val shade100 = Color(materialColor.getColor(Shade.Shade100).value)
    val shade200 = Color(materialColor.getColor(Shade.Shade200).value)
    val shade300 = Color(materialColor.getColor(Shade.Shade300).value)
    val shade400 = Color(materialColor.getColor(Shade.Shade400).value)
    val shade700 = Color(materialColor.getColor(Shade.Shade700).value)
    val onShade700 = shade50.getAppropriateTextColor()

    return lightColorScheme(
        primary = base,
        onPrimary = base.getAppropriateTextColor(),
        primaryContainer = shade200,
        onPrimaryContainer = shade300,
        secondaryContainer = shade400,
        onSecondaryContainer = shade200.getAppropriateTextColor(),
        background = shade50,
        onBackground = onShade700,
        surface = shade50,
        onSurface = onShade700,
        surfaceVariant = shade100,
        onSurfaceVariant = shade50.getAppropriateTextColor(),
        outline = shade700,
        inverseOnSurface = shade100,
    )
}

/**
 * Custom Monet implementation. Generates dark colors.
 *
 * @param primary Color from which colorScheme will be generated from
 * @return Dark colorScheme
 */
fun dynamicDarkThemmo(primary: Color): ColorScheme {
    val materialColor = MaterialColorImpl(primary.toArgb())
    val base = Color(materialColor.getColor(Shade.Shade300).value)
    val shade50 = Color(0xFF161616)
    val shade900 = Color(materialColor.getColor(Shade.Shade900).value)
    val shade800 = Color(materialColor.getColor(Shade.Shade800).value)
    val shade600 = Color(materialColor.getColor(Shade.Shade600).value)
    val shade500 = Color(materialColor.getColor(Shade.Shade500).value)
    val shade200 = Color(materialColor.getColor(Shade.Shade200).value)
    val onShade700 = Color(0xFFe1e3dd)

    return darkColorScheme(
        primary = base,
        onPrimary = base.getAppropriateTextColor(),
        primaryContainer = shade800,
        onPrimaryContainer = shade600,
        secondaryContainer = shade500,
        onSecondaryContainer = shade500.getAppropriateTextColor(),
        background = shade50,
        onBackground = onShade700,
        surface = shade50,
        onSurface = onShade700,
        surfaceVariant = shade900,
        onSurfaceVariant = shade50.getAppropriateTextColor(),
        outline = shade200,
        inverseOnSurface = shade900,
    )
}
