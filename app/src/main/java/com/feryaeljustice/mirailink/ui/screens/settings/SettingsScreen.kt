package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    sessionViewModel: GlobalSessionViewModel,
    goToFeedbackScreen: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = "Imagen del logo en settings",
            modifier = Modifier
                .size(240.dp)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.weight(0.25f))
        MiraiLinkButton(onClick = { goToFeedbackScreen() }, content = {
            MiraiLinkText(
                text = "Danos tu opinión",
                color = MaterialTheme.colorScheme.onPrimary
            )
        })
        Spacer(modifier = Modifier.height(16.dp))
        MiraiLinkButton(onClick = { viewModel.logout() }, content = {
            MiraiLinkText(text = "Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
        })
        Spacer(modifier = Modifier.weight(1.75f))
    }
}
