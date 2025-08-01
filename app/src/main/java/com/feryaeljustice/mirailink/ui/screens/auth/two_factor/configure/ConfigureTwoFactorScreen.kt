package com.feryaeljustice.mirailink.ui.screens.auth.two_factor.configure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@Composable
fun ConfigureTwoFactorScreen(onBackClick: () -> Unit) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val actualBackClick by rememberUpdatedState(onBackClick)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                } else {
                    Modifier
                }
            )
    ) {
        MiraiLinkIconButton(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = {
                actualBackClick()
            },
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}