// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false


}


buildscript {
    repositories {
        google()
        mavenCentral()

    }


    dependencies {
            classpath(libs.androidx.navigation.safe.args.gradle.plugin)
            classpath(libs.google.services)

        }
    }

subprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains:annotations:${libs.versions.jetbrains.annotations.get()}")

            eachDependency {
                if (requested.group == "org.jetbrains" && requested.name == "annotations") {
                    useVersion(libs.versions.jetbrains.annotations.get())
                }
                if (requested.group == "com.intellij" && requested.name == "annotations") {
                    useTarget("org.jetbrains:annotations:${libs.versions.jetbrains.annotations.get()}")
                }
            }
        }
    }
}
