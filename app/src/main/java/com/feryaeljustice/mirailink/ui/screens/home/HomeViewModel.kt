package com.feryaeljustice.mirailink.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.error.LocationError
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val likeUser: LikeUserUseCase,
    private val dislikeUser: DislikeUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase,
    private val sendLocationPingUseCase: SendLocationPingUseCase,
    private val ioDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {
    sealed class HomeUiState {
        object Idle : HomeUiState()

        object Loading : HomeUiState()

        data class Success(
            val visibleUsers: List<UserViewEntry>,
            val currentIndex: Int = 0,
        ) : HomeUiState()

        data class Error(val error: UiError) : HomeUiState()
    }

    val state: StateFlow<HomeUiState>
        field = MutableStateFlow<HomeUiState>(HomeUiState.Idle)

    var currentUser: UserViewEntry? = null
        private set

    private val _userQueue = mutableListOf<UserViewEntry>()
    private val swipeHistory = mutableListOf<UserViewEntry>()
    private var feedLoadJob: Job? = null

    // TODO: Meter guardado en bdd local o en bdd remota para persistencia de calculo undo feature
    internal var lastUndoTime: Long = 0L

    init {
        reload()
        observeSearchPreferences()
    }

    private fun observeSearchPreferences() {
        viewModelScope.launch {
            getSearchPreferencesUseCase()
                .distinctUntilChanged()
                .drop(1)
                .collect {
                    _userQueue.clear()
                    loadUsers()
                }
        }
    }

    fun reload() {
        loadCurrentUser()
        loadUsers()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    getCurrentUserUseCase()
                }

            if (result is MiraiLinkResult.Success) {
                currentUser = result.data.toUserViewEntry()
            } else if (result is MiraiLinkResult.Error) {
                setRecoveryAction(::reload)
                state.value = HomeUiState.Error(result.error.toUiError())
            }
        }
    }

    fun loadUsers() {
        feedLoadJob?.cancel()
        feedLoadJob = viewModelScope.launch {
            state.value = HomeUiState.Loading

            val result =
                withContext(ioDispatcher) {
                    getFeedUseCase()
                }

            if (result is MiraiLinkResult.Success) {
                _userQueue.clear()
                _userQueue.addAll(result.data.map { it.toUserViewEntry() })
                updateUiState()
            } else if (result is MiraiLinkResult.Error) {
                val preferences = getSearchPreferencesUseCase().first()
                if (result.error == LocationError.LOCATION_REQUIRED &&
                    preferences.scope == SearchScope.RADIUS_RESIDENCE
                ) {
                    // Una residencia válida sin perfiles cercanos es un estado vacío,
                    // no un error accionable de permisos o configuración.
                    _userQueue.clear()
                    updateUiState()
                } else {
                    setRecoveryAction(::loadUsers)
                    state.value = HomeUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun updateActiveLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch(ioDispatcher) {
            sendLocationPingUseCase(latitude, longitude)
        }
    }

    private fun updateUiState() {
        state.value = HomeUiState.Success(visibleUsers = _userQueue.take(2))
    }

    fun swipeRight() {
        val current = _userQueue.firstOrNull() ?: return

        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { likeUser(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    safeRemoveFirst()
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::swipeRight)
                    state.value = HomeUiState.Error(result.error.toUiError())
                }
            }
        }
    }

    fun swipeLeft() {
        val current = _userQueue.firstOrNull() ?: return

        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { dislikeUser(current.id) }) {
                is MiraiLinkResult.Success -> {
                    saveToHistory(current)
                    safeRemoveFirst()
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    setRecoveryAction(::swipeLeft)
                    state.value = HomeUiState.Error(result.error.toUiError())
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
}
