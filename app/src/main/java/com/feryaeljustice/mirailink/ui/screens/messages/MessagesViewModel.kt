package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases
import com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.ui.viewentities.ChatPreviewViewEntity
import com.feryaeljustice.mirailink.ui.viewentities.MatchUserViewEntity
import com.feryaeljustice.mirailink.ui.viewentities.toMatchUserViewEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val chatUseCases: ChatUseCases,
) :
    ViewModel() {

    sealed class MessagesUiState {
        object Idle : MessagesUiState()
        object Loading : MessagesUiState()
        data class Success(
            val matches: List<MatchUserViewEntity>,
            val openChats: List<ChatPreviewViewEntity>
        ) : MessagesUiState()

        data class Error(val message: String, val exception: Throwable? = null) : MessagesUiState()
    }

    private val _state = MutableStateFlow<MessagesUiState>(MessagesUiState.Idle)
    val state = _state.asStateFlow()

    private var _matches: MutableList<MatchUserViewEntity> =
        mutableListOf(
            MatchUserViewEntity(
                "1",
                "Fer",
                "Ferr",
                TEMPORAL_PLACEHOLDER_PICTURE_URL,
                false
            ),
            MatchUserViewEntity(
                "2",
                "Maria",
                "Mariaa",
                "https://loremflickr.com/320/240/dog",
                true
            )
        )

    private var _openChats: MutableList<ChatPreviewViewEntity> =
        mutableListOf(
            ChatPreviewViewEntity(
                "1",
                "Fer",
                "Ferr",
                TEMPORAL_PLACEHOLDER_PICTURE_URL,
                "Hola, ¿cómo estás?",
                false
            ),
            ChatPreviewViewEntity(
                "2",
                "Maria",
                "Mariaa",
                "https://loremflickr.com/320/240/dog",
                "¿Qué me dijiste?",
                true
            )
        )

    init {
        loadData()
    }

    fun loadData() {
        loadMatches()
        loadChats()
    }

    fun loadMatches() {
        _state.value = MessagesUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getMatchesUseCase()) {
                is MiraiLinkResult.Success -> {
                    val matchesResult = result.data.map { user ->
                        val url = user.photos.firstOrNull()?.url
                        val us = user.toMatchUserViewEntity()
                        us.copy(avatarUrl = url.getFormattedUrl())
                    }
                    _matches = matchesResult.toMutableList()
                    _state.value =
                        MessagesUiState.Success(matches = matchesResult, openChats = _openChats)
                }

                is MiraiLinkResult.Error -> {
                    _state.value = MessagesUiState.Error(result.message, result.exception)
                }
            }
        }
    }

    fun loadChats() {
        _state.value = MessagesUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = chatUseCases.getChatsFromUser()) {
                is MiraiLinkResult.Success -> {
                    val chatsResult = result.data.map { chat ->
                        val avatar = chat.destinatary.avatarUrl
                        ChatPreviewViewEntity(
                            userId = chat.destinatary.id ?: Date().toString(),
                            username = chat.destinatary.username ?: "Unknown",
                            nickname = chat.destinatary.nickname ?: "Unknown",
                            avatarUrl = avatar.getFormattedUrl(),
                            lastMessage = chat.lastMessageText,
                            isBoosted = false,
                            readsPending = chat.unreadCount
                        )
                    }

                    _openChats = chatsResult.toMutableList()
                    _state.value =
                        MessagesUiState.Success(matches = _matches, openChats = chatsResult)
                }

                is MiraiLinkResult.Error -> {
                    _state.value = MessagesUiState.Error(result.message, result.exception)
                }
            }
        }
    }
}