package com.feryaeljustice.mirailink.ui.screens.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.constants.deepLinkPrivacyPolicyUrl
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@Composable
fun SettingsScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    goToFeedbackScreen: () -> Unit,
    goToConfigureTwoFactorScreen: () -> Unit,
    showToast: (String, Int) -> Unit,
    copyToClipBoard: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val actualGoToFeedbackScreen by rememberUpdatedState(goToFeedbackScreen)
    val actualGoToConfigureTwoFactorScreen by rememberUpdatedState(goToConfigureTwoFactorScreen)

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.hideTopBarSettingsIcon()
    }

    LaunchedEffect(Unit) {
        viewModel.logoutSuccess.collect { success ->
            if (success) miraiLinkSession.clearSession()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteSuccess.collect { success ->
            if (success) miraiLinkSession.clearSession()
        }
    }

    val delAccountDoneText = stringResource(R.string.delete_account_done)
    AnimatedVisibility(showDeleteDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.delete_account_confirm_title),
            message = stringResource(R.string.delete_account_confirm_text),
            onDismiss = { showDeleteDialog = false },
            onAccept = {
                showDeleteDialog = false
                viewModel.deleteAccount {
                    showToast(
                        delAccountDoneText,
                        Toast.LENGTH_SHORT,
                    )
                }
            },
            onCancel = { showDeleteDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    val logoutDoneText = stringResource(R.string.logout_done)
    AnimatedVisibility(showLogoutDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.logout_account_confirm_title),
            onDismiss = { showLogoutDialog = false },
            onAccept = {
                showLogoutDialog = false
                viewModel.logout(onFinish = {
                    showToast(logoutDoneText, Toast.LENGTH_SHORT)
                })
            },
            onCancel = { showLogoutDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary,
        )
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = stringResource(R.string.content_description_settings_screen_img_logo),
            modifier =
                Modifier
                    .size(240.dp)
                    .padding(8.dp),
        )
        Spacer(modifier = Modifier.weight(0.25f))
        MiraiLinkButton(onClick = { actualGoToFeedbackScreen() }, content = {
            MiraiLinkText(
                text = stringResource(R.string.settings_screen_txt_give_feedback),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkButton(onClick = { actualGoToConfigureTwoFactorScreen() }, content = {
            MiraiLinkText(
                text = stringResource(R.string.configure_two_factor),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkButton(onClick = { showLogoutDialog = true }, content = {
            MiraiLinkText(
                text = stringResource(R.string.logout),
                color = MaterialTheme.colorScheme.onPrimary,
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkTextButton(
            onClick = { showDeleteDialog = true },
            text = stringResource(R.string.delete_account),
            isTransparentBackground = false,
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        )
        Spacer(modifier = Modifier.weight(1.75f))

        Row(
            modifier =
                Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkTextButton(
                text = stringResource(R.string.privacy_policy),
                style = MaterialTheme.typography.bodyLarge,
                onClick = {
                    uriHandler.openUri(deepLinkPrivacyPolicyUrl)
                },
                onLongClick = {
                    copyToClipBoard(deepLinkPrivacyPolicyUrl)
                },
                isTransparentBackground = true,
            )
        }
        Row(
            modifier =
                Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkText(
                text = stringResource(R.string.version_app, BuildConfig.VERSION_NAME),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
