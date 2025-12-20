package com.feryaeljustice.mirailink.ui.components.topbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkImage
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.ThemeSwitcher

enum class TopBarLayoutDirection {
    ROW,
    COLUMN,
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiraiLinkTopBar(
    isAuthenticated: Boolean,
    onThemeChange: () -> Unit,
    onNavigateHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
    darkTheme: Boolean = false,
    enabled: Boolean = true,
    showSettingsIcon: Boolean = true,
    title: String? = null,
    layoutDirection: TopBarLayoutDirection = TopBarLayoutDirection.ROW,
) {
    TopAppBar(modifier = modifier, title = {
        val titleModifier =
            Modifier
                .fillMaxWidth()
                .then(
                    if (isAuthenticated && enabled) {
                        Modifier.clickable(
                            onClickLabel =
                                stringResource(
                                    R.string.navigate_home,
                                ),
                        ) { if (enabled) onNavigateHome() }
                    } else {
                        Modifier
                    },
                )

        val commonText: @Composable () -> Unit = {
            MiraiLinkText(
                text = title ?: stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium,
            )
        }

        when (layoutDirection) {
            TopBarLayoutDirection.ROW -> {
                Row(
                    modifier = titleModifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    MiraiLinkImage(
                        modifier = Modifier.size(40.dp),
                        painterId = R.drawable.logomirailink,
                        contentDescription = stringResource(R.string.app_name),
                        contentScale = ContentScale.Fit,
                    )
                    commonText()
                }
            }

            TopBarLayoutDirection.COLUMN -> {
                Column(
                    modifier = titleModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    MiraiLinkImage(
                        modifier = Modifier.fillMaxWidth(),
                        painterId = R.drawable.logomirailink,
                        contentDescription = stringResource(R.string.app_name),
                        contentScale = ContentScale.Fit,
                    )
                    commonText()
                }
            }
        }
    }, actions = {
        if (enabled) {
            ThemeSwitcher(darkTheme = darkTheme, onClick = onThemeChange)
            if (showSettingsIcon) {
                MiraiLinkIconButton(onClick = onNavigateToSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings),
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
    val title: String? = null,
    val layoutDirection: TopBarLayoutDirection = TopBarLayoutDirection.ROW, // Added for consistency
)
