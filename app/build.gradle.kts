import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    // alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlinx.parcelize)

    alias(libs.plugins.ksp)

    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.screenshot)

    alias(libs.plugins.kotzilla)

    alias(libs.plugins.stability.analyzer)
}

android {
    namespace = "com.feryaeljustice.mirailink"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.feryaeljustice.mirailink"

        minSdk = 26
        targetSdk = 36
        versionCode = 28
        versionName = "1.7.0"

        testInstrumentationRunner = "com.feryaeljustice.mirailink.MiraiLinkTestRunner"
    }

    // Cargar keystore.properties
    val keystoreProperties =
        Properties().apply {
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
            buildConfigField("String", "TEST_USER", keystoreProperties["TEST_USER"] as String)
            buildConfigField("String", "TEST_PASS", keystoreProperties["TEST_PASS"] as String)
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = true
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    kotlin {
        jvmToolchain { languageVersion.set(JavaLanguageVersion.of(11)) }

        compilerOptions {
            freeCompilerArgs.add("-Xexplicit-backing-fields")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE-notice.md}"
        }
    }

    lint {
        disable.add("ktlint:standard:function-naming")
    }

    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

@Suppress("ktlint:standard:no-consecutive-comments")
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

    // Material Core
    implementation(libs.androidx.material.icons.core)

    // Material 3
    implementation(libs.androidx.material3)

    // M3 Adaptive
    implementation(libs.androidx.material3.adaptive)

    // AppCompat
    implementation(libs.androidx.appcompat)

    // Google Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Testing - Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.truth)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.arch.core.testing)

    // Testing - Instrumentation Tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.koin.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.turbine)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.okhttp.mockwebserver)
    androidTestImplementation(libs.androidx.test.uiautomator)
    androidTestImplementation(libs.koin.test.junit4)

    // Screenshot testing
    screenshotTestImplementation(libs.screenshot.validation.api)
    screenshotTestImplementation(libs.androidx.ui.tooling)

    // Tooling
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // KotlinX Serialization
    implementation(libs.kotlinx.serialization.json)
    // Serialization Converter for Retrofit
    implementation(libs.retrofit.converter.kotlinx.serialization)

    // Datastore
    implementation(libs.androidx.datastore.preferences)
    // Proto DataStore
    implementation(libs.androidx.datastore)

    // Jetpack Security
    implementation(libs.androidx.security.crypto)

    // Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)

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
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.ai)
    implementation(libs.firebase.appcheck.debug)

    // Google Ads
    implementation(libs.play.services.ads)
    // UMP (consent)
    implementation(libs.google.ump)

    // Androidx Credentials
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.playservices.auth) // Compatibilidad Android 13 a abajo

    // Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core.coroutines)
    // implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.annotations)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.koin.android.test)
    ksp(libs.koin.ksp.compiler)

    // Kotzilla
    // implementation(libs.kotzilla.sdk)
    implementation(libs.kotzilla.sdk.compose)
    // implementation(libs.kotzilla.sdk.android)

    // Composables (.com) -> Unstyled
    implementation(libs.composables.compose.unstyled)
    implementation(libs.composables.compose.unstyled.theming)
    implementation(libs.composables.compose.unstyled.primitives)
    implementation(libs.composables.compose.unstyled.platformtheme)
}

kotzilla {
    composeInstrumentation = true
}
