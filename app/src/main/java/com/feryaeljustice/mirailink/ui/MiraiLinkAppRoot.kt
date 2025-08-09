package com.feryaeljustice.mirailink.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.feryaeljustice.mirailink.ui.navigation.NavWrapper
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme

@Composable
fun MiraiLinkAppRoot() {
    val systemIsInDarkMode = isSystemInDarkTheme()
    var darkTheme by rememberSaveable { mutableStateOf(systemIsInDarkMode) }
//    EnableTransparentStatusBar(darkMode = darkTheme)
    MiraiLinkTheme(darkTheme = darkTheme) {
        NavWrapper(darkTheme = darkTheme, onThemeChange = {
            darkTheme = !darkTheme
        })
    }
}

/*
@Composable
private fun EnableTransparentStatusBar(darkMode: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val colorTransparent = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                view.setBackgroundColor(colorTransparent)
                insets
            }
        } else {
            window.statusBarColor = colorTransparent
        }
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
    }
}*/
