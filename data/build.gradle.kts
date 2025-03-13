
import java.util.Properties

plugins {

    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.google.devtools.ksp)
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.google.services)
    }
}

android {
    namespace = "com.hye.sesac.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"

    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {
    implementation(libs.firebase.bom)
    implementation(libs.androidx.core.ktx)
    implementation(libs.firebase.firestore)
    implementation(project(":domain"))
    implementation(libs.androidx.room.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //mlkit
    implementation(libs.digital.ink.recognition)

    //roomDB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.compiler)
    implementation(libs.androidx.fragment.ktx)

    ksp(libs.androidx.room.compiler)

    //retrofit, tikxml
    implementation(libs.retrofit)
    implementation(libs.tikxml.retrofit.converter)
    implementation(libs.annotation)
    implementation(libs.tikxml.core)
    implementation(libs.kotlinx.coroutines.core)
    kapt(libs.tikxml.processor)

    //okhttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

}




