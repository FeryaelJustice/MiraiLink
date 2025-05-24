package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.ui.viewentities.ChatPreviewViewEntity
import com.feryaeljustice.mirailink.ui.viewentities.MatchUserViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor() : ViewModel() {
    private val _matches: MutableStateFlow<List<MatchUserViewEntity>> =
        MutableStateFlow(
            listOf(
                MatchUserViewEntity(
                    "1",
                    "Fer",
                    "http://192.168.1.132:3000/assets/img/profiles/Goku.jpeg",
                    false
                ),
                MatchUserViewEntity(
                    "2",
                    "Maria",
                    "https://loremflickr.com/320/240/dog",
                    true
                )
            )
        )
    val matches = _matches.asStateFlow()

    private val _openChats: MutableStateFlow<List<ChatPreviewViewEntity>> =
        MutableStateFlow(
            listOf(
                ChatPreviewViewEntity(
                    "1",
                    "Fer",
                    "http://192.168.1.132:3000/assets/img/profiles/Goku.jpeg",
                    "Hola, ¿cómo estás?",
                    false
                ),
                ChatPreviewViewEntity(
                    "2",
                    "Maria",
                    "https://loremflickr.com/320/240/dog",
                    "¿Qué me dijiste?",
                    true
                )
            )
        )
    val openChats = _openChats.asStateFlow()
}