

import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
    }

}

plugins {

    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}
dependencies {
    //implementation(libs.retrofit)
    //implementation(libs.annotation)
    //implementation(libs.tikxml.core)
    //implementation(libs.tikxml.retrofit.converter)
    implementation(libs.kotlinx.coroutines.core)
   // kapt(libs.tikxml.processor)
}
