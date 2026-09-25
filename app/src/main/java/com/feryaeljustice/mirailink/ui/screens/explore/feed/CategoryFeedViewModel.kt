package com.feryaeljustice.mirailink.ui.screens.explore.feed

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.UpdateCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CategoryFeedViewModel(
    val categoryId: String,
    val categoryName: String,
    private val getCategoryFeedUseCase: GetCategoryFeedUseCase,
    private val getCategoryPreferencesUseCase: GetCategoryPreferencesUseCase,
    private val updateCategoryPreferencesUseCase: UpdateCategoryPreferencesUseCase,
    private val likeUserUseCase: LikeUserUseCase,
    private val dislikeUserUseCase: DislikeUserUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    sealed class CategoryFeedUiState {
        data object Idle : CategoryFeedUiState()
        data object Loading : CategoryFeedUiState()
        data class Success(
            val visibleUsers: List<UserViewEntry>,
            val currentIndex: Int = 0,
        ) : CategoryFeedUiState()
        data object Empty : CategoryFeedUiState()
        data class Error(val error: UiError) : CategoryFeedUiState()
    }

    private val _state = MutableStateFlow<CategoryFeedUiState>(CategoryFeedUiState.Idle)
    val state: StateFlow<CategoryFeedUiState> = _state.asStateFlow()

    private val _radiusKm = MutableStateFlow(50)
    val radiusKm: StateFlow<Int> = _radiusKm.asStateFlow()

    private val _isSavingPreferences = MutableStateFlow(false)
    val isSavingPreferences: StateFlow<Boolean> = _isSavingPreferences.asStateFlow()

    private val _showSettingsSheet = MutableStateFlow(false)
    val showSettingsSheet: StateFlow<Boolean> = _showSettingsSheet.asStateFlow()

    private val _userQueue = mutableListOf<UserViewEntry>()
    private val swipeHistory = mutableListOf<UserViewEntry>()
    internal var lastUndoTime: Long = 0L

    init {
        loadPreferences()
        loadFeed()
    }

    fun openSettingsSheet() {
        _showSettingsSheet.value = true
    }

    fun closeSettingsSheet() {
        _showSettingsSheet.value = false
    }

    fun loadPreferences() {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getCategoryPreferencesUseCase(categoryId) }) {
                is MiraiLinkResult.Success -> {
                    _radiusKm.value = result.data.radiusKm
                }
                is MiraiLinkResult.Error -> {
                    // Fallback to default radius
                }
            }
        }
    }

    fun loadFeed() {
        viewModelScope.launch {
            _state.value = CategoryFeedUiState.Loading
            when (val result = withContext(ioDispatcher) { getCategoryFeedUseCase(categoryId) }) {
                is MiraiLinkResult.Success -> {
                    _userQueue.clear()
                    _userQueue.addAll(result.data.map { it.toUserViewEntry() })
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::loadFeed)
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun updateRadius(newRadiusKm: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            _isSavingPreferences.value = true
            when (val result = withContext(ioDispatcher) {
                updateCategoryPreferencesUseCase(categoryId, newRadiusKm)
            }) {
                is MiraiLinkResult.Success -> {
                    _radiusKm.value = result.data.radiusKm
                    _isSavingPreferences.value = false
                    closeSettingsSheet()
                    onComplete()
                    loadFeed()
                }
                is MiraiLinkResult.Error -> {
                    _isSavingPreferences.value = false
                }
            }
        }
    }

    fun swipeRight() {
        val current = _userQueue.firstOrNull() ?: return
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { likeUserUseCase(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    safeRemoveFirst()
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::swipeRight)
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun swipeLeft() {
        val current = _userQueue.firstOrNull() ?: return
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { dislikeUserUseCase(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    safeRemoveFirst()
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::swipeLeft)
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
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

    private fun updateUiState() {
        if (_userQueue.isEmpty()) {
            _state.value = CategoryFeedUiState.Empty
        } else {
            _state.value = CategoryFeedUiState.Success(
                visibleUsers = _userQueue.take(2).toList(),
                currentIndex = 0,
            )
        }
    }
}
