package io.github.sadellie.themmo

import android.app.WallpaperColors
import android.app.WallpaperManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import io.github.sadellie.themmo.monet.LocalTonalPalettes
import io.github.sadellie.themmo.monet.TonalPalettes.Companion.toTonalPalettes
import io.github.sadellie.themmo.monet.dynamicColorScheme


/**
 * Extract primary color from device wallpaper.
 *
 * @param context Context
 * @return Primary color of current wallpaper image.
 */
@RequiresApi(Build.VERSION_CODES.O_MR1)
fun extractWallpaperPrimary(context: Context): WallpaperColors? {
    return WallpaperManager.getInstance(context).getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
}

/**
 * Custom Monet implementation.
 *
 * @param primary Color from which colorScheme will be generated from
 * @param isLight If True will generate light theme, else dark theme
 * @return colorScheme
 */
@Composable
fun dynamicThemmo(primary: Color, isLight: Boolean): ColorScheme {
    var colorScheme: ColorScheme? = null
    // This feels so wrong...
    CompositionLocalProvider(LocalTonalPalettes provides primary.toTonalPalettes()) {
        colorScheme = dynamicColorScheme(isLight)
    }
    return colorScheme ?: if (isLight) lightColorScheme() else darkColorScheme()
}
