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
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import android.graphics.Color as AndroidColor

private val mValuePercentConversion = floatArrayOf(
    1.06f,
    0.70f,
    0.50f,
    0.30f,
    0.15f,
    0.00f,
    -0.10f,
    -0.25f,
    -0.42f,
    -0.59f
)

fun getModifiedHue(hue: Float, shade: Float): Float {
    var newHue = hue
    return if (shade > 500) {
        newHue /= 360.0f
        val hueAt900 = 1.003f * newHue - 0.016f
        newHue += (hueAt900 - newHue) / (900f - 500f) * (shade - 500f)
        newHue * 360.0f
    } else {
        newHue
    }
}

fun getModifiedSaturation(saturation: Float, shade: Float): Float {
    if (shade == 500f) {
        return saturation
    }
    return if (shade < 500f) {
        val f = 0.136f * saturation - 0.025f
        val satAt50 = max(f, 0.0f)
        (saturation - satAt50) / (500 - 50) * (shade - 50) + satAt50
    } else {
        val satAt900 =
            min(-1.019f * saturation * saturation + 2.283f * saturation - 0.281f, 1.0f)
        (satAt900 - saturation) / (900 - 500) * (shade - 500) + saturation
    }
}

fun getModifiedValue(value: Float, shade: Float): Float {
    if (shade == 500f) {
        return value
    }
    val indexFloat = shade / 100.0f
    val indexFloor = floor(indexFloat.toDouble()).toInt()
    val indexCeil = ceil(indexFloat.toDouble()).toInt()
    val max: Int = mValuePercentConversion.size - 1
    val lowerIndex = min(max(indexFloor, 0), max)
    val upperIndex = min(max(indexCeil, 0), max)
    val lowerPercent: Float =
        mValuePercentConversion[lowerIndex]
    val valuePercent: Float = if (lowerIndex != upperIndex) {
        val upperPercent: Float = mValuePercentConversion[upperIndex]
        val deltaPercent = upperPercent - lowerPercent
        val deltaIndex = (upperIndex - lowerIndex).toFloat()
        lowerPercent + deltaPercent / deltaIndex * (indexFloat - lowerIndex.toFloat())
    } else {
        lowerPercent
    }
    return if (shade < 500f) {
        value + (1.0f - value) * valuePercent
    } else {
        value + value * valuePercent
    }
}

private fun Color.getModifiedColor(shade: Float): Color {
    var modified = this
    val argb = this.toArgb()
    if (shade != 500f) {
        val hsv = FloatArray(3)
        AndroidColor.colorToHSV(argb, hsv)
        hsv[0] = getModifiedHue(hsv[0], shade)
        hsv[1] = getModifiedSaturation(hsv[1], shade)
        hsv[2] = getModifiedValue(hsv[2], shade)
        modified = Color(AndroidColor.HSVToColor(AndroidColor.alpha(argb), hsv))
    }
    return modified
}

private fun Color.shade50() = this.getModifiedColor(50f)
private fun Color.shade100() = this.getModifiedColor(100f)
private fun Color.shade200() = this.getModifiedColor(200f)
private fun Color.shade300() = this.getModifiedColor(300f)
private fun Color.shade400() = this.getModifiedColor(400f)
// private fun Color.shade500() = this.getModifiedColor(500f) not needed
private fun Color.shade600() = this.getModifiedColor(600f)
private fun Color.shade700() = this.getModifiedColor(700f)
private fun Color.shade800() = this.getModifiedColor(800f)
private fun Color.shade900() = this.getModifiedColor(900f)

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
    val shade50 = primary.shade50()
    val shade100 = primary.shade100()
    val shade200 = primary.shade200()
    val shade300 = primary.shade300()
    val shade400 = primary.shade400()
    val shade700 = primary.shade700()
    val onShade700 = shade50.getAppropriateTextColor()

    return lightColorScheme(
        primary = primary,
        onPrimary = primary.getAppropriateTextColor(),
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
    val base = primary.shade300()
    val shade50 = Color(0xFF161616)
    val shade900 = primary.shade900()
    val shade800 = primary.shade800()
    val shade600 = primary.shade600()
    val shade500 = primary.shade50()
    val shade200 = primary.shade200()
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
