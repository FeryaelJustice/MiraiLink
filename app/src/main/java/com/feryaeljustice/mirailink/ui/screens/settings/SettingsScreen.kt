package com.feryaeljustice.mirailink.ui.screens.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkDialog
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    sessionViewModel: GlobalSessionViewModel,
    goToFeedbackScreen: () -> Unit,
    onLogout: () -> Unit,
    showToast: (String, Int) -> Unit
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val currentOnLogout by rememberUpdatedState(onLogout)

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val privacyUrl = viewModel.baseUrl + "/privacypolicy"

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.hideTopBarSettingsIcon()
    }

    LaunchedEffect(Unit) {
        viewModel.logoutSuccess.collect { success ->
            if (success) currentOnLogout()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteSuccess.collect { success ->
            if (success) currentOnLogout()
        }
    }

    AnimatedVisibility(showDeleteDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.delete_account_confirm_title),
            message = stringResource(R.string.delete_account_confirm_text),
            onDismiss = { showDeleteDialog = false },
            onAccept = {
                showDeleteDialog = false
                viewModel.deleteAccount {
                    showToast(
                        context.getString(R.string.delete_account_done),
                        Toast.LENGTH_SHORT
                    )
                }
            },
            onCancel = { showDeleteDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    AnimatedVisibility(showLogoutDialog) {
        MiraiLinkDialog(
            title = stringResource(R.string.logout_account_confirm_title),
            onDismiss = { showLogoutDialog = false },
            onAccept = {
                showLogoutDialog = false
                viewModel.logout(onFinish = {
                    showToast(context.getString(R.string.logout_done), Toast.LENGTH_SHORT)
                })
            },
            onCancel = { showLogoutDialog = false },
            acceptText = stringResource(R.string.accept),
            cancelText = stringResource(R.string.cancel),
            containerColor = MaterialTheme.colorScheme.surface,
            textColor = MaterialTheme.colorScheme.onSurface,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = stringResource(R.string.content_description_settings_screen_img_logo),
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(0.25f))
        MiraiLinkButton(onClick = { goToFeedbackScreen() }, content = {
            MiraiLinkText(
                text = stringResource(R.string.settings_screen_txt_give_feedback),
                color = MaterialTheme.colorScheme.onPrimary
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkButton(onClick = { showLogoutDialog = true }, content = {
            MiraiLinkText(
                text = stringResource(R.string.logout),
                color = MaterialTheme.colorScheme.onPrimary
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkTextButton(
            onClick = { showDeleteDialog = true },
            text = stringResource(R.string.delete_account),
            isTransparentBackground = false,
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        )
        Spacer(modifier = Modifier.weight(1.75f))

        Row(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MiraiLinkTextButton(
                text = stringResource(R.string.privacy_policy),
                onClick = {
                    uriHandler.openUri(privacyUrl)
                },
                isTransparentBackground = true
            )
        }
    }
}
