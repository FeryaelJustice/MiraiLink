package com.feryaeljustice.mirailink.ui.screens.settings

import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.usecase.auth.LogoutUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.SaveSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.DeleteAccountUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.RetryableViewModel
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.isCountryCodeValid
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.UiText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SettingsViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase,
    private val saveSearchPreferencesUseCase: SaveSearchPreferencesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : RetryableViewModel() {

    private val _logoutSuccess = MutableSharedFlow<Boolean>()
    val logoutSuccess = _logoutSuccess.asSharedFlow()

    val error: StateFlow<UiError?>
        field = MutableStateFlow<UiError?>(null)

    private val _deleteSuccess = MutableSharedFlow<Boolean>()
    val deleteSuccess = _deleteSuccess.asSharedFlow()

    // Preferencias de busqueda cargadas del repositorio
    private val _savedPreferences = MutableStateFlow(SearchPreferences())
    val savedPreferences = _savedPreferences.asStateFlow()

    // Estado borrador editable en pantalla
    private val _draftRadiusKm = MutableStateFlow(40f)
    val draftRadiusKm = _draftRadiusKm.asStateFlow()

    private val _draftScope = MutableStateFlow(SearchScope.RADIUS)
    val draftScope = _draftScope.asStateFlow()

    private val _draftTargetCountry = MutableStateFlow<String?>(null)
    val draftTargetCountry = _draftTargetCountry.asStateFlow()

    private val _draftMatchLiveLocation = MutableStateFlow(false)
    val draftMatchLiveLocation = _draftMatchLiveLocation.asStateFlow()

    // Coordenadas del usuario para centrar el minimapa
    private val _userLatitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LATITUDE)
    val userLatitude = _userLatitude.asStateFlow()

    private val _userLongitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LONGITUDE)
    val userLongitude = _userLongitude.asStateFlow()

    private val _isSavingPreferences = MutableStateFlow(false)
    val isSavingPreferences = _isSavingPreferences.asStateFlow()

    val hasUnsavedChanges: StateFlow<Boolean> = combine(
        _savedPreferences,
        _draftRadiusKm,
        _draftScope,
        _draftTargetCountry,
        _draftMatchLiveLocation,
    ) { saved, radius, scope, targetCountry, matchLiveLocation ->
        saved.radiusKm != radius ||
            saved.scope != scope ||
            saved.targetCountryCode != targetCountry ||
            saved.matchByLiveLocation != matchLiveLocation
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        loadSearchPreferences()
        loadUserLocation()
    }

    private fun loadSearchPreferences() {
        viewModelScope.launch(ioDispatcher) {
            getSearchPreferencesUseCase().collect { prefs ->
                _savedPreferences.value = prefs
                _draftRadiusKm.value = prefs.radiusKm
                _draftScope.value = prefs.scope
                _draftTargetCountry.value = prefs.targetCountryCode
                _draftMatchLiveLocation.value = prefs.matchByLiveLocation
            }
        }
    }

    private fun loadUserLocation() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    val user = result.data
                    val lat = user.currentLatitude ?: user.residenceLatitude ?: GeoUtils.DEFAULT_FALLBACK_LATITUDE
                    val lon = user.currentLongitude ?: user.residenceLongitude ?: GeoUtils.DEFAULT_FALLBACK_LONGITUDE
                    _userLatitude.value = lat
                    _userLongitude.value = lon
                }
                is MiraiLinkResult.Error -> {
                    // Fallback to Palma default
                    _userLatitude.value = GeoUtils.DEFAULT_FALLBACK_LATITUDE
                    _userLongitude.value = GeoUtils.DEFAULT_FALLBACK_LONGITUDE
                }
            }
        }
    }

    fun updateUserCoordinates(latitude: Double, longitude: Double) {
        _userLatitude.value = latitude
        _userLongitude.value = longitude
    }

    val isTargetCountryValid: StateFlow<Boolean> = _draftTargetCountry.map { country ->
        country.isNullOrBlank() || country.isCountryCodeValid()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun updateDraftRadius(radius: Float) {
        _draftRadiusKm.value = radius.coerceIn(SearchPreferences.MIN_RADIUS_KM, SearchPreferences.MAX_RADIUS_KM)
    }

    fun updateDraftRadius(radius: Int) {
        _draftRadiusKm.value = radius.toFloat().coerceIn(SearchPreferences.MIN_RADIUS_KM, SearchPreferences.MAX_RADIUS_KM)
    }

    fun updateDraftScope(scope: SearchScope) {
        _draftScope.value = scope
    }

    fun updateDraftTargetCountry(country: String?) {
        _draftTargetCountry.value = country?.trim()?.uppercase()
    }

    fun updateDraftMatchLiveLocation(enabled: Boolean) {
        _draftMatchLiveLocation.value = enabled
    }

    fun saveSearchPreferences(onSuccess: () -> Unit) {
        // Validación del código de país si el scope es SPECIFIC_COUNTRY
        if (_draftScope.value == SearchScope.SPECIFIC_COUNTRY) {
            val country = _draftTargetCountry.value
            if (country.isNullOrBlank() || !country.isCountryCodeValid()) {
                error.value = UiError(
                    message = UiText.Resource(R.string.search_invalid_country_code),
                    actionLabel = UiText.Resource(R.string.accept),
                    recovery = ErrorRecovery.REVIEW_INPUT,
                )
                return
            }
        }

        val updated = SearchPreferences(
            radiusKm = _draftRadiusKm.value,
            scope = _draftScope.value,
            targetCountryCode = _draftTargetCountry.value,
            matchByLiveLocation = _draftMatchLiveLocation.value,
        )
        _isSavingPreferences.value = true
        viewModelScope.launch(ioDispatcher) {
            val result = saveSearchPreferencesUseCase(updated)
            withContext(mainDispatcher) {
                _isSavingPreferences.value = false
                when (result) {
                    is MiraiLinkResult.Success -> {
                        _savedPreferences.value = updated
                        onSuccess()
                    }
                    is MiraiLinkResult.Error -> {
                        error.value = result.error.toUiError()
                    }
                }
            }
        }
    }

    fun logout(onFinish: () -> Unit) {
        setRecoveryAction { logout(onFinish) }
        error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = logoutUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _logoutSuccess.emit(false)
                        error.value = result.error.toUiError()
                    }
                }
            }
        }
    }

    fun deleteAccount(onFinish: () -> Unit) {
        setRecoveryAction { deleteAccount(onFinish) }
        error.value = null
        viewModelScope.launch(ioDispatcher) {
            when (val result = deleteAccountUseCase()) {
                is MiraiLinkResult.Success -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(true)
                        onFinish()
                    }
                }

                is MiraiLinkResult.Error -> {
                    withContext(mainDispatcher) {
                        _deleteSuccess.emit(false)
                        error.value = result.error.toUiError()
                    }
                }
            }
        }
    }
}
