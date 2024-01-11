plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sadellie"
            artifactId = "themmo"
            version = "1.2.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

android {
    namespace = "io.github.sadellie.themmo"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34

        aarMetadata {
            minCompileSdk = 29
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true

        // These are unused features
        aidl = false
        renderScript = false
        shaders = false
        buildConfig = false
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get().toString()
    }
    packaging {
        jniLibs.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(libs.androidx.core.core.ktx)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.test.ext.junit.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.com.github.kyant0.m3color)
    implementation(libs.androidx.compose.material3)
}