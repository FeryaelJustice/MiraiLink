package com.feryaeljustice.mirailink.ui.utils

import androidx.window.core.layout.WindowSizeClass

enum class DeviceConfiguration {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceConfiguration {
            val minWidth = windowSizeClass.minWidthDp
            val minHeight = windowSizeClass.minHeightDp

            return when {
                // 📱 Móvil en vertical
                minWidth < 600 && minHeight >= 600 -> MOBILE_PORTRAIT

                // 📱 Móvil en horizontal
                minWidth >= 600 && minHeight < 480 -> MOBILE_LANDSCAPE

                // 🔲 Tablet en vertical
                minWidth in 600..<840 && minHeight >= 900 -> TABLET_PORTRAIT

                // 🔳 Tablet en horizontal
                minWidth >= 840 && minHeight in 480..<900 -> TABLET_LANDSCAPE

                // 🖥 Escritorio o grandes pantallas
                else -> DESKTOP
            }
        }
    }
}

fun DeviceConfiguration.requiresDisplayCutoutPadding(): Boolean {
    return this == DeviceConfiguration.MOBILE_LANDSCAPE ||
            this == DeviceConfiguration.TABLET_LANDSCAPE
}