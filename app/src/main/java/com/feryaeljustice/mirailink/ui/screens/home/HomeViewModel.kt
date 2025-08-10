package com.feryaeljustice.mirailink.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.mappers.toUserViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeedUseCase: Lazy<GetFeedUseCase>,
    private val likeUser: Lazy<LikeUserUseCase>,
    private val dislikeUser: Lazy<DislikeUserUseCase>,
    private val getCurrentUserUseCase: Lazy<GetCurrentUserUseCase>
) : ViewModel() {

    sealed class HomeUiState {
        object Idle : HomeUiState()
        object Loading : HomeUiState()
        data class Success(val visibleUsers: List<UserViewEntry>, val currentIndex: Int = 0) :
            HomeUiState()

        data class Error(val message: String, val exception: Throwable? = null) : HomeUiState()
    }

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val state = _state.asStateFlow()

    var currentUser: UserViewEntry? = null
        private set

    private val _userQueue = mutableListOf<UserViewEntry>()
    private val swipeHistory = mutableListOf<UserViewEntry>()

    // TODO: Meter guardado en bdd local o en bdd remota para persistencia de calculo undo feature
    private var lastUndoTime: Long = 0L

    init {
        loadCurrentUser()
        loadUsers()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getCurrentUserUseCase.get()()
            }

            if (result is MiraiLinkResult.Success) {
                currentUser = result.data.toUserViewEntry()
            } else if (result is MiraiLinkResult.Error) {
                Log.e("HomeViewModel", "loadCurrentUser: ${result.message}")
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {
            _state.value = HomeUiState.Loading

            val result = withContext(Dispatchers.IO) {
                getFeedUseCase.get()()
            }

            if (result is MiraiLinkResult.Success) {
                _userQueue.clear()
                _userQueue.addAll(result.data.map { it.toUserViewEntry() })
                updateUiState()
            } else if (result is MiraiLinkResult.Error) {
                _state.value = HomeUiState.Error(result.message, result.exception)
            }
        }
    }

    private fun updateUiState() {
        _state.value = HomeUiState.Success(visibleUsers = _userQueue.take(2))
    }

    fun swipeRight() {
        val current = _userQueue.firstOrNull() ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                likeUser.get()(current.id)
            }
            saveToHistory(current)
            safeRemoveFirst()
            updateUiState()
        }
    }

    fun swipeLeft() {
        val current = _userQueue.firstOrNull() ?: return

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                dislikeUser.get()(current.id)
            }
            saveToHistory(current)
            safeRemoveFirst()
            updateUiState()
        }
    }

    private fun safeRemoveFirst() {
        if (_userQueue.isNotEmpty()) {
            _userQueue.removeAt(0)
        }
    }

    private fun saveToHistory(user: UserViewEntry) {
        swipeHistory.add(0, user)
    }

    fun canUndo(): Boolean {
        val now = System.currentTimeMillis()
        return swipeHistory.isNotEmpty() && now - lastUndoTime >= TIME_24_HOURS
    }

    fun undoSwipe(): Boolean {
        if (!canUndo()) return false

        val userToRestore = swipeHistory.firstOrNull() ?: return false
        swipeHistory.removeAt(0)
        _userQueue.add(0, userToRestore)
        lastUndoTime = System.currentTimeMillis()
        updateUiState()
        return true
    }
}