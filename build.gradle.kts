// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // 1. Plugins Base (Plataforma)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // 2. Plugins de Features (Kotlin/UI)
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.kotlinx.parcelize) apply false

    // 3. Plugins de Generación de Código
    alias(libs.plugins.ksp) apply false
}
