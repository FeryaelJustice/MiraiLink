package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.MatchesRow
import com.feryaeljustice.mirailink.ui.components.MessageListItem
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onNavigateToChat: (String) -> Unit
) {
    val matches by viewModel.matches.collectAsState()
    val openChats by viewModel.openChats.collectAsState()
    val searchQuery by remember { mutableStateOf("") }

    val filteredMatches = remember(searchQuery, matches) {
        matches.filter {
            it.username.contains(searchQuery, ignoreCase = true)
        }
    }

    val filteredOpenChats = remember(searchQuery, openChats) {
        openChats.filter {
            it.username.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        val newTopBarConfig = sessionViewModel.topBarConfig.value.copy(showSettingsIcon = true)
        sessionViewModel.updateTopBar(newTopBarConfig)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MatchesRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        horizontal = 2.dp,
                        vertical = 8.dp
                    )
                ), matches = filteredMatches
        ) {
            onNavigateToChat(it.id)
        }
        HorizontalDivider(modifier = Modifier.fillMaxWidth())
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    PaddingValues(
                        horizontal = 2.dp,
                        vertical = 8.dp
                    )
                )
        ) {
            items(filteredOpenChats) { chat ->
                MessageListItem(chat = chat, onClick = {
                    onNavigateToChat(it)
                })
            }
        }
    }
}