package io.github.sadellie.themmosample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.sadellie.themmo.Themmo
import io.github.sadellie.themmo.core.MonetMode
import io.github.sadellie.themmo.core.ThemingMode
import io.github.sadellie.themmo.rememberThemmoController

@Composable
internal fun PreviewThemmo() {
  val themmoController = rememberThemmoController(dynamicThemeEnabled = true)

  Themmo(themmoController) {
    val allColors =
      mapOf(
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
    Scaffold { paddingValues ->
      var colorHex by rememberSaveable { mutableStateOf("") }

      Column(Modifier.padding(paddingValues).verticalScroll(rememberScrollState())) {
        Text("Current theme: ${themmoController.currentThemingMode}")

        ThemingMode.entries.forEach { mode ->
          Button(
            onClick = { themmoController.setThemingMode(mode) },
            shapes = ButtonDefaults.shapes(),
          ) {
            Text(mode.name)
          }
        }

        // This option is only for supported API levels
        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text("Dynamic theming")
          Box(modifier = Modifier.fillMaxWidth()) {
            Switch(
              checked = themmoController.isDynamicThemeEnabled,
              onCheckedChange = { themmoController.enableDynamicTheme(it) },
            )
          }
        }

        Row(
          modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text("AMOLED")
          Box(modifier = Modifier.fillMaxWidth()) {
            Switch(
              checked = themmoController.isAmoledThemeEnabled,
              onCheckedChange = { themmoController.enableAmoledTheme(it) },
            )
          }
        }

        Text("Custom color. Enter HEX.")
        OutlinedTextField(
          value = colorHex,
          onValueChange = {
            colorHex = it
            try {
              val color = if (it.isEmpty()) Color.Unspecified else hexToColor(it)
              themmoController.setCustomColor(color)
            } catch (_: Exception) {
              // Don't do this type of catch in prod, lol
            }
          },
          placeholder = { Text("HEX value, like #A70000") },
        )

        Text("Current mode: ${themmoController.currentMonetMode}")
        MonetMode.entries.forEach { mode ->
          Button(
            onClick = { themmoController.setMonetMode(mode) },
            shapes = ButtonDefaults.shapes(),
          ) {
            Text(mode.name)
          }
        }

        allColors.forEach {
          Row {
            Box(Modifier.size(16.dp).background(color = it.value))
            Text(text = it.key, color = MaterialTheme.colorScheme.onBackground)
          }
        }
      }
    }
  }
}

private fun hexToColor(hex: String): Color {
  var stringToParse = hex.removePrefix("#").uppercase()
  if (stringToParse.length == 6) {
    // hex without alpha 123456 -> FF123546
    stringToParse = "FF$stringToParse"
  }
  val alpha = stringToParse.take(2).toInt(16)
  val red = stringToParse.substring(2, 4).toInt(16)
  val green = stringToParse.substring(4, 6).toInt(16)
  val blue = stringToParse.substring(6, 8).toInt(16)
  return Color(red, green, blue, alpha)
}
