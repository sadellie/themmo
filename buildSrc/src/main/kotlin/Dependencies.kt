object Deps {

    val coreKtx by lazy { "androidx.core:core-ktx:${Versions.coreKtxVersion}" }

    object Gradle {
        val gradle by lazy { "com.android.tools.build:gradle:${Versions.gradleVersion}" }
        val kotlin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}" }
    }

    object Module {
        val compose by lazy { "androidx.compose.ui:ui:${Versions.composeVersion}" }
        val composeTooling by lazy { "androidx.compose.ui:ui-tooling:${Versions.composeVersion}" }
        val composeToolingPreview by lazy { "androidx.compose.ui:ui-tooling-preview:${Versions.composeVersion}" }
        val material3 by lazy { "androidx.compose.material3:material3:${Versions.material3Version}" }
    }

    object Test {
        val junit by lazy { "junit:junit:${Versions.junitVersion}" }
        val junitExt by lazy { "androidx.test.ext:junit:${Versions.junitExtVersion}" }
    }

}