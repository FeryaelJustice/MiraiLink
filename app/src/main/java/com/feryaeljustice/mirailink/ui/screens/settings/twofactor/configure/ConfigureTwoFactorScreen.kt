package com.feryaeljustice.mirailink.ui.screens.settings.twofactor.configure

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkCard
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupDialog
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@Composable
fun ConfigureTwoFactorScreen(
    viewModel: ConfigureTwoFactorViewModel,
    onBackClick: () -> Unit,
    onShowError: (String) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val actualBackClick by rememberUpdatedState(onBackClick)
    val actualOnShowError by rememberUpdatedState(onShowError)

    val isTwoFactorEnabled by viewModel.isTwoFactorEnabled.collectAsState()
    val showSetupDialog by viewModel.showSetupDialog.collectAsState()
    val otpUrl by viewModel.otpUrl.collectAsState()
    val base32 by viewModel.base32.collectAsState()
    val recoveryCodes by viewModel.recoveryCodes.collectAsState()
    val code by viewModel.verify2FACode.collectAsState()
    val isConfigure2FADialogLoading by viewModel.isConfigure2FALoading.collectAsState()
    val errorMsg by viewModel.errorString.collectAsState()
    val showError by remember(errorMsg) {
        derivedStateOf { errorMsg?.isNotBlank() ?: false }
    }

    if (showSetupDialog) {
        TwoFactorSetupDialog(
            otpUrl = otpUrl,
            base32 = base32,
            recoveryCodes = recoveryCodes,
            code = code,
            isLoading = isConfigure2FADialogLoading,
            onCodeChange = viewModel::onCodeChanged,
            onDismiss = viewModel::dismissDialog,
            onConfirm = viewModel::confirmCode
        )
    }

    if (showError) {
        actualOnShowError(errorMsg ?: "")
    }

    Column(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            MiraiLinkCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClickLabel = stringResource(R.string.configure_two_factor)) {
                        viewModel.launchSetupDialog()
                    },
                containerColor = if (isTwoFactorEnabled) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.errorContainer
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiraiLinkText(
                        text = stringResource(R.string.is_two_factor_enabled),
                        color = if (isTwoFactorEnabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                    Icon(
                        painter = painterResource(id = if (isTwoFactorEnabled) R.drawable.ic_check_box else R.drawable.ic_indeterminate_check_box),
                        contentDescription = stringResource(R.string.is_two_factor_enabled),
                        tint = if (isTwoFactorEnabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}