package com.feryaeljustice.mirailink.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.formatDateSeparator
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField
import com.feryaeljustice.mirailink.ui.components.chat.DateSeparator
import com.feryaeljustice.mirailink.ui.components.chat.MessageItem
import com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton
import com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview
import com.feryaeljustice.mirailink.ui.components.topbars.ChatTopBar
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatMessageViewEntry
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.ZoneId

private sealed interface ChatItemModel {
    val id: String
}

private data class MessageItemModel(
    val message: ChatMessageViewEntry,
) : ChatItemModel {
    override val id: String = message.id
}

private data class DateSeparatorItemModel(
    val timestamp: Long,
) : ChatItemModel {
    override val id: String = "separator-$timestamp"
}

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@Composable
fun ChatScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    userId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val sender by viewModel.sender.collectAsStateWithLifecycle()
    val receiver by viewModel.receiver.collectAsStateWithLifecycle()
    val input = rememberSaveable { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    var showReportDialog by rememberSaveable { mutableStateOf(false) }
    var selectedReportReason by rememberSaveable { mutableStateOf("") }

    val chatItems =
        remember(messages) {
            messages
                .groupBy { message ->
                    Instant
                        .ofEpochMilli(message.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }.toSortedMap(compareByDescending { it }) // Sort dates newest to oldest
                .flatMap { (date, messagesOnDate) ->
                    val dateSeparator =
                        DateSeparatorItemModel(
                            date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        )
                    val sortedMessages =
                        messagesOnDate
                            .sortedByDescending { it.timestamp }
                            .map { MessageItemModel(it) }

                    // In a reversed list, the separator needs to come *after* the items of the day
                    // to appear *above* them visually.
                    sortedMessages + dateSeparator
                }
        }

    val (fullscreenImageUrl, setFullscreenImageUrl) = remember { mutableStateOf<String?>(null) }

    if (fullscreenImageUrl != null) {
        FullscreenImagePreview(
            imageUrl = fullscreenImageUrl,
            onDismiss = { setFullscreenImageUrl(null) },
            closeContentDescription = stringResource(R.string.content_description_user_card_close_btn),
            imageContentDescription = stringResource(R.string.content_description_user_card_fullscreen_img),
        )
    }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
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
        modifier =
            Modifier
                .fillMaxSize()
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
    ) {
        ChatTopBar(
            modifier = Modifier,
            receiverName = receiver?.nicknameElseUsername(),
            receiverUrlPhoto = receiver?.profilePhoto?.url.getFormattedUrl(),
            onLongPressOnImage = { url ->
                setFullscreenImageUrl(url)
            },
            onReportClick = {
                showReportDialog = true
            },
            onBackClick = onBackClick,
        )
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = true,
            state = scrollState,
        ) {
            items(
                items = chatItems,
                key = { it.id },
            ) { item ->
                when (item) {
                    is MessageItemModel -> {
                        MessageItem(
                            msgContent = item.message.content,
                            msgTimestamp = item.message.timestamp,
                            isOwnMessage = item.message.sender.id == sender?.id,
                        )
                    }

                    is DateSeparatorItemModel -> {
                        DateSeparator(
                            date = formatDateSeparator(item.timestamp),
                        )
                    }
                }
            }
        }
        Row(
            modifier =
                Modifier
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
                                    it,
                                ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions =
                    KeyboardActions(
                        onSend = {
                            if (input.value.isNotBlank()) {
                                viewModel.sendMessage(input.value)
                                input.value = ""
                            }
                        },
                    ),
            )
            Spacer(modifier = Modifier.width(4.dp))

            EmojiPickerButton(onEmojiSelect = { emoji ->
                input.value += emoji // Añade el emoji al final del mensaje
            })

            Spacer(modifier = Modifier.width(4.dp))

            MiraiLinkIconButton(onClick = {
                if (input.value.isNotBlank()) {
                    viewModel.sendMessage(input.value)
                    input.value = ""
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = stringResource(R.string.send),
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
                        },
                        text = stringResource(R.string.report),
                        onTransparentBackgroundContentColor = MaterialTheme.colorScheme.secondary,
                    )
                },
                dismissButton = {
                    MiraiLinkTextButton(
                        onClick = { showReportDialog = false },
                        text = stringResource(R.string.cancel),
                        onTransparentBackgroundContentColor = MaterialTheme.colorScheme.error,
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
                                onTransparentBackgroundContentColor =
                                    if (selectedReportReason ==
                                        reason
                                    ) {
                                        MaterialTheme.colorScheme.tertiary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    },
                            )
                        }
                    }
                },
            )
        }
    }
}
