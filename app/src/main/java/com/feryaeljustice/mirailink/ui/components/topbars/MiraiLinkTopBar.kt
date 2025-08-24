package com.feryaeljustice.mirailink.ui.components.topbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.ThemeSwitcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiraiLinkTopBar(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    enabled: Boolean = true,
    isAuthenticated: Boolean,
    showSettingsIcon: Boolean = true,
    title: String? = null,
    onThemeChange: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    TopAppBar(title = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (isAuthenticated && enabled) Modifier.clickable(
                        onClickLabel = stringResource(
                            R.string.navigate_home
                        )
                    ) { if (enabled) onNavigateHome() } else Modifier),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logomirailink),
                contentDescription = stringResource(R.string.app_name),
            )
            MiraiLinkText(
                text = title ?: stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }, actions = {
        if (enabled) {
            ThemeSwitcher(darkTheme = darkTheme, onClick = onThemeChange)
            if (showSettingsIcon) {
                MiraiLinkIconButton(onClick = onNavigateToSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        }
    })
}

data class TopBarConfig(
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true,
    val showSettingsIcon: Boolean = true,
    val disableTopBar: Boolean = false,
    val disableBottomBar: Boolean = false,
    val enableAppLogoClick: Boolean = true,
    val title: String? = null
)