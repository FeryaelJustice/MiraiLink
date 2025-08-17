import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinx.parcelize)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.feryaeljustice.mirailink"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.feryaeljustice.mirailink"

        minSdk = 26
        targetSdk = 36
        versionCode = 13
        versionName = "1.1.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Cargar keystore.properties
    val keystoreProperties = Properties().apply {
        val file = rootProject.file("keystore.properties")
        if (file.exists()) {
            load(FileInputStream(file))
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
//        create("debug") {
//            storeFile = file(keystoreProperties["storeFileDebug"] as String)
//            storePassword = keystoreProperties["storePasswordDebug"] as String
//            keyAlias = keystoreProperties["keyAliasDebug"] as String
//            keyPassword = keystoreProperties["keyPasswordDebug"] as String
//        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
//            signingConfig = signingConfigs.getByName("debug")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.foundation.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Material 3
    implementation(libs.androidx.material3)

    // M3 Adaptive
    implementation(libs.androidx.material3.adaptive)

    // AppCompat
    implementation(libs.androidx.appcompat)

    // Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)
    // Serialization Converter for Retrofit
    implementation(libs.retrofit.converter.serialization)

    // Datastore
    implementation(libs.androidx.datastore.preferences)
    // Proto DataStore
    implementation(libs.androidx.datastore)

    // Jetpack Security
    implementation(libs.androidx.security.crypto)

    // Navigation & Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.compiler)

    // Coil (images)
    implementation(platform(libs.coil.bom))
    implementation(libs.coil.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Socket IO
    implementation(libs.io.socket.client)
    implementation(libs.io.socket.engine.client)

    // Emoji Picker
    implementation(libs.compose.emoji.picker)

    // QR Code
    implementation(libs.zxing.core)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Google Ads
    implementation(libs.play.services.ads)
    // UMP (consent)
    implementation(libs.google.ump)
}