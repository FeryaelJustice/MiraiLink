package com.feryaeljustice.mirailink.ui.screens.auth.recover

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun RecoverPasswordScreen(
    viewModel: RecoverPasswordViewModel,
    sessionViewModel: GlobalSessionViewModel
) {
    LaunchedEffect(Unit) {
        val newTopBarConfig = sessionViewModel.topBarConfig.value.copy(showSettingsIcon = true)
        sessionViewModel.updateTopBar(newTopBarConfig)
    }
}