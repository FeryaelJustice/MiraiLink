package com.feryaeljustice.mirailink.ui.screens.ai.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import org.koin.androidx.compose.koinViewModel

@Suppress("EffectKeys", "ParamsComparedByRef", "ktlint:standard:function-naming")
@Composable
fun AiChatScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: AiChatViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var prompt by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var response by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MiraiLinkIconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            MiraiLinkText(
                text = stringResource(id = R.string.ai_chat_screen_title),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text(text = stringResource(id = R.string.chat_screen_send_msg)) },
                placeholder = { Text(text = stringResource(id = R.string.chat_screen_send_msg)) },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions =
                    KeyboardActions(
                        onSend = {
                            if (uiState !is AiChatUiState.Loading && prompt.text.isNotBlank()) {
                                viewModel.sendMessage(prompt = prompt.text)
                            }
                        },
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))
            EmojiPickerButton(
                textFieldValue = prompt,
                onTextFieldValueChange = { prompt = it },
            )
        }

        Button(
            onClick = { viewModel.sendMessage(prompt = prompt.text) },
            enabled = uiState !is AiChatUiState.Loading && prompt.text.isNotBlank(),
        ) {
            Text(stringResource(id = R.string.send))
        }

        Spacer(modifier = Modifier.height(8.dp))

        when (val state = uiState) {
            is AiChatUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AiChatUiState.Success -> {
                response = state.response // Actualiza la respuesta solo en éxito
            }

            is AiChatUiState.Error -> {
                MiraiLinkErrorContent(
                    error = state.error,
                    onAction = viewModel::performErrorAction,
                )
            }

            else -> {}
        }

        response?.let {
            Card(
                modifier =
                    Modifier
                        .fillMaxWidth(),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(all = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = stringResource(id = R.string.ai_chat_screen_title),
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
