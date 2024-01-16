package io.github.sadellie.themmo

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
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.res.colorResource
import io.github.sadellie.themmo.core.MonetMode
import io.github.sadellie.themmo.core.ThemingMode

// Constants for Saver
private const val AMOLED_ENABLED = "AMOLED_ENABLED"
private const val THEMING_MODE = "THEMING_MODE"
private const val DYNAMIC_ENABLED = "DYNAMIC_ENABLED"
private const val CUSTOM_COLOR = "CUSTOM_COLOR"
private const val MONET_MODE = "MONET_MODE"

/**
 * Controller that holds current theming options and provides methods to manipulate them.
 *
 * @param lightColorScheme Scheme as default light color scheme.
 * @param darkColorScheme Scheme as default dark color scheme.
 * @param themingMode Current [ThemingMode].
 * @param dynamicThemeEnabled When true will use dynamic theming (Monet).
 * @param amoledThemeEnabled When true will change background color to black. Only for dark theme.
 * @param customColor Color from which color scheme will be generated from.
 * @param monetMode Monet mode for custom color scheme generation
 */
class ThemmoController(
    val lightColorScheme: ColorScheme,
    val darkColorScheme: ColorScheme,
    themingMode: ThemingMode,
    dynamicThemeEnabled: Boolean,
    amoledThemeEnabled: Boolean,
    customColor: Color,
    monetMode: MonetMode,
) {
    var currentThemingMode: ThemingMode by mutableStateOf(themingMode)
        private set

    var isDynamicThemeEnabled: Boolean by mutableStateOf(dynamicThemeEnabled)
        private set

    var isAmoledThemeEnabled: Boolean by mutableStateOf(amoledThemeEnabled)
        private set

    var currentCustomColor: Color by mutableStateOf(customColor)
        private set

    var currentMonetMode: MonetMode by mutableStateOf(monetMode)
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

    fun setCustomColor(color: Color) {
        currentCustomColor = color
    }

    fun setMonetMode(monetMode: MonetMode) {
        currentMonetMode = monetMode
    }

    @Composable
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

    @Composable
    private fun provideLightColorScheme(context: Context): ColorScheme {
        return when {
            isDynamicThemeEnabled -> {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                        dynamicLightColorScheme(context)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                        val keyColor = colorResource(android.R.color.system_accent1_500)

                        dynamicColorScheme(
                            keyColor = keyColor,
                            isDark = false,
                            style = currentMonetMode,
                        )
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                        val keyColor = extractWallpaperPrimary(context)

                        if (keyColor == null) {
                            lightColorScheme
                        } else {
                            dynamicColorScheme(
                                keyColor = keyColor,
                                isDark = false,
                                style = currentMonetMode,
                            )
                        }
                    }
                    else -> lightColorScheme
                }
            }
            !isDynamicThemeEnabled and currentCustomColor.isSpecified -> {
                dynamicColorScheme(
                    keyColor = currentCustomColor,
                    isDark = false,
                    style = currentMonetMode
                )
            }
            else -> lightColorScheme
        }
    }

    @Composable
    private fun provideDarkColorScheme(context: Context): ColorScheme {
        val darkColorScheme: ColorScheme = when {
            isDynamicThemeEnabled -> {
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                        dynamicDarkColorScheme(context)
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                        val keyColor = colorResource(android.R.color.system_accent1_500)

                        dynamicColorScheme(
                            keyColor = keyColor,
                            isDark = true,
                            style = currentMonetMode,
                        )
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                        val keyColor = extractWallpaperPrimary(context)

                        if (keyColor == null) {
                            darkColorScheme
                        } else {
                            dynamicColorScheme(
                                keyColor = keyColor,
                                isDark = true,
                                style = currentMonetMode,
                            )
                        }
                    }
                    else -> darkColorScheme
                }
            }
            !isDynamicThemeEnabled and currentCustomColor.isSpecified -> {
                dynamicColorScheme(
                    keyColor = currentCustomColor,
                    isDark = true,
                    style = currentMonetMode,
                )
            }
            else -> darkColorScheme
        }

        // Turning into amoled if needed
        return if (isAmoledThemeEnabled) {
            darkColorScheme.copy(background = Color.Black, surface = Color.Black)
        } else {
            darkColorScheme
        }
    }

    fun saveState(): Map<String, Any> {
        return mapOf(
            AMOLED_ENABLED to isAmoledThemeEnabled,
            THEMING_MODE to currentThemingMode,
            DYNAMIC_ENABLED to isDynamicThemeEnabled,
            // We can't save unsigned longs
            CUSTOM_COLOR to currentCustomColor.value.toLong(),
            MONET_MODE to currentMonetMode
        )
    }

}

/**
 * Restores [ThemmoController] for saver.
 *
 * @param lightColorScheme Light color scheme.
 * @param darkColorScheme Dark color scheme.
 * @param map Map of other options from [ThemmoController] that were saved.
 * @return Restored [ThemmoController].
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
        customColor = Color(value = (map[CUSTOM_COLOR] as Long).toULong()),
        monetMode = map[MONET_MODE] as MonetMode
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
    customColor: Color =  Color.Unspecified,
    monetMode: MonetMode = MonetMode.TonalSpot
): ThemmoController {
    return rememberSaveable(
        saver = themmoControllerSaver(lightColorScheme, darkColorScheme)
    ) {
        ThemmoController(
            lightColorScheme = lightColorScheme,
            darkColorScheme = darkColorScheme,
            themingMode = themingMode,
            dynamicThemeEnabled = dynamicThemeEnabled,
            amoledThemeEnabled = amoledThemeEnabled,
            customColor = customColor,
            monetMode = monetMode
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
