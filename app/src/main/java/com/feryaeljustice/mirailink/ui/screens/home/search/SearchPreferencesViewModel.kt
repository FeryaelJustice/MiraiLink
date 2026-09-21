package com.feryaeljustice.mirailink.ui.screens.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.usecase.settings.GetSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.settings.SaveSearchPreferencesUseCase
import com.feryaeljustice.mirailink.domain.usecase.location.SendLocationPingUseCase
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
    private val sendLocationPingUseCase: SendLocationPingUseCase,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val savedPreferences = MutableStateFlow(SearchPreferences())
    private val _draftRadiusKm = MutableStateFlow(40f)
    val draftRadiusKm = _draftRadiusKm.asStateFlow()
    private val _draftScope = MutableStateFlow(SearchScope.RADIUS_RESIDENCE)
    val draftScope = _draftScope.asStateFlow()
    private val _draftTargetCountry = MutableStateFlow<String?>(null)
    val draftTargetCountry = _draftTargetCountry.asStateFlow()
    private val _userLatitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LATITUDE)
    val userLatitude = _userLatitude.asStateFlow()
    private val _userLongitude = MutableStateFlow(GeoUtils.DEFAULT_FALLBACK_LONGITUDE)
    val userLongitude = _userLongitude.asStateFlow()
    private var residenceLatitude: Double? = null
    private var residenceLongitude: Double? = null
    private val _residenceLabel = MutableStateFlow<String?>(null)
    val residenceLabel = _residenceLabel.asStateFlow()
    private val _residenceCoordinatesMissing = MutableStateFlow(true)
    val residenceCoordinatesMissing = _residenceCoordinatesMissing.asStateFlow()
    private var activeLatitude: Double? = null
    private var activeLongitude: Double? = null
    private val _isSavingPreferences = MutableStateFlow(false)
    val isSavingPreferences = _isSavingPreferences.asStateFlow()
    private val _error = MutableStateFlow<UiError?>(null)
    val error = _error.asStateFlow()

    val hasUnsavedChanges: StateFlow<Boolean> = combine(
        savedPreferences, _draftRadiusKm, _draftScope, _draftTargetCountry,
    ) { saved, radius, scope, targetCountry ->
        saved.radiusKm != radius || saved.scope != scope ||
            saved.targetCountryCode != targetCountry
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
            }
        }
    }

    private fun loadUserLocation() {
        viewModelScope.launch(ioDispatcher) {
            when (val result = getCurrentUserUseCase()) {
                is MiraiLinkResult.Success -> {
                    _residenceLabel.value = listOfNotNull(
                        result.data.residenceCity,
                        result.data.residenceRegion,
                        result.data.residenceCountryCode,
                    ).joinToString(", ").ifBlank { null }
                    residenceLatitude = result.data.residenceLatitude
                    residenceLongitude = result.data.residenceLongitude
                    _residenceCoordinatesMissing.value =
                        result.data.residenceLatitude == null || result.data.residenceLongitude == null
                    activeLatitude = result.data.currentLatitude
                    activeLongitude = result.data.currentLongitude
                    updateMapCenter(_draftScope.value)
                }
                is MiraiLinkResult.Error -> Unit
            }
        }
    }

    fun updateResidenceCoordinates(latitude: Double, longitude: Double) {
        residenceLatitude = latitude
        residenceLongitude = longitude
        _residenceCoordinatesMissing.value = false
        if (_draftScope.value == SearchScope.RADIUS_RESIDENCE) updateMapCenter(_draftScope.value)
    }


    fun updateUserCoordinates(latitude: Double, longitude: Double) {
        activeLatitude = latitude
        activeLongitude = longitude
        if (_draftScope.value == SearchScope.RADIUS_ACTIVE) {
            updateMapCenter(_draftScope.value)
        }
        viewModelScope.launch(ioDispatcher) {
            sendLocationPingUseCase(latitude, longitude)
        }
    }

    fun updateDraftRadius(radius: Float) {
        _draftRadiusKm.value = radius.coerceIn(SearchPreferences.MIN_RADIUS_KM, SearchPreferences.MAX_RADIUS_KM)
    }

    fun updateDraftScope(scope: SearchScope) {
        _draftScope.value = scope
        updateMapCenter(scope)
    }

    private fun updateMapCenter(scope: SearchScope) {
        val latitude = if (scope == SearchScope.RADIUS_ACTIVE) activeLatitude else residenceLatitude
        val longitude = if (scope == SearchScope.RADIUS_ACTIVE) activeLongitude else residenceLongitude
        _userLatitude.value = latitude ?: GeoUtils.DEFAULT_FALLBACK_LATITUDE
        _userLongitude.value = longitude ?: GeoUtils.DEFAULT_FALLBACK_LONGITUDE
    }

    fun updateDraftTargetCountry(country: String?) {
        _draftTargetCountry.value = country?.trim()?.uppercase()
    }

    fun save(onSuccess: () -> Unit) {
        val targetCountry = _draftTargetCountry.value
        if (_draftScope.value == SearchScope.SPECIFIC_COUNTRY &&
            (targetCountry.isNullOrBlank() || !targetCountry.isCountryCodeValid())
        ) {
            _error.value = UiError(UiText.Resource(R.string.search_invalid_country_code), UiText.Resource(R.string.accept), ErrorRecovery.REVIEW_INPUT)
            return
        }
        val updated = SearchPreferences(
            _draftRadiusKm.value,
            _draftScope.value,
            targetCountry.takeIf { _draftScope.value == SearchScope.SPECIFIC_COUNTRY },
        )
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
