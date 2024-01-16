package com.sadellie.themmosample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import io.github.sadellie.themmo.core.MonetMode
import io.github.sadellie.themmo.Themmo
import io.github.sadellie.themmo.ThemmoController
import io.github.sadellie.themmo.core.ThemingMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Themmo {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExampleSettingScreen(it)
                }
            }
        }
    }
}

@Composable
fun allColors(): Map<String, Color> = mapOf(
     "primary" to MaterialTheme.colorScheme.primary,
     "onPrimary" to MaterialTheme.colorScheme.onPrimary,
     "primaryContainer" to MaterialTheme.colorScheme.primaryContainer,
     "onPrimaryContainer" to MaterialTheme.colorScheme.onPrimaryContainer,
     "inversePrimary" to MaterialTheme.colorScheme.inversePrimary,
     "secondary" to MaterialTheme.colorScheme.secondary,
     "onSecondary" to MaterialTheme.colorScheme.onSecondary,
     "secondaryContainer" to MaterialTheme.colorScheme.secondaryContainer,
     "onSecondaryContainer" to MaterialTheme.colorScheme.onSecondaryContainer,
     "tertiary" to MaterialTheme.colorScheme.tertiary,
     "onTertiary" to MaterialTheme.colorScheme.onTertiary,
     "tertiaryContainer" to MaterialTheme.colorScheme.tertiaryContainer,
     "onTertiaryContainer" to MaterialTheme.colorScheme.onTertiaryContainer,
     "background" to MaterialTheme.colorScheme.background,
     "onBackground" to MaterialTheme.colorScheme.onBackground,
     "surface" to MaterialTheme.colorScheme.surface,
     "onSurface" to MaterialTheme.colorScheme.onSurface,
     "surfaceVariant" to MaterialTheme.colorScheme.surfaceVariant,
     "onSurfaceVariant" to MaterialTheme.colorScheme.onSurfaceVariant,
     "surfaceTint" to MaterialTheme.colorScheme.surfaceTint,
     "inverseSurface" to MaterialTheme.colorScheme.inverseSurface,
     "inverseOnSurface" to MaterialTheme.colorScheme.inverseOnSurface,
     "error" to MaterialTheme.colorScheme.error,
     "onError" to MaterialTheme.colorScheme.onError,
     "errorContainer" to MaterialTheme.colorScheme.errorContainer,
     "onErrorContainer" to MaterialTheme.colorScheme.onErrorContainer,
     "outline" to MaterialTheme.colorScheme.outline,
     "outlineVariant" to MaterialTheme.colorScheme.outlineVariant,
     "scrim" to MaterialTheme.colorScheme.scrim,
     "surfaceBright" to MaterialTheme.colorScheme.surfaceBright,
     "surfaceDim" to MaterialTheme.colorScheme.surfaceDim,
     "surfaceContainer" to MaterialTheme.colorScheme.surfaceContainer,
     "surfaceContainerHigh" to MaterialTheme.colorScheme.surfaceContainerHigh,
     "surfaceContainerHighest" to MaterialTheme.colorScheme.surfaceContainerHighest,
     "surfaceContainerLow" to MaterialTheme.colorScheme.surfaceContainerLow,
     "surfaceContainerLowest" to MaterialTheme.colorScheme.surfaceContainerLowest,
)

@Composable
fun ExampleSettingScreen(themmoController: ThemmoController) {
    var colorHex by rememberSaveable { mutableStateOf("") }

    Column(Modifier.verticalScroll(rememberScrollState())) {

        Text("Current theme: ${themmoController.currentThemingMode}")

        ThemingMode.entries.forEach {
            Button(onClick = { themmoController.setThemingMode(it) }) { Text(it.name) }
        }

        // This option is only for supported API levels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            SettingRow("Dynamic theming") {
                Switch(
                    checked = themmoController.isDynamicThemeEnabled,
                    onCheckedChange = {
                        themmoController.enableDynamicTheme(it)
                    },
                )
            }
        }

        SettingRow("AMOLED") {
            Switch(
                checked = themmoController.isAmoledThemeEnabled,
                onCheckedChange = { themmoController.enableAmoledTheme(it) },
            )
        }

        Text("Custom color. Enter HEX.")
        OutlinedTextField(
            value = colorHex,
            onValueChange = {
                colorHex = it
                try {
                    val color = if (it.isEmpty()) Color.Unspecified else Color(it.toColorInt())
                    themmoController.setCustomColor(color)
                } catch (e: Exception) {
                    // Don't do this type of catch in prod, lol
                }
            },
            placeholder = { Text("HEX value, like #A70000") }
        )

        Text("Current mode: ${themmoController.currentMonetMode}")
        MonetMode.entries.forEach {
            Button(onClick = { themmoController.setMonetMode(it) }) { Text(it.name) }
        }

        allColors().forEach {
            SomeBox(color = it.value, text = it.key)
        }
    }
}

@Composable
fun SomeBox(color: Color, text: String) {
    Row {
        Box(
            Modifier
                .size(16.dp)
                .background(color))
        Text(text, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
fun SettingRow(text: String, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text)
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewPalette1(
    @PreviewParameter(ThemmoPreviewProvider::class, limit = 2) themmoController: ThemmoController
) {
    Themmo(
        themmoController = themmoController
    ) {
        Box(Modifier.background(MaterialTheme.colorScheme.surfaceVariant).size(46.dp))
    }
}

class ThemmoPreviewProvider : PreviewParameterProvider<ThemmoController> {
    override val values = sequenceOf(
        ThemmoController(
            customColor = Color(0xFF186c31),
            themingMode = ThemingMode.FORCE_DARK,
            lightColorScheme = lightColorScheme(),
            darkColorScheme = darkColorScheme(),
            dynamicThemeEnabled = false,
            amoledThemeEnabled = false,
            monetMode = MonetMode.TonalSpot
        ),
        ThemmoController(
            customColor = Color(0xFF186c31),
            themingMode = ThemingMode.FORCE_DARK,
            lightColorScheme = lightColorScheme(),
            darkColorScheme = darkColorScheme(),
            dynamicThemeEnabled = false,
            amoledThemeEnabled = true,
            monetMode = MonetMode.TonalSpot
        ),
    )
}
