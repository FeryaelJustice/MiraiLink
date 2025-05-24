package com.feryaeljustice.mirailink.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.model.User
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val likeUser: LikeUserUseCase,
    private val dislikeUser: DislikeUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    sealed class HomeUiState {
        object Idle : HomeUiState()
        object Loading : HomeUiState()
        data class Success(val visibleUsers: List<User>, val currentIndex: Int = 0) : HomeUiState()
        data class Error(val message: String, val exception: Throwable? = null) : HomeUiState()
    }

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Idle)
    val state = _state.asStateFlow()

    var currentUser: User? = null

    private val _userQueue = mutableListOf<User>()
    private val swipeHistory = mutableListOf<User>()
    private var lastUndoTime: Long = 0L
    // TODO: Meter guardado en bdd local o en bdd remota para persistencia de calculo undo feature

    init {
        loadCurrentUser()
        loadUsers()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    currentUser = result.data
                }

                is MiraiLinkResult.Error -> {
                    // Nothing
                    Log.e("HomeViewModel", "loadCurrentUser: ${result.message}")
                }
            }
        }
    }

    fun loadUsers() {
        _state.value = HomeUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getFeedUseCase()) {
                is MiraiLinkResult.Success -> {
                    _userQueue.clear()
                    _userQueue.addAll(result.data)
                    updateUiState()
                }

                is MiraiLinkResult.Error -> {
                    _state.value = HomeUiState.Error(result.message, result.exception)
                }
            }
        }
    }

    private fun updateUiState() {
        _state.value = HomeUiState.Success(_userQueue.take(2))
    }

    fun swipeRight() {
        val current = _userQueue.firstOrNull() ?: return
        viewModelScope.launch {
            likeUser(current.id)
            saveToHistory(current)
            safeRemoveFirst()
            updateUiState()
        }
    }

    fun swipeLeft() {
        val current = _userQueue.firstOrNull() ?: return
        viewModelScope.launch {
            dislikeUser(current.id)
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

    private fun saveToHistory(user: User) {
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