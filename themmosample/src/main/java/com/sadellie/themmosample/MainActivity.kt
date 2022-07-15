package com.sadellie.themmosample

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sadellie.themmo.ThemingMode
import com.sadellie.themmo.Themmo
import com.sadellie.themmo.ThemmoController
import com.sadellie.themmo.rememberThemmoController
import com.sadellie.themmosample.ui.theme.DarkColorScheme
import com.sadellie.themmosample.ui.theme.LightColorScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Themmo(
                themmoController = rememberThemmoController(
                    lightColorScheme = LightColorScheme,
                    darkColorScheme = DarkColorScheme
                ),
                typography = MaterialTheme.typography
            ) {
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
fun ExampleSettingScreen(themmoController: ThemmoController) {
    Column {
        var dropDownExpanded by rememberSaveable { mutableStateOf(false) }
        var currentOption by rememberSaveable { mutableStateOf(themmoController.currentThemingMode) }

        SettingRow("Mode") {
            ExposedDropdownMenuBox(
                expanded = dropDownExpanded, onExpandedChange = { dropDownExpanded = it }
            ) {
                OutlinedTextField(
                    value = currentOption.name,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = dropDownExpanded,
                    onDismissRequest = { dropDownExpanded = false }) {
                    ThemingMode.values().forEach {
                        DropdownMenuItem(
                            text = { Text(it.name) },
                            onClick = {
                                currentOption = it
                                themmoController.setThemingMode(it)
                                dropDownExpanded = false
                            }
                        )
                    }
                }
            }
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
