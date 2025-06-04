package com.feryaeljustice.mirailink.ui.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.local.SessionManager
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.components.TopBarConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

// ViewModel para manejar el logout y redirigir a la pantalla de inicio de sesión de la app
@HiltViewModel
class GlobalSessionViewModel @Inject constructor(
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
    private val checkProfilePictureUseCase: CheckProfilePictureUseCase,
) : ViewModel() {
    val isAuthenticated: Flow<Boolean> = sessionManager.isAuthenticated
    val isVerified: Flow<Boolean> = sessionManager.isVerifiedFlow
    val onLogout: SharedFlow<Unit> = sessionManager.onLogout
    val currentLocalUserId: Flow<String?> = sessionManager.userIdFlow
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId.asStateFlow()
    private val _hasProfilePicture = MutableStateFlow<Boolean?>(null)
    val hasProfilePicture: StateFlow<Boolean?> = _hasProfilePicture.asStateFlow()
    private var observeHasProfilePictureJob: Job? = null

    // UI TopBarConfig
    private val _topBarConfig = MutableStateFlow(TopBarConfig())
    val topBarConfig: StateFlow<TopBarConfig> = _topBarConfig.asStateFlow()

    init {
        viewModelScope.launch {
            val curUserId = currentLocalUserId.firstOrNull()
            _currentUserId.value = curUserId

            if (!curUserId.isNullOrBlank()) {
                sessionManager.saveUserId(curUserId)
                startObservingHasProfilePicture(curUserId)
            } else {
                stopObservingHasProfilePicture()
            }
        }

        viewModelScope.launch {
            onLogout.collect {
                logoutUseCase()
            }
        }
    }

    // Métodos para actualizar el estado de la UI
    fun disableBars() {
        _topBarConfig.value = _topBarConfig.value.copy(
            disableTopBar = true,
            disableBottomBar = true
        )
    }

    fun enableBars() {
        _topBarConfig.value = _topBarConfig.value.copy(
            disableTopBar = false,
            disableBottomBar = false
        )
    }

    fun hideBars() {
        _topBarConfig.value = _topBarConfig.value.copy(
            showTopBar = false,
            showBottomBar = false
        )
    }

    fun showBars() {
        _topBarConfig.value = _topBarConfig.value.copy(
            showTopBar = true,
            showBottomBar = true
        )
    }

    fun showHideTopBar(show: Boolean) {
        _topBarConfig.value = _topBarConfig.value.copy(showTopBar = show)
    }

    fun showHideBottomBar(show: Boolean) {
        _topBarConfig.value = _topBarConfig.value.copy(showBottomBar = show)
    }

    fun enableDisableTopBar(enable: Boolean) {
        _topBarConfig.value = _topBarConfig.value.copy(disableTopBar = enable)
    }

    fun enableDisableBottomBar(enable: Boolean) {
        _topBarConfig.value = _topBarConfig.value.copy(disableBottomBar = enable)
    }

    fun hideTopBarSettingsIcon() {
        _topBarConfig.value = _topBarConfig.value.copy(showSettingsIcon = false)
    }

    fun showTopBarSettingsIcon() {
        _topBarConfig.value = _topBarConfig.value.copy(showSettingsIcon = true)
    }

    suspend fun setUserId(userId: String?) {
        userId?.let {
            sessionManager.saveUserId(it)
            _currentUserId.value = it
        }
    }

    fun startObservingHasProfilePicture(userId: String) {
        if (observeHasProfilePictureJob?.isActive == true) return
        observeHasProfilePictureJob = viewModelScope.launch {
            while (true) {
                when (val result = checkProfilePictureUseCase(userId)) {
                    is MiraiLinkResult.Success -> _hasProfilePicture.value = result.data
                    is MiraiLinkResult.Error -> _hasProfilePicture.value = false
                }
                delay(5000)
            }
        }
    }

    fun stopObservingHasProfilePicture() {
        observeHasProfilePictureJob?.cancel()
    }

    // Aparte del job
    suspend fun refreshHasProfilePicture(userId: String) {
        when (val result = checkProfilePictureUseCase(userId)) {
            is MiraiLinkResult.Success -> _hasProfilePicture.value = result.data
            is MiraiLinkResult.Error -> _hasProfilePicture.value = false
        }
    }
}