package com.feryaeljustice.mirailink.ui.screens.home

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.constants.TIME_24_HOURS
import com.feryaeljustice.mirailink.domain.error.LocationError
import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.model.swipe.UndoQuota
import com.feryaeljustice.mirailink.domain.usecase.feed.GetFeedUseCase
import com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.DislikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.GetUndoQuotaUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.LikeUserUseCase
import com.feryaeljustice.mirailink.domain.usecase.swipe.UndoSwipeUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val getUndoQuotaUseCase: GetUndoQuotaUseCase,
    private val undoSwipeUseCase: UndoSwipeUseCase,
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

    sealed interface HomeEvent {
        data object NavigateToPaywall : HomeEvent
    }

    val state: StateFlow<HomeUiState>
        field = MutableStateFlow<HomeUiState>(HomeUiState.Idle)

    private val _events = MutableSharedFlow<HomeEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<HomeEvent> = _events.asSharedFlow()

    private val _currentUser = MutableStateFlow<UserViewEntry?>(null)
    val currentUser: StateFlow<UserViewEntry?> = _currentUser.asStateFlow()

    private val _undoQuota = MutableStateFlow<UndoQuota?>(null)
    val undoQuota: StateFlow<UndoQuota?> = _undoQuota.asStateFlow()

    private val _userQueue = mutableListOf<UserViewEntry>()
    private val swipeHistory = mutableListOf<UserViewEntry>()
    private var feedLoadJob: Job? = null

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
        loadUndoQuota()
        loadUsers()
    }

    fun loadUndoQuota() {
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getUndoQuotaUseCase() }) {
                is MiraiLinkResult.Success -> {
                    _undoQuota.value = result.data
                }
                is MiraiLinkResult.Error -> {
                    // Carga de cuota silenciosa; no bloquea la interfaz de tarjetas
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val result =
                withContext(ioDispatcher) {
                    getCurrentUserUseCase()
                }

            if (result is MiraiLinkResult.Success) {
                _currentUser.value = result.data.toUserViewEntry()
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
                    _undoQuota.value?.let { currentQuota ->
                        if (!currentQuota.hasUndoableSwipe) {
                            _undoQuota.value = currentQuota.copy(
                                hasUndoableSwipe = true,
                                canUndo = currentQuota.remainingUndos > 0,
                            )
                        }
                    }
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    if (result.error == SubscriptionError.DAILY_LIKES_LIMIT_REACHED) {
                        _events.tryEmit(HomeEvent.NavigateToPaywall)
                    }
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
                    _undoQuota.value?.let { currentQuota ->
                        if (!currentQuota.hasUndoableSwipe) {
                            _undoQuota.value = currentQuota.copy(
                                hasUndoableSwipe = true,
                                canUndo = currentQuota.remainingUndos > 0,
                            )
                        }
                    }
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

    /**
     * Determina si el boton de deshacer debe mostrarse como activo.
     * Retorna true si hay un swipe reciente en la sesion actual o si el servidor
     * reporta swipes reversibles de sesiones anteriores (`hasUndoableSwipe`).
     * Esto permite al usuario pulsar el boton incluso si la cuota es 0, lo que
     * desencadenara la navegacion al Paywall para presentarle los planes Plus y Premium.
     */
    fun canUndo(): Boolean {
        return swipeHistory.isNotEmpty() || _undoQuota.value?.hasUndoableSwipe == true
    }

    /**
     * Ejecuta el rebobinado del ultimo swipe realizado.
     *
     * 1. Si no hay swipes reversibles, se descarta la accion.
     * 2. Si la cuota restante es 0, emite [HomeEvent.NavigateToPaywall] para invitar al usuario
     *    a mejorar su suscripcion y aborta la ejecucion remota.
     * 3. Invoca [undoSwipeUseCase] pasando el id del usuario de la sesion (o null para sesiones anteriores).
     * 4. En caso de exito, inserta el perfil recuperado al inicio de la cola de tarjetas
     *    ([_userQueue]) y actualiza el estado de la cuota.
     * 5. En caso de error [SubscriptionError.DAILY_UNDO_LIMIT_REACHED], emite la navegacion al Paywall.
     *
     * @return true si se inicio el proceso de deshacer, false si se derivo al Paywall o no habia swipes.
     */
    fun undoSwipe(): Boolean {
        if (!canUndo()) return false

        val currentQuota = _undoQuota.value
        if (currentQuota != null && currentQuota.remainingUndos <= 0) {
            _events.tryEmit(HomeEvent.NavigateToPaywall)
            return false
        }

        val targetUserId = swipeHistory.firstOrNull()?.id
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { undoSwipeUseCase(targetUserId) }) {
                is MiraiLinkResult.Success -> {
                    val restoredEntry = result.data.user.toUserViewEntry()
                    if (swipeHistory.isNotEmpty()) {
                        swipeHistory.removeAt(0)
                    }
                    _userQueue.add(0, restoredEntry)
                    _undoQuota.value = result.data.quota
                    updateUiState()
                }
                is MiraiLinkResult.Error -> {
                    if (result.error == SubscriptionError.DAILY_UNDO_LIMIT_REACHED) {
                        _events.tryEmit(HomeEvent.NavigateToPaywall)
                    }
                    setRecoveryAction(::undoSwipe)
                    state.value = HomeUiState.Error(result.error.toUiError())
                }
            }
        }
        return true
    }
}
