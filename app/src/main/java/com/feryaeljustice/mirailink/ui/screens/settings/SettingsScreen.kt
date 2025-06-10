package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onLogout: () -> Unit
) {
    val logoutState = rememberUpdatedState(onLogout)

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.hideTopBarSettingsIcon()
        viewModel.logoutSuccess.collect { success ->
            if (success) logoutState.value()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        MiraiLinkButton(onClick = { viewModel.logout() }, content = {
            MiraiLinkText("Cerrar sesión")
        })
    }
}
