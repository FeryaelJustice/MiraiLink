package com.feryaeljustice.mirailink.ui.screens.auth.verification

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun VerificationScreen(
    viewModel: VerificationViewModel,
    sessionViewModel: GlobalSessionViewModel,
    userId: String,
    onFinish: () -> Unit,
    onLogout: () -> Unit,
) {
    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.hideBars()
        sessionViewModel.disableBars()

        viewModel.checkUserIsVerified(onFinish)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .windowInsetsPadding(WindowInsets.displayCutout),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (uiState.step) {
            1 -> {
                MiraiLinkText(text = stringResource(R.string.request_email_verification))
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(onClick = { viewModel.requestCode(userId) }) {
                    MiraiLinkText(
                        text = stringResource(R.string.send_code),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                MiraiLinkButton(onClick = { onLogout() }) {
                    MiraiLinkText(
                        text = stringResource(R.string.logout),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            2 -> {
                MiraiLinkOutlinedTextField(
                    value = uiState.token,
                    onValueChange = {
                        viewModel.onTokenChanged(it)
                    },
                    label = stringResource(R.string.code),
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(onClick = {
                    viewModel.confirmCode(
                        userId,
                        onFinish = onFinish
                    )
                }) {
                    MiraiLinkText(
                        text = stringResource(R.string.verify),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        if (uiState.error != null) MiraiLinkText(uiState.error!!, color = Color.Red)
    }

    BackHandler(enabled = true) { Log.i("OnBack", "Clicked back on Verification Screen") }
}