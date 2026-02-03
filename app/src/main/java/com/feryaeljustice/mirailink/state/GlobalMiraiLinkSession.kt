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

    val currentUserId: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    val hasProfilePicture: StateFlow<Boolean?>
        field = MutableStateFlow<Boolean?>(null)

    private var observeHasProfilePictureJob: Job? = null

    // UI TopBarConfig
    val topBarConfig: StateFlow<TopBarConfig>
        field = MutableStateFlow<TopBarConfig>(TopBarConfig())

    fun clearSession() = appScope.launch { sessionManager.clearSession() }

    fun saveSession(
        token: String,
        userId: String,
    ) = appScope.launch { sessionManager.saveSession(token, userId) }

    fun saveIsVerified(verified: Boolean) = appScope.launch { sessionManager.saveIsVerified(isVerified = verified) }

    init {
        appScope.launch {
            sessionManager.userIdFlow.distinctUntilChanged().collectLatest { curUserId ->
                currentUserId.value = curUserId

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
        topBarConfig.update {
            it.copy(
                disableTopBar = true,
                disableBottomBar = true,
            )
        }
    }

    fun enableBars() {
        topBarConfig.update {
            it.copy(
                disableTopBar = false,
                disableBottomBar = false,
            )
        }
    }

    fun hideBars() {
        topBarConfig.update {
            it.copy(
                showTopBar = false,
                showBottomBar = false,
            )
        }
    }

    fun showBars() {
        topBarConfig.update {
            it.copy(
                showTopBar = true,
                showBottomBar = true,
            )
        }
    }

    fun showHideTopBar(show: Boolean) {
        topBarConfig.update { it.copy(showTopBar = show) }
    }

    fun showHideBottomBar(show: Boolean) {
        topBarConfig.update { it.copy(showBottomBar = show) }
    }

    fun enableDisableTopBar(enable: Boolean) {
        topBarConfig.update { it.copy(disableTopBar = enable) }
    }

    fun enableDisableBottomBar(enable: Boolean) {
        topBarConfig.update { it.copy(disableBottomBar = enable) }
    }

    fun hideTopBarSettingsIcon() {
        topBarConfig.update { it.copy(showSettingsIcon = false) }
    }

    fun showTopBarSettingsIcon() {
        topBarConfig.update { it.copy(showSettingsIcon = true) }
    }

    /*    private suspend fun saveUserId(userId: String?) {
            userId?.let {
                sessionManager.saveUserId(it)
                currentUserId.value = it
            }
        }*/

    fun startObservingHasProfilePicture(userId: String) {
        if (observeHasProfilePictureJob?.isActive == true) return
        observeHasProfilePictureJob =
            appScope.launch {
                var backoff = 10_000L
                while (isActive) {
                    val old = hasProfilePicture.value
                    when (val r = checkProfilePictureUseCase(userId)) {
                        is MiraiLinkResult.Success -> {
                            val new = r.data
                            if (new != old) hasProfilePicture.value = new
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
                hasProfilePicture.value = result.data
            }

            is MiraiLinkResult.Error -> {}
        }
    }
}
