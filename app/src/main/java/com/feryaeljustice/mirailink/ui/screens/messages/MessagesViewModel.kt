package com.feryaeljustice.mirailink.ui.screens.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toChatPreviewViewEntry
import com.feryaeljustice.mirailink.data.mappers.ui.toMatchUserViewEntry
import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.usecase.chat.ChatUseCases
import com.feryaeljustice.mirailink.domain.usecase.match.GetMatchesUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.getFormattedUrl
import com.feryaeljustice.mirailink.ui.viewentries.chat.ChatPreviewViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.MatchUserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MessagesViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val chatUseCases: ChatUseCases,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    sealed class MessagesUiState {
        object Idle : MessagesUiState()

        object Loading : MessagesUiState()

        data class Success(
            val matches: List<MatchUserViewEntry>,
            val openChats: List<ChatPreviewViewEntry>,
        ) : MessagesUiState()

        data class Error(
            val message: String,
            val exception: Throwable? = null,
        ) : MessagesUiState()
    }

    val state: StateFlow<MessagesUiState>
        field = MutableStateFlow<MessagesUiState>(MessagesUiState.Idle)

    private var _matches: MutableList<MatchUserViewEntry> =
        mutableListOf(
            MatchUserViewEntry(
                "1",
                "Fer",
                "Ferr",
                TEMPORAL_PLACEHOLDER_PICTURE_URL,
                false,
            ),
            MatchUserViewEntry(
                "2",
                "Maria",
                "Mariaa",
                "https://loremflickr.com/320/240/dog",
                true,
            ),
        )

    private var _openChats: MutableList<ChatPreviewViewEntry> =
        mutableListOf(
            ChatPreviewViewEntry(
                "1",
                "Fer",
                "Ferr",
                TEMPORAL_PLACEHOLDER_PICTURE_URL,
                "Hola, ¿cómo estás?",
                false,
            ),
            ChatPreviewViewEntry(
                "2",
                "Maria",
                "Mariaa",
                "https://loremflickr.com/320/240/dog",
                "¿Qué me dijiste?",
                true,
            ),
        )

    init {
        loadData()
    }

    fun loadData() {
        loadMatches()
        loadChats()
    }

    fun loadMatches() {
        viewModelScope.launch {
            state.value = MessagesUiState.Loading

            val result =
                withContext(ioDispatcher) {
                    getMatchesUseCase()
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    val matchesResult =
                        result.data.map { user ->
                            val url = user.photos.firstOrNull()?.url
                            val us = user.toMatchUserViewEntry()
                            us.copy(avatarUrl = url.getFormattedUrl())
                        }

                    _matches = matchesResult.toMutableList()
                    state.value =
                        MessagesUiState.Success(matches = matchesResult, openChats = _openChats)
                }

                is MiraiLinkResult.Error -> {
                    state.value = MessagesUiState.Error(result.message, result.exception)
                }
            }
        }
    }

    fun loadChats() {
        viewModelScope.launch {
            state.value = MessagesUiState.Loading

            val result =
                withContext(ioDispatcher) {
                    chatUseCases.getChatsFromUser()
                }

            when (result) {
                is MiraiLinkResult.Success -> {
                    val chatsResult =
                        result.data.map { chat ->
                            chat.toChatPreviewViewEntry()
                        }
                    _openChats = chatsResult.toMutableList()
                    state.value =
                        MessagesUiState.Success(matches = _matches, openChats = chatsResult)
                }

                is MiraiLinkResult.Error -> {
                    state.value = MessagesUiState.Error(result.message, result.exception)
                }
            }
        }
    }
}
