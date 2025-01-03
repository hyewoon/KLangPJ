// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

}


buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        val nav_version = "2.8.4"
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        classpath ("com.google.gms:google-services:4.4.0")
    }
}