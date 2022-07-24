package io.github.sadellie.themmo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// Constants for Saver
private const val AMOLED_ENABLED = "AMOLED_ENABLED"
private const val THEMING_MODE = "THEMING_MODE"
private const val DYNAMIC_ENABLED = "DYNAMIC_ENABLED"

/**
 * Controller that holds current theming options and provides methods to manipulate them.
 *
 * @param lightColorScheme Scheme as default light color scheme.
 * @param darkColorScheme Scheme as default dark color scheme.
 * @param themingMode Current [ThemingMode].
 * @param dynamicThemeEnabled When true will use dynamic theming (Monet).
 * @param amoledThemeEnabled When true will change background color to black. Only for dark theme.
 */
class ThemmoController(
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme,
    themingMode: ThemingMode,
    dynamicThemeEnabled: Boolean,
    amoledThemeEnabled: Boolean,
) {
    var currentThemingMode: ThemingMode by mutableStateOf(themingMode)
        private set

    var isDynamicThemeEnabled: Boolean by mutableStateOf(dynamicThemeEnabled)
        private set

    var isAmoledThemeEnabled: Boolean by mutableStateOf(amoledThemeEnabled)
        private set

    fun setThemingMode(mode: ThemingMode) {
        currentThemingMode = mode
    }

    @RequiresApi(value = Build.VERSION_CODES.O_MR1)
    fun enableDynamicTheme(enable: Boolean) {
        isDynamicThemeEnabled = enable
    }

    fun enableAmoledTheme(enable: Boolean) {
        isAmoledThemeEnabled = enable
    }

    internal fun provideColorScheme(
        context: Context,
        isSystemDark: Boolean,
    ): ColorScheme {
        return when (currentThemingMode) {
            ThemingMode.AUTO -> {
                if (isSystemDark) {
                    provideDarkColorScheme(context)
                } else {
                    provideLightColorScheme(context)
                }
            }
            ThemingMode.FORCE_LIGHT -> provideLightColorScheme(context)
            ThemingMode.FORCE_DARK -> provideDarkColorScheme(context)
        }
    }

    @SuppressLint("NewApi")
    private fun provideLightColorScheme(context: Context): ColorScheme {
        return when {
            // Android 12+ devices use new api
            isDynamicThemeEnabled and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
                dynamicLightColorScheme(context)
            }
            // Dynamic theming for devices with Android 8.1 up to Android 11
            isDynamicThemeEnabled and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) -> {
                // Wallpaper colors can be null. We return default theme for such cases
                val wallpaperColors = extractWallpaperPrimary(context)
                    ?: return lightColorScheme
                val primary = Color(
                    red = wallpaperColors.primaryColor.red(),
                    green = wallpaperColors.primaryColor.green(),
                    blue = wallpaperColors.primaryColor.blue()
                )
                dynamicLightThemmo(primary)
            }
            else -> lightColorScheme
        }
    }

    @SuppressLint("NewApi")
    private fun provideDarkColorScheme(context: Context): ColorScheme {
        val dark = when {
            // Android 12+ devices use new api
            isDynamicThemeEnabled and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
                dynamicDarkColorScheme(context)
            }
            // Dynamic theming for devices with Android 8.1 up to Android 11
            isDynamicThemeEnabled and (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) -> {
                // Wallpaper colors can be null. We return default theme for such cases
                val wallpaperColors = extractWallpaperPrimary(context)
                    ?: return darkColorScheme
                val primary = Color(
                    red = wallpaperColors.primaryColor.red(),
                    green = wallpaperColors.primaryColor.green(),
                    blue = wallpaperColors.primaryColor.blue()
                )
                dynamicDarkThemmo(primary)
            }
            else -> darkColorScheme
        }

        // Turning into amoled if needed
        return if (isAmoledThemeEnabled) {
            dark.copy(background = Color.Black, surface = Color.Black)
        } else {
            dark
        }
    }

    fun saveState(): Map<String, Any> {
        return mapOf(
            AMOLED_ENABLED to isAmoledThemeEnabled,
            THEMING_MODE to currentThemingMode,
            DYNAMIC_ENABLED to isDynamicThemeEnabled
        )
    }

}

enum class ThemingMode {
    /**
     * Will ask system which theme to use.
     */
    AUTO,

    /**
     * Will use light colors only
     */
    FORCE_LIGHT,

    /**
     * Will use dark colors only
     */
    FORCE_DARK
}

/**
 * Restores [ThemmoController] for saver.
 *
 * @param lightColorScheme Light color scheme.
 * @param darkColorScheme Dark color scheme.
 * @param map Map of other options from [ThemmoController] that were saved.
 * @return Restored [ThemmoController]
 */
internal fun restoreThemmoState(
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme,
    map: Map<String, Any?>
): ThemmoController {
    return ThemmoController(
        lightColorScheme,
        darkColorScheme,
        themingMode = map[THEMING_MODE] as ThemingMode,
        dynamicThemeEnabled = map[DYNAMIC_ENABLED] as Boolean,
        amoledThemeEnabled = map[AMOLED_ENABLED] as Boolean,
    )
}

/**
 * Returns saveable [ThemmoController].
 *
 * @see [ThemmoController]
 * @return [ThemmoController] with applied parameters.
 */
@Composable
fun rememberThemmoController(
    lightColorScheme: ColorScheme = lightColorScheme(),
    darkColorScheme: ColorScheme = darkColorScheme(),
    amoledThemeEnabled: Boolean = false,
    themingMode: ThemingMode = ThemingMode.AUTO,
    dynamicThemeEnabled: Boolean = false,
): ThemmoController {
    return rememberSaveable(
        saver = themmoControllerSaver(lightColorScheme, darkColorScheme)
    ) {
        ThemmoController(
            lightColorScheme = lightColorScheme,
            darkColorScheme = darkColorScheme,
            themingMode = themingMode,
            dynamicThemeEnabled = dynamicThemeEnabled,
            amoledThemeEnabled = amoledThemeEnabled
        )
    }
}

/**
 * Saver that is used in rememberSaveable method. Handles save and restore logic.
 *
 * [lightColorScheme] and [darkColorScheme] are used as parameters here. There is probably no need
 * to save colorSchemes as they are less likely to change on runtime.
 * @return Saver for [ThemmoController].
 */
internal fun themmoControllerSaver(
    lightColorScheme: ColorScheme,
    darkColorScheme: ColorScheme
): Saver<ThemmoController, *> = mapSaver(
    save = { it.saveState() },
    restore = { restoreThemmoState(lightColorScheme, darkColorScheme, it) }
)