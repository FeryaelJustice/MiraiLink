package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.chat.ChatList
import com.feryaeljustice.mirailink.ui.components.match.MatchesRow
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel,
    miraiLinkSession: GlobalMiraiLinkSession,
    onNavigateToChat: (String) -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsState()
    val searchQuery by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
    }

    PullToRefreshBox(
        isRefreshing = state is MessagesViewModel.MessagesUiState.Loading,
        onRefresh = {
            viewModel.loadData()
        },
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
                val error = state as MessagesViewModel.MessagesUiState.Error
                MiraiLinkText(
                    text = error.message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            MessagesViewModel.MessagesUiState.Loading ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }

            MessagesViewModel.MessagesUiState.Idle -> Unit
        }
    }
}
