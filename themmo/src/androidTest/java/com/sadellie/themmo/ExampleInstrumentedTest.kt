package com.sadellie.themmo

import android.content.Context
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private val themmoController = ThemmoController(
        lightColorScheme = lightColorScheme(),
        darkColorScheme = darkColorScheme(),
        themingMode = ThemingMode.AUTO,
        dynamicThemeEnabled = false,
        amoledThemeEnabled = false
    )
    private val appContext: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun autoLight() {
        // auto with light system and all false, should be light
        themmoController.setThemingMode(mode = ThemingMode.AUTO)
        val providedColorScheme = themmoController.provideColorScheme(context = appContext, isSystemDark = false)

        assertEquals(lightColorScheme().background, providedColorScheme.background)
    }

    @Test
    fun autoDark() {
        // auto with dark system and all false, should be dark
        themmoController.setThemingMode(mode = ThemingMode.AUTO)
        val providedColorScheme = themmoController.provideColorScheme(context = appContext, isSystemDark = true)

        assertEquals(darkColorScheme().background, providedColorScheme.background)
    }

    @Test
    fun light() {
        // force light and all false, should be light
        themmoController.setThemingMode(mode = ThemingMode.FORCE_LIGHT)
        val providedColorScheme = themmoController.provideColorScheme(context = appContext, isSystemDark = false)

        assertEquals(lightColorScheme().background, providedColorScheme.background)
    }

    @Test
    fun dark() {
        // force dark and all false, should be dark
        themmoController.setThemingMode(mode = ThemingMode.FORCE_DARK)
        val providedColorScheme = themmoController.provideColorScheme(context = appContext, isSystemDark = false)

        assertEquals(darkColorScheme().background, providedColorScheme.background)
    }

    @Test
    fun darkAmoled() {
        // force dark with amoled, should be dark with black background
        themmoController.setThemingMode(mode = ThemingMode.FORCE_DARK)
        themmoController.enableAmoledTheme(true)
        val providedColorScheme = themmoController.provideColorScheme(context = appContext, isSystemDark = false)

        assertEquals(Color.Black, providedColorScheme.background)
    }
}