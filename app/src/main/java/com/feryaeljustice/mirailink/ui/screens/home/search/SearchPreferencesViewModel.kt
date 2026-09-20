package com.feryaeljustice.mirailink.ui.screens.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.SaveSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.domain.util.isCountryCodeValid
import com.feryaeljustice.mirailink.ui.error.ErrorRecovery
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.UiText
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchPreferencesViewModel(
    private val getSearchPreferencesUseCase: GetSearchPreferencesUseCase,
    private val saveSearchPreferencesUseCase: SaveSearchPreferencesUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val savedPreferences = MutableStateFlow(SearchPreferences())
    private val _draftRadiusKm = MutableStateFlow(40f)
    val draftRadiusKm = _draftRadiusKm.asStateFlow()
    private val _draftScope = MutableStateFlow(SearchScope.RADIUS)
    val draftScope = _draftScope.asStateFlow()
    private val _draftTargetCountry = MutableStateFlow<String?>(null)
    val draftTargetCountry = _draftTargetCountry.asStateFlow()
    private val _draftMatchLiveLocation = MutableStateFlow(false)
    val draftMatchLiveLocation = _draftMatchLiveLocation.asStateFlow()
    private val _userLatitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LATITUDE)
    val userLatitude = _userLatitude.asStateFlow()
    private val _userLongitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LONGITUDE)
    val userLongitude = _userLongitude.asStateFlow()
    private val _isSavingPreferences = MutableStateFlow(false)
    val isSavingPreferences = _isSavingPreferences.asStateFlow()
    private val _error = MutableStateFlow<UiError?>(null)
    val error = _error.asStateFlow()

    val hasUnsavedChanges: StateFlow<Boolean> = combine(
        savedPreferences, _draftRadiusKm, _draftScope, _draftTargetCountry, _draftMatchLiveLocation,
    ) { saved, radius, scope, targetCountry, matchLiveLocation ->
        saved.radiusKm != radius || saved.scope != scope ||
            saved.targetCountryCode != targetCountry || saved.matchByLiveLocation != matchLiveLocation
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    init {
        reload()
    }

    fun reload() {
        loadSearchPreferences()
        loadUserLocation()
    }

    private fun loadSearchPreferences() {
        viewModelScope.launch(ioDispatcher) {
            getSearchPreferencesUseCase().collect { preferences ->
                savedPreferences.value = preferences
                _draftRadiusKm.value = preferences.radiusKm
                _draftScope.value = preferences.scope
                _draftTargetCountry.value = preferences.targetCountryCode
                _draftMatchLiveLocation.value = preferences.matchByLiveLocation
            }
        }
    }

    private fun loadUserLocation() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    _userLatitude.value = result.data.currentLatitude ?: result.data.residenceLatitude ?: GeoUtils.DEFAULT_FALLBACK_LATITUDE
                    _userLongitude.value = result.data.currentLongitude ?: result.data.residenceLongitude ?: GeoUtils.DEFAULT_FALLBACK_LONGITUDE
                }
                is MiraiLinkResult.Error -> Unit
            }
        }
    }

    fun updateUserCoordinates(latitude: Double, longitude: Double) {
        _userLatitude.value = latitude
        _userLongitude.value = longitude
    }

    fun updateDraftRadius(radius: Float) {
        _draftRadiusKm.value = radius.coerceIn(SearchPreferences.MIN_RADIUS_KM, SearchPreferences.MAX_RADIUS_KM)
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

    fun save(onSuccess: () -> Unit) {
        val targetCountry = _draftTargetCountry.value
        if (_draftScope.value == SearchScope.SPECIFIC_COUNTRY &&
            (targetCountry.isNullOrBlank() || !targetCountry.isCountryCodeValid())
        ) {
            _error.value = UiError(UiText.Resource(R.string.search_invalid_country_code), UiText.Resource(R.string.accept), ErrorRecovery.REVIEW_INPUT)
            return
        }
        val updated = SearchPreferences(_draftRadiusKm.value, _draftScope.value, targetCountry, _draftMatchLiveLocation.value)
        _isSavingPreferences.value = true
        viewModelScope.launch(ioDispatcher) {
            when (val result = saveSearchPreferencesUseCase(updated)) {
                is MiraiLinkResult.Success -> withContext(mainDispatcher) {
                    savedPreferences.value = updated
                    _isSavingPreferences.value = false
                    onSuccess()
                }
                is MiraiLinkResult.Error -> withContext(mainDispatcher) {
                    _isSavingPreferences.value = false
                    _error.value = result.error.toUiError()
                }
            }
        }
    }
}
