package com.feryaeljustice.mirailink.ui.screens.chat

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.MessageItem
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, sessionViewModel: GlobalSessionViewModel, userId: String) {
    val messages by viewModel.messages.collectAsState()
    val sender by viewModel.sender.collectAsState()
    val receiver by viewModel.receiver.collectAsState()
    val input = remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        sessionViewModel.showTopBarSettingsIcon()
        viewModel.setReceiver(userId)
    }

    LaunchedEffect(messages.size) {
        scrollState.animateScrollToItem(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp),
            reverseLayout = true,
            state = scrollState,
        ) {
            items(messages.reversed()) { msg ->
                MessageItem(message = msg, isOwnMessage = msg.sender.id == sender?.id)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TextField(
                value = input.value,
                onValueChange = { input.value = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("${sender?.username?.replaceFirstChar { firstChar -> firstChar.uppercase() }}, escribe un mensaje...") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (input.value.isNotBlank()) {
                    viewModel.sendMessage(input.value)
                    input.value = ""
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Enviar"
                )
            }
        }
    }
}