package com.feryaeljustice.mirailink.ui.screens.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun FeedbackScreen(viewModel: FeedbackViewModel, sessionViewModel: GlobalSessionViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MiraiLinkText(
                text = "Feedback",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            uiState.error?.let {
                MiraiLinkText(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            MiraiLinkTextField(
                value = uiState.feedback,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { viewModel.updateFeedback(it) },
                label = "Enter your feedback",
            )
            Spacer(modifier = Modifier.height(8.dp))
            MiraiLinkButton(onClick = { viewModel.sendFeedback() }) {
                MiraiLinkText(
                    text = "Enviar feedback",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}