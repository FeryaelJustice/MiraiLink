package com.feryaeljustice.mirailink.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, sessionViewModel: GlobalSessionViewModel, userId: String) {
    LaunchedEffect(Unit) {
        sessionViewModel.showTopBarSettingsIcon()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Chat with User $userId")
    }
}