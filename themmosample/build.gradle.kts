import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.compose)
  alias(libs.plugins.multiplatform)
  alias(libs.plugins.android.application)
}

kotlin {
  androidTarget {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
  }
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    outputModuleName.set("composeApp")
    browser {
      val rootDirPath = project.rootDir.path
      val projectDirPath = project.projectDir.path
      commonWebpackConfig {
        outputFileName = "composeApp.js"
        devServer =
          (devServer ?: KotlinWebpackConfig.DevServer()).apply {
            static =
              (static ?: mutableListOf()).apply {
                // Serve sources to debug inside browser
                add(rootDirPath)
                add(projectDirPath)
              }
          }
      }
      testTask { useKarma { useFirefoxHeadless() } }
    }
    binaries.executable()
  }
  sourceSets {
    commonMain.dependencies {
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(compose.ui)
      implementation(libs.org.jetbrains.compose.material3.material3)
      implementation(project(":themmo"))
    }
    androidMain.dependencies {
      implementation(compose.uiTooling)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.appcompat.appcompat)
      implementation(libs.androidx.core.ktx)
    }
    wasmJsMain.dependencies {}
  }

  compilerOptions {
    optIn.addAll(
      "kotlin.time.ExperimentalTime",
      "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
    )
  }
  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.get().compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
    }
  }
}

android {
  namespace = "io.github.sadellie.themmosample"
  compileSdk = 36

  defaultConfig {
    applicationId = "io.github.sadellie.themmosample"
    minSdk = 31
    targetSdk = 36
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    debug {
      isDebuggable = true
      isMinifyEnabled = false
      isShrinkResources = false
      applicationIdSuffix = ""
    }
    release {
      initWith(getByName("debug"))
      isDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      applicationIdSuffix = ""
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    // Explicit because I do not trust Android devs and so should you
    compose = true
    aidl = false
    renderScript = false
    shaders = false
    buildConfig = true
    resValues = false
  }
  packaging.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
}
