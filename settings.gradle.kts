pluginManagement {
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositories {
    google()
    mavenCentral()
    maven(url = "https://jitpack.io")
  }
}

rootProject.name = "Themmo"

with(this) {
  include(":themmo")
  include(":themmosample")
  include(":themmo-core")
}
