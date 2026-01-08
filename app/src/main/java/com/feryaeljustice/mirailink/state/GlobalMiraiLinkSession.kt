package com.feryaeljustice.mirailink.state

import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.components.topbars.TopBarConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GlobalMiraiLinkSession(
    private val sessionManager: SessionManager,
    private val checkProfilePictureUseCase: CheckProfilePictureUseCase,
    private val appScope: CoroutineScope,
){
    val isAuthenticated: StateFlow<Boolean> =
        sessionManager.isAuthenticatedFlow
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = false,
            )

    fun currentAuth(): Boolean = isAuthenticated.value

    val isVerified: StateFlow<Boolean> =
        sessionManager.isVerifiedFlow
            .distinctUntilChanged()
            .stateIn(
                scope = appScope,
                started = SharingStarted.Eagerly,
                initialValue = false,
            )
    val onLogout: SharedFlow<Unit> = sessionManager.onLogout
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    private val _hasProfilePicture = MutableStateFlow<Boolean?>(null)
    val hasProfilePicture: StateFlow<Boolean?> = _hasProfilePicture.asStateFlow()
    private var observeHasProfilePictureJob: Job? = null

    // UI TopBarConfig
    private val _topBarConfig = MutableStateFlow(TopBarConfig())
    val topBarConfig: StateFlow<TopBarConfig> = _topBarConfig.asStateFlow()

    fun clearSession() = appScope.launch { sessionManager.clearSession() }

    fun saveSession(
        token: String,
        userId: String,
    ) = appScope.launch { sessionManager.saveSession(token, userId) }

    fun saveIsVerified(verified: Boolean) = appScope.launch { sessionManager.saveIsVerified(isVerified = verified) }

    init {
        appScope.launch {
            sessionManager.userIdFlow.distinctUntilChanged().collectLatest { curUserId ->
                _currentUserId.value = curUserId

                if (!curUserId.isNullOrBlank()) {
                    startObservingHasProfilePicture(curUserId)
                } else {
                    stopObservingHasProfilePicture()
                }

                /* if (!_isInitialized.value) {
                     _isInitialized.value = true
                 }*/
            }
        }
    }

    // Métodos para actualizar el estado de la UI
    fun disableBars() {
        _topBarConfig.update {
            it.copy(
                disableTopBar = true,
                disableBottomBar = true,
            )
        }
    }

    fun enableBars() {
        _topBarConfig.update {
            it.copy(
                disableTopBar = false,
                disableBottomBar = false,
            )
        }
    }

    fun hideBars() {
        _topBarConfig.update {
            it.copy(
                showTopBar = false,
                showBottomBar = false,
            )
        }
    }

    fun showBars() {
        _topBarConfig.update {
            it.copy(
                showTopBar = true,
                showBottomBar = true,
            )
        }
    }

    fun showHideTopBar(show: Boolean) {
        _topBarConfig.update { it.copy(showTopBar = show) }
    }

    fun showHideBottomBar(show: Boolean) {
        _topBarConfig.update { it.copy(showBottomBar = show) }
    }

    fun enableDisableTopBar(enable: Boolean) {
        _topBarConfig.update { it.copy(disableTopBar = enable) }
    }

    fun enableDisableBottomBar(enable: Boolean) {
        _topBarConfig.update { it.copy(disableBottomBar = enable) }
    }

    fun hideTopBarSettingsIcon() {
        _topBarConfig.update { it.copy(showSettingsIcon = false) }
    }

    fun showTopBarSettingsIcon() {
        _topBarConfig.update { it.copy(showSettingsIcon = true) }
    }

    /*    private suspend fun saveUserId(userId: String?) {
            userId?.let {
                sessionManager.saveUserId(it)
                _currentUserId.value = it
            }
        }*/

    fun startObservingHasProfilePicture(userId: String) {
        if (observeHasProfilePictureJob?.isActive == true) return
        observeHasProfilePictureJob =
            appScope.launch {
                var backoff = 10_000L
                while (isActive) {
                    val old = _hasProfilePicture.value
                    when (val r = checkProfilePictureUseCase(userId)) {
                        is MiraiLinkResult.Success -> {
                            val new = r.data
                            if (new != old) _hasProfilePicture.value = new
                            backoff = 10_000L // reset
                        }

                        is MiraiLinkResult.Error -> {
                            backoff = (backoff * 2).coerceAtMost(120_000L)
                        }
                    }
                    delay(backoff)
                }
            }
    }

    fun stopObservingHasProfilePicture() {
        observeHasProfilePictureJob?.cancel()
    }

    // Aparte del job
    suspend fun refreshHasProfilePicture(userId: String) {
        when (val result = checkProfilePictureUseCase(userId)) {
            is MiraiLinkResult.Success -> {
                _hasProfilePicture.value = result.data
            }

            is MiraiLinkResult.Error -> {}
        }
    }
}
