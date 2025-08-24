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
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkBasicText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkCard
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorPutCodeOrRecoveryCDialog
import com.feryaeljustice.mirailink.ui.components.twofactor.TwoFactorSetupDialog
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@Composable
fun ConfigureTwoFactorScreen(
    viewModel: ConfigureTwoFactorViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onBackClick: () -> Unit,
    onShowError: (String) -> Unit
) {
    val userID = sessionViewModel.currentUserId.collectAsState()

    val secondaryColor = MaterialTheme.colorScheme.secondary
    val secondaryContainerColor = MaterialTheme.colorScheme.secondaryContainer
    val errorColor = MaterialTheme.colorScheme.error
    val errorContainerColor = MaterialTheme.colorScheme.errorContainer

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val actualBackClick by rememberUpdatedState(onBackClick)
    val actualOnShowError by rememberUpdatedState(onShowError)

    val isTwoFactorEnabled by viewModel.isTwoFactorEnabled.collectAsState()

    val showSetupDialog by viewModel.showSetupDialog.collectAsState()
    val isConfigure2FADialogLoading by viewModel.isConfigure2FALoading.collectAsState()
    val otpUrl by viewModel.otpUrl.collectAsState()
    val base32 by viewModel.base32.collectAsState()
    val recoveryCodes by viewModel.recoveryCodes.collectAsState()
    val setupTwoFactorCode by viewModel.verify2FACode.collectAsState()

    val showDisableTwoFactorDialog by viewModel.showDisableTwoFactorDialog.collectAsState()
    val isDisable2FADialogLoading by viewModel.isDisable2FALoading.collectAsState()
    val disableTwoFactorCode by viewModel.disable2FACode.collectAsState()

    val errorMsg by viewModel.errorString.collectAsState()
    val showError by remember(errorMsg) {
        derivedStateOf { errorMsg?.isNotBlank() ?: false }
    }

    val cardTextColor by remember(isTwoFactorEnabled) {
        derivedStateOf { if (isTwoFactorEnabled) secondaryColor else errorColor }
    }

    LaunchedEffect(Unit) {
        viewModel.onlyCheckTwoFacStatusWithIO(userID.value)
    }

    if (showSetupDialog) {
        TwoFactorSetupDialog(
            otpUrl = otpUrl,
            base32 = base32,
            recoveryCodes = recoveryCodes,
            code = setupTwoFactorCode,
            isLoading = isConfigure2FADialogLoading,
            onCodeChange = viewModel::onSetupTwoFactorCodeChanged,
            onDismiss = viewModel::dismissSetupTwoFactorDialog,
            onConfirm = {
                viewModel.confirmSetupTwoFactor(userID = userID.value)
            }
        )
    }

    if (showDisableTwoFactorDialog) {
        TwoFactorPutCodeOrRecoveryCDialog(
            code = disableTwoFactorCode,
            isLoading = isDisable2FADialogLoading,
            onCodeChange = viewModel::onDisableTwoFactorCodeChanged,
            onDismiss = viewModel::dismissDisableTwoFactorDialog,
            onConfirm = {
                viewModel.confirmDisableTwoFactor(userID = userID.value)
            }
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
                    .then(
                        if (isTwoFactorEnabled) {
                            Modifier.clickable(onClickLabel = stringResource(R.string.disable_two_factor)) {
                                viewModel.launchDisableTwoFactorDialog()
                            }
                        } else {
                            Modifier.clickable(onClickLabel = stringResource(R.string.configure_two_factor)) {
                                viewModel.launchSetupTwoFactorDialog()
                            }
                        }
                    ),
                containerColor = if (isTwoFactorEnabled) secondaryContainerColor else errorContainerColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MiraiLinkBasicText(
                        text = stringResource(R.string.is_two_factor_enabled),
                        textStyle = MaterialTheme.typography.bodySmall,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = MaterialTheme.typography.bodySmall.fontSize,
                            maxFontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            stepSize = 1.sp
                        ),
                        color = { cardTextColor },
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        painter = painterResource(id = if (isTwoFactorEnabled) R.drawable.ic_check_box else R.drawable.ic_indeterminate_check_box),
                        contentDescription = stringResource(R.string.is_two_factor_enabled),
                        tint = if (isTwoFactorEnabled) secondaryColor else errorColor
                    )
                }
            }
        }
    }
}