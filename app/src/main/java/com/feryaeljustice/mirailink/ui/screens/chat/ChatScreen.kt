package com.feryaeljustice.mirailink.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField
import com.feryaeljustice.mirailink.ui.components.chat.MessageItem
import com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton
import com.feryaeljustice.mirailink.ui.components.topbars.ChatTopBar
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    sessionViewModel: GlobalSessionViewModel,
    userId: String,
    onBackClick: () -> Unit
) {
//    val chatId by viewModel.chatId.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val sender by viewModel.sender.collectAsState()
    val receiver by viewModel.receiver.collectAsState()
    val input = remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    var showReportDialog by remember { mutableStateOf(false) }
    var selectedReportReason by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.showTopBarSettingsIcon()
    }

    LaunchedEffect(userId) {
        viewModel.resetChatState()
        viewModel.initChat(receiverId = userId, type = ChatViewModel.Companion.CHATTYPE.PRIVATE)
    }

    LaunchedEffect(messages.size) {
        scrollState.animateScrollToItem(0)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopMessagePolling()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ChatTopBar(
            user = receiver,
            modifier = Modifier,
            onReportClick = {
                showReportDialog = true
            },
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = true,
            state = scrollState,
        ) {
            items(messages.reversed(), key = { it.id }) { msg ->
                MessageItem(message = msg, isOwnMessage = msg.sender.id == sender?.id)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            MiraiLinkTextField(
                value = input.value,
                onValueChange = { input.value = it },
                modifier = Modifier.weight(1f),
                maxLines = 1,
                label = stringResource(R.string.chat_screen_send_msg),
                placeholder = {
                    val txt = sender?.nicknameElseUsername()?.superCapitalize()
                    txt?.let {
                        MiraiLinkText(
                            text =
                                stringResource(
                                    R.string.chat_screen_smthg_send_msg,
                                    it
                                ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (input.value.isNotBlank()) {
                            viewModel.sendMessage(input.value)
                            input.value = ""
                        }
                    }
                )
            )
            Spacer(modifier = Modifier.width(4.dp))

            EmojiPickerButton { emoji ->
                input.value += emoji // Añade el emoji al final del mensaje
            }

            Spacer(modifier = Modifier.width(4.dp))

            MiraiLinkIconButton(onClick = {
                if (input.value.isNotBlank()) {
                    viewModel.sendMessage(input.value)
                    input.value = ""
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = stringResource(R.string.send)
                )
            }
        }

        AnimatedVisibility(showReportDialog) {
            AlertDialog(
                onDismissRequest = { showReportDialog = false },
                confirmButton = {
                    MiraiLinkTextButton(
                        onClick = {
                            if (selectedReportReason.isNotBlank()) {
                                viewModel.reportUser(userId, selectedReportReason)
                                showReportDialog = false
                            }
                        }, text = stringResource(R.string.report),
                        onTransparentBackgroundContentColor = MaterialTheme.colorScheme.secondary
                    )
                },
                dismissButton = {
                    MiraiLinkTextButton(
                        onClick = { showReportDialog = false },
                        text = stringResource(R.string.cancel),
                        onTransparentBackgroundContentColor = MaterialTheme.colorScheme.error
                    )
                },
                title = {
                    MiraiLinkText(text = stringResource(R.string.specify_reason))
                },
                text = {
                    Column {
                        stringArrayResource(R.array.report_reasons).forEach { reason ->
                            MiraiLinkTextButton(
                                onClick = { selectedReportReason = reason },
                                text = reason,
                                onTransparentBackgroundContentColor = if (selectedReportReason == reason) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    }
}