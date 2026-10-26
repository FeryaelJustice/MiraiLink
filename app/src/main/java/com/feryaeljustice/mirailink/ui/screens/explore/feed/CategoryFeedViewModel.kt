package com.feryaeljustice.mirailink.ui.screens.explore.feed

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.GetCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.explore.UpdateCategoryPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

import com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker
import com.feryaeljustice.mirailink.domain.telemetry.CrashReporter
import com.feryaeljustice.mirailink.domain.telemetry.MiraiAnalyticsEvent

@KoinViewModel
class CategoryFeedViewModel(
    val categoryId: String,
    val categoryName: String,
    private val getCategoryFeedUseCase: GetCategoryFeedUseCase,
    private val getCategoryPreferencesUseCase: GetCategoryPreferencesUseCase,
    private val updateCategoryPreferencesUseCase: UpdateCategoryPreferencesUseCase,
    private val likeUserUseCase: LikeUserUseCase,
    private val dislikeUserUseCase: DislikeUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase? = null,
    private val ioDispatcher: CoroutineDispatcher,
    private val analytics: AnalyticsTracker? = null,
    private val crashReporter: CrashReporter? = null,
) : RetryableViewModel() {

    sealed interface CategoryFeedEvent {
        data object NavigateToPaywall : CategoryFeedEvent
    }

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

    private val _events = MutableSharedFlow<CategoryFeedEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<CategoryFeedEvent> = _events.asSharedFlow()

    private val _state = MutableStateFlow<CategoryFeedUiState>(CategoryFeedUiState.Idle)
    val state: StateFlow<CategoryFeedUiState> = _state.asStateFlow()

    private val _radiusKm = MutableStateFlow(50)
    val radiusKm: StateFlow<Int> = _radiusKm.asStateFlow()

    private val _targetGender = MutableStateFlow<TargetSearchGender?>(null)
    val targetGender: StateFlow<TargetSearchGender?> = _targetGender.asStateFlow()

    private val _userGender = MutableStateFlow<String?>(null)
    val userGender: StateFlow<String?> = _userGender.asStateFlow()

    private val _isSavingPreferences = MutableStateFlow(false)
    val isSavingPreferences: StateFlow<Boolean> = _isSavingPreferences.asStateFlow()

    private val _showSettingsSheet = MutableStateFlow(false)
    val showSettingsSheet: StateFlow<Boolean> = _showSettingsSheet.asStateFlow()

    private val _userQueue = mutableListOf<UserViewEntry>()
    private val swipeHistory = mutableListOf<UserViewEntry>()
    internal var lastUndoTime: Long = 0L

    init {
        analytics?.logEvent(
            MiraiAnalyticsEvent.ExploreEvent.CategorySelected(
                categoryId = categoryId,
                categoryName = categoryName,
            ),
        )
        loadPreferences()
        loadUserGender()
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
                    _targetGender.value = result.data.targetGender
                }
                is MiraiLinkResult.Error -> {
                    // Usar radio predeterminado si no se obtiene el configurado
                }
            }
        }
    }

    fun loadUserGender() {
        val useCase = getCurrentUserUseCase ?: return
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { useCase() }) {
                is MiraiLinkResult.Success -> {
                    _userGender.value = result.data.gender
                }
                is MiraiLinkResult.Error -> Unit
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
                    crashReporter?.recordException(
                        throwable = Exception("Load category feed error: ${result.error}"),
                        reason = "Category feed failed",
                        screen = "category_feed",
                        metadata = mapOf("category_id" to categoryId),
                    )
                    setRecoveryAction(::loadFeed)
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun updateSettings(
        newRadiusKm: Int,
        newTargetGender: TargetSearchGender? = _targetGender.value,
        onComplete: () -> Unit = {},
    ) {
        analytics?.logEvent(
            MiraiAnalyticsEvent.ExploreEvent.FilterApplied(
                distanceKm = newRadiusKm,
                gender = newTargetGender?.name,
            ),
        )
        viewModelScope.launch {
            _isSavingPreferences.value = true
            when (val result = withContext(ioDispatcher) {
                updateCategoryPreferencesUseCase(categoryId, newRadiusKm, newTargetGender)
            }) {
                is MiraiLinkResult.Success -> {
                    _radiusKm.value = result.data.radiusKm
                    _targetGender.value = result.data.targetGender
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

    fun updateRadius(newRadiusKm: Int, onComplete: () -> Unit = {}) {
        updateSettings(newRadiusKm = newRadiusKm, newTargetGender = _targetGender.value, onComplete = onComplete)
    }

    fun swipeRight() {
        val current = _userQueue.firstOrNull() ?: return
        swipeRight(current)
    }

    private fun swipeRight(current: UserViewEntry) {
        analytics?.logEvent(
            MiraiAnalyticsEvent.DiscoveryEvent.CardSwiped(
                action = "like",
                targetUserId = current.id,
            ),
        )
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { likeUserUseCase(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    removeTarget(current.id)
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    if (result.error == SubscriptionError.DAILY_LIKES_LIMIT_REACHED) {
                        analytics?.logEvent(
                            MiraiAnalyticsEvent.DiscoveryEvent.DailyLimitReached(limitType = "likes"),
                        )
                        _events.tryEmit(CategoryFeedEvent.NavigateToPaywall)
                    }
                    setRecoveryAction { swipeRight(current) }
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun swipeLeft() {
        val current = _userQueue.firstOrNull() ?: return
        swipeLeft(current)
    }

    private fun swipeLeft(current: UserViewEntry) {
        analytics?.logEvent(
            MiraiAnalyticsEvent.DiscoveryEvent.CardSwiped(
                action = "pass",
                targetUserId = current.id,
            ),
        )
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { dislikeUserUseCase(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    removeTarget(current.id)
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction { swipeLeft(current) }
                    _state.value = CategoryFeedUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    private fun removeTarget(id: String) {
        _userQueue.removeAll { it.id == id }
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
