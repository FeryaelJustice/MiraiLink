package com.feryaeljustice.mirailink.ui.screens.chat

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessageParsed
import com.feryaeljustice.mirailink.domain.model.chat.gesture.GestureMessagePayload
import com.feryaeljustice.mirailink.domain.util.formatDateSeparator
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.domain.util.superCapitalize
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextField
import com.feryaeljustice.mirailink.ui.components.chat.DateSeparator
import com.feryaeljustice.mirailink.ui.components.chat.MessageItem
import com.feryaeljustice.mirailink.ui.components.chat.emoji.EmojiPickerButton
import com.feryaeljustice.mirailink.ui.components.chat.gesture.GestureChallengeModal
import com.feryaeljustice.mirailink.ui.components.chat.gesture.GestureInviteMessageCard
import com.feryaeljustice.mirailink.ui.components.chat.gesture.GestureResultMessageCard
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
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
    modifier: Modifier = Modifier,
    miraiLinkSession: GlobalMiraiLinkSession,
    userId: String,
    onBackClick: () -> Unit,
    onNavigateToProfileDetail: ((String) -> Unit)? = null,
    viewModel: ChatViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val capsule by viewModel.capsule.collectAsStateWithLifecycle()
    val pendingWork by viewModel.pendingWork.collectAsStateWithLifecycle()
    val messageBusy by viewModel.messageBusy.collectAsStateWithLifecycle()
    val capsuleBusy by viewModel.capsuleBusy.collectAsStateWithLifecycle()
    val confirmedCapsuleActionId by viewModel.confirmedCapsuleActionId.collectAsStateWithLifecycle()
    val isDemo by miraiLinkSession.isDemoMode.collectAsStateWithLifecycle()
    val view = androidx.compose.ui.platform.LocalView.current
    var unlockPulse by remember { mutableStateOf(0) }
    androidx.lifecycle.compose.LifecycleResumeEffect(userId) {
        viewModel.setScreenActive(true)
        viewModel.startMessagePolling(userId)
        onPauseOrDispose { viewModel.setScreenActive(false); viewModel.stopMessagePolling() }
    }
    LaunchedEffect(viewModel) {
        viewModel.unlockEffects.collect {
            unlockPulse++
            view.performHapticFeedback(android.view.HapticFeedbackConstants.CONFIRM)
        }
    }
    val affinityViewModel: com.feryaeljustice.mirailink.ui.screens.affinity.AffinityContactViewModel = koinViewModel()
    val affinityContact by affinityViewModel.contact.collectAsStateWithLifecycle()
    var showAffinityInfoDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        affinityViewModel.load(userId)
    }

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val sender by viewModel.sender.collectAsStateWithLifecycle()
    val receiver by viewModel.receiver.collectAsStateWithLifecycle()
    val input = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val error by viewModel.error.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()

    var showReportDialog by rememberSaveable { mutableStateOf(false) }
    var selectedReportReason by rememberSaveable { mutableStateOf("") }
    var showCapsuleModal by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    var showGestureModal by rememberSaveable { mutableStateOf(false) }
    var showGestureActionDialog by rememberSaveable { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            showGestureModal = true
        }
    }

    val launchGestureGame: () -> Unit = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            showGestureModal = true
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val chatItems by remember(messages) {
        derivedStateOf {
            messages
                .groupBy { message ->
                    Instant
                        .ofEpochMilli(message.timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                }
                .toSortedMap(compareByDescending { it }) // Sort dates newest to oldest
                .flatMap { (date, messagesOnDate) ->
                    messagesOnDate
                        .sortedByDescending { it.timestamp }
                        .map { MessageItemModel(it) } +
                            DateSeparatorItemModel(
                                date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                                    .toEpochMilli(),
                            )
                }
        }
    }

    val (fullscreenImageUrl, setFullscreenImageUrl) = remember { mutableStateOf<String?>(null) }

    if (fullscreenImageUrl != null) {
        FullscreenImagePreview(
            photoPresentation = receiver?.photoPresentation,
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
            modifier
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
            unlockPulse = unlockPulse,
            modifier = Modifier,
            photoPresentation = receiver?.photoPresentation,
            receiverName = receiver?.nicknameElseUsername(),
            receiverUrlPhoto = receiver?.profilePhoto?.url.getFormattedUrl(),
            capsule = capsule,
            ownUserId = sender?.id,
            onCapsuleClick = { showCapsuleModal = true },
            onAvatarClick = {
                receiver?.username?.let { username ->
                    onNavigateToProfileDetail?.invoke(username)
                }
            },
            onLongPressOnImage = { url ->
                setFullscreenImageUrl(url)
            },
            onReportClick = {
                showReportDialog = true
            },
            onBackClick = onBackClick,
            isAffinityOrigin = affinityContact.origin == "affinity",
            onAffinityClick = { showAffinityInfoDialog = true },
        )
        if (showAffinityInfoDialog) {
            AlertDialog(
                onDismissRequest = { showAffinityInfoDialog = false },
                title = { androidx.compose.material3.Text(stringResource(R.string.affinity_title)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        androidx.compose.material3.Text(stringResource(R.string.affinity_chat_origin))
                        if (affinityContact.matched) {
                            androidx.compose.material3.Text(
                                stringResource(R.string.affinity_now_matched),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = { showAffinityInfoDialog = false }) {
                        androidx.compose.material3.Text(stringResource(android.R.string.ok))
                    }
                },
            )
        }
        com.feryaeljustice.mirailink.ui.screens.affinity.AffinityChatBanner(userId, viewModel = affinityViewModel)
        error?.let { currentError ->
            MiraiLinkErrorContent(
                error = currentError,
                onAction = viewModel::performErrorAction,
            )
        }
        if (pendingWork && !messageBusy && !capsuleBusy) androidx.compose.material3.TextButton(onClick = viewModel::retryPending) { androidx.compose.material3.Text(stringResource(R.string.capsule_retry_pending)) }
        if (showCapsuleModal && capsule != null) {
            com.feryaeljustice.mirailink.ui.components.capsule.CrystalCapsuleModal(
                state = capsule!!,
                ownId = sender?.id,
                busy = capsuleBusy || pendingWork,
                isDemo = isDemo,
                onDismiss = { showCapsuleModal = false },
                onAction = viewModel::capsuleAction,
                confirmedActionId = confirmedCapsuleActionId,
            )
        }
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            reverseLayout = true,
            state = scrollState,
        ) {
            if (capsule?.status == "revealed" && messages.isEmpty()) {
                item(key = "capsule_completed_banner") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_heart),
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                                Text(
                                    text = stringResource(R.string.capsule_completed_chat_banner),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                )
                            }
                        }
                    }
                }
            }
            items(
                items = chatItems,
                key = { it.id },
            ) { item ->
                when (item) {
                    is MessageItemModel -> {
                        val parsed = remember(item.message.content) {
                            GestureMessagePayload.parse(item.message.content)
                        }
                        when (parsed) {
                            is GestureMessageParsed.Invite -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    contentAlignment = if (item.message.sender.id == sender?.id) {
                                        Alignment.CenterEnd
                                    } else {
                                        Alignment.CenterStart
                                    },
                                ) {
                                    GestureInviteMessageCard(
                                        onAcceptChallenge = launchGestureGame,
                                    )
                                }
                            }

                            is GestureMessageParsed.Result -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    contentAlignment = if (item.message.sender.id == sender?.id) {
                                        Alignment.CenterEnd
                                    } else {
                                        Alignment.CenterStart
                                    },
                                ) {
                                    GestureResultMessageCard(
                                        summary = parsed.summary,
                                        onPlayAgain = launchGestureGame,
                                    )
                                }
                            }

                            is GestureMessageParsed.Regular -> {
                                MessageItem(
                                    msgContent = item.message.content,
                                    msgTimestamp = item.message.timestamp,
                                    isOwnMessage = item.message.sender.id == sender?.id,
                                )
                            }
                        }
                    }

                    is DateSeparatorItemModel -> {
                        DateSeparator(
                            date = formatDateSeparator(item.timestamp),
                        )
                    }
                }
            }
        }
        if (capsule != null && capsule?.status != "revealed") {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_visibility_off),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                    Text(
                        text = stringResource(R.string.capsule_chat_blocked_notice),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f),
                    )
                    Button(
                        onClick = { showCapsuleModal = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.capsule_title),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        } else {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkIconButton(
                    onClick = { showGestureActionDialog = true },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_gamepad),
                        contentDescription = stringResource(R.string.gesture_roulette_btn_content_description),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

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
                                if (input.value.text.isNotBlank() && !messageBusy && !pendingWork) {
                                    viewModel.sendMessage(input.value.text)
                                    input.value = TextFieldValue("")
                                }
                            },
                        ),
                )
                Spacer(modifier = Modifier.width(4.dp))

                EmojiPickerButton(
                    textFieldValue = input.value,
                    onTextFieldValueChange = { input.value = it },
                )

                Spacer(modifier = Modifier.width(4.dp))

                MiraiLinkIconButton(onClick = {
                    if (input.value.text.isNotBlank() && !messageBusy && !pendingWork) {
                        viewModel.sendMessage(input.value.text)
                        input.value = TextFieldValue("")
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = stringResource(R.string.send),
                    )
                }
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

        if (showGestureActionDialog) {
            AlertDialog(
                onDismissRequest = { showGestureActionDialog = false },
                title = {
                    MiraiLinkText(
                        text = stringResource(R.string.gesture_roulette_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                },
                text = {
                    Column {
                        MiraiLinkText(
                            text = stringResource(R.string.gesture_roulette_dialog_desc),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        MiraiLinkButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showGestureActionDialog = false
                                launchGestureGame()
                            },
                        ) {
                            MiraiLinkText(text = stringResource(R.string.gesture_roulette_play_action))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        MiraiLinkButton(
                            modifier = Modifier.fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            onClick = {
                                showGestureActionDialog = false
                                viewModel.sendGestureChallengeInvite()
                            },
                        ) {
                            MiraiLinkText(text = stringResource(R.string.gesture_roulette_invite_action))
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    MiraiLinkTextButton(
                        onClick = { showGestureActionDialog = false },
                        text = stringResource(R.string.cancel),
                    )
                },
            )
        }

        if (showGestureModal) {
            GestureChallengeModal(
                onDismiss = { showGestureModal = false },
                onShareResult = { summary ->
                    viewModel.sendGestureChallengeResult(summary)
                    showGestureModal = false
                },
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun ChatScreenTopBarPreview() {
    ChatTopBar(
        receiverName = "Sakura",
        receiverUrlPhoto = null,
        onBackClick = {},
        onReportClick = {},
        onLongPressOnImage = {},
    )
}
