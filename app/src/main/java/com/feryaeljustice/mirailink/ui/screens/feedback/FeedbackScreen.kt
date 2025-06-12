package com.feryaeljustice.mirailink.ui.screens.feedback

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel, showToast: (String, Int) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            MiraiLinkIconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = {
                    onBackClick()
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
                verticalArrangement = Arrangement.Center
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.feedback),
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
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally),
                    onValueChange = { viewModel.updateFeedback(it) },
                    label = stringResource(R.string.feedback_screen_enter_your_feedback),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (uiState.feedback.isNotBlank()) {
                                viewModel.sendFeedback(onFinish = {
                                    showToast(
                                        context.getString(R.string.feedback),
                                        Toast.LENGTH_SHORT
                                    )
                                })
                            }
                        }
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                MiraiLinkButton(onClick = {
                    if (uiState.feedback.isNotBlank()) {
                        viewModel.sendFeedback(onFinish = {
                            showToast(context.getString(R.string.feedback_done), Toast.LENGTH_SHORT)
                        })
                    }
                }) {
                    MiraiLinkText(
                        text = stringResource(R.string.feedback_screen_send_feedback),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}