package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetThemePreferenceUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.SetThemePreferenceUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.CancellationException
import com.feryaeljustice.mirailink.domain.error.DataError
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getThemePreferenceUseCase: GetThemePreferenceUseCase,
    private val setThemePreferenceUseCase: SetThemePreferenceUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val holoPreferencesRepository: com.feryaeljustice.mirailink.domain.repository.HoloPreferencesRepository,
) : RetryableViewModel() {
    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()
    private val _error = MutableStateFlow<UiError?>(null)
    val error: StateFlow<UiError?> = _error
    private val _deleteSuccess = MutableSharedFlow<Boolean>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    private val _currentUser = MutableStateFlow<UserViewEntry?>(null)
    val currentUser: StateFlow<UserViewEntry?> = _currentUser.asStateFlow()

    val themePreference: StateFlow<ThemePreference> =
        getThemePreferenceUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ThemePreference.SYSTEM,
            )

    private val holoReadRetry = MutableStateFlow(0)
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val holoProfileEnabled: StateFlow<Boolean> = holoReadRetry.flatMapLatest {
      holoPreferencesRepository.observeEnabled()
        .catch { error ->
            if (error is CancellationException) throw error
            setRecoveryAction { _error.value = null; holoReadRetry.value++ }
            _error.value = DataError.Local.UNKNOWN.toUiError()
            emit(false)
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun setHoloProfileEnabled(enabled: Boolean) {
        setRecoveryAction { setHoloProfileEnabled(enabled) }
        _error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = holoPreferencesRepository.setEnabled(enabled)) {
                is MiraiLinkResult.Success -> Unit
                is MiraiLinkResult.Error -> withContext(mainDispatcher) { _error.value = result.error.toUiError() }
            }
        }
    }

    fun setThemePreference(themePreference: ThemePreference) {
        setRecoveryAction { setThemePreference(themePreference) }
        _error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = setThemePreferenceUseCase(themePreference)) {
                is MiraiLinkResult.Success -> Unit
                is MiraiLinkResult.Error -> withContext(mainDispatcher) {
                    _error.value = result.error.toUiError()
                }
            }
        }
    }

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    _currentUser.value = result.data.toUserViewEntry()
                }
                is MiraiLinkResult.Error -> {
                    // Silently ignore or log
                }
            }
        }
    }

    fun logout(onFinish: () -> Unit) {
        setRecoveryAction { logout(onFinish) }
        _error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = logoutUseCase()) {
                is MiraiLinkResult.Success -> withContext(mainDispatcher) {
                    _logoutSuccess.emit(true)
                    onFinish()
                }
                is MiraiLinkResult.Error -> withContext(mainDispatcher) {
                    _logoutSuccess.emit(false)
                    _error.value = result.error.toUiError()
                }
            }
        }
    }

    fun deleteAccount(onFinish: () -> Unit) {
        setRecoveryAction { deleteAccount(onFinish) }
        _error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = deleteAccountUseCase()) {
                is MiraiLinkResult.Success -> withContext(mainDispatcher) {
                    _deleteSuccess.emit(true)
                    onFinish()
                }
                is MiraiLinkResult.Error -> withContext(mainDispatcher) {
                    _deleteSuccess.emit(false)
                    _error.value = result.error.toUiError()
                }
            }
        }
    }
}
