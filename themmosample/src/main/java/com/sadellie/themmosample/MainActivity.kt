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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sadellie.themmo.ThemingMode
import io.github.sadellie.themmo.Themmo
import io.github.sadellie.themmo.ThemmoController

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
fun ExampleSettingScreen(themmoController: ThemmoController) {
    Column {

        Text("Current theme: ${themmoController.currentThemingMode}")

        ThemingMode.values().forEach {
            Button(onClick = {themmoController.setThemingMode(it)}) { Text(it.name) }
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
