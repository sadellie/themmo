import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.multiplatform)
}

kotlin {
  androidTarget {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    outputModuleName.set("composeApp")
    browser()
    binaries.executable()
  }

  sourceSets.commonMain.dependencies {}
}


android {
  namespace = "io.github.sadellie.themmo.core"
  defaultConfig.minSdk = 23
  defaultConfig.targetSdk = 36
  compileSdk = defaultConfig.targetSdk
}
