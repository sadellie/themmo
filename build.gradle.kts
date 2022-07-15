buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        with(Deps.Gradle) {
            classpath(gradle)
            classpath(kotlin)
        }
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.build.gradle.kts files
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}