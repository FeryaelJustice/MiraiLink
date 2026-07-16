package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.components.chat.ChatList
import com.feryaeljustice.mirailink.ui.components.match.MatchesRow
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming", "ParamsComparedByRef", "EffectKeys")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    onNavigateToChat: (String) -> Unit,
    onNavigateToAiChat: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MessagesViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            val tooltipState = rememberTooltipState()
            TooltipBox(
                positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                    positioning = TooltipAnchorPosition.Above
                ),
                tooltip = {
                    PlainTooltip {
                        MiraiLinkText(text = stringResource(R.string.ai_chat_open))
                    }
                },
                state = tooltipState,
            ) {
                FloatingActionButton(onClick = { onNavigateToAiChat() }) {
                    Icon(
                        modifier = Modifier.width(18.dp),
                        painter = painterResource(id = R.drawable.robot_ai),
                        tint = null,
                        contentDescription = stringResource(R.string.ai_chat_open),
                    )
                }
            }
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state is MessagesViewModel.MessagesUiState.Loading,
            onRefresh = {
                viewModel.loadData()
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        start = innerPadding.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(layoutDirection = LayoutDirection.Ltr),
                    )
                    .consumeWindowInsets(paddingValues = innerPadding)
                    .then(
                        if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                            Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                        } else {
                            Modifier
                        },
                    ),
        ) {
            when (val currentState = state) {
                is MessagesViewModel.MessagesUiState.Success -> {
                    val matches = currentState.matches
                    val openChats = currentState.openChats

                    val filteredMatches =
                        remember(searchQuery, matches) {
                            matches.filter {
                                it.username.contains(searchQuery, ignoreCase = true)
                            }
                        }

                    val filteredOpenChats =
                        remember(searchQuery, openChats) {
                            openChats.filter {
                                it.username.contains(searchQuery, ignoreCase = true)
                            }
                        }

                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                    ) {
                        MatchesRow(
                            modifier = Modifier.fillMaxWidth(),
                            matches = filteredMatches,
                            onNavigateToChat = onNavigateToChat,
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth())
                        ChatList(
                            modifier = Modifier.fillMaxWidth(),
                            chats = filteredOpenChats,
                            onNavigateToChat = onNavigateToChat,
                        )
                    }
                }

                is MessagesViewModel.MessagesUiState.Error -> {
                    MiraiLinkErrorContent(
                        error = currentState.error,
                        onAction = viewModel::performErrorAction,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                MessagesViewModel.MessagesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                MessagesViewModel.MessagesUiState.Idle -> {}
            }
        }
    }
}
