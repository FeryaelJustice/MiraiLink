package com.feryaeljustice.mirailink.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.feryaeljustice.mirailink.ui.navigation.NavWrapper
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemIsInDarkMode = isSystemInDarkTheme()
            var darkTheme by rememberSaveable { mutableStateOf(systemIsInDarkMode) }
            EnableTransparentStatusBar(darkMode = darkTheme)
            MiraiLinkTheme(darkTheme = darkTheme) {
                NavWrapper(darkTheme = darkTheme, onThemeChange = {
                    Log.d("Theme", "Theme changed: $darkTheme")
                    darkTheme = !darkTheme
                })
            }
        }
    }
}

@Composable
private fun EnableTransparentStatusBar(darkMode: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        val colorTransparent = Color.Transparent.toArgb()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
            window.decorView.setOnApplyWindowInsetsListener { view, insets ->
//                val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
                view.setBackgroundColor(colorTransparent)

                // Adjust padding to avoid overlap
//                view.setPadding(0, statusBarInsets.top, 0, 0)
                insets
            }
        } else {
            // For Android 14 and below
            window.statusBarColor = colorTransparent
        }
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
    }
}