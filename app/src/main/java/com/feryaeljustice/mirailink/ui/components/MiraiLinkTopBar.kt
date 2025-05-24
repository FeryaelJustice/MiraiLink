package com.feryaeljustice.mirailink.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.feryaeljustice.mirailink.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiraiLinkTopBar(
    darkTheme: Boolean = false,
    isAuthenticated: Boolean,
    onThemeChange: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    showSettingsIcon: Boolean = true,
    title: String? = null
) {
    TopAppBar(title = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClickLabel = "Navigate Home") { onNavigateHome() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logomirailink),
                contentDescription = "Mirai Link",
            )
            Text(text = title ?: "Mirai Link")
        }
    }, actions = {
        if (isAuthenticated) {
            ThemeSwitcher(darkTheme = darkTheme, onClick = onThemeChange)
            if (showSettingsIcon) {
                IconButton(onClick = onNavigateToSettings) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        }
    })
}

data class TopBarConfig(
    val showTopBar: Boolean = true,
    val showSettingsIcon: Boolean = true,
    val title: String? = null
)