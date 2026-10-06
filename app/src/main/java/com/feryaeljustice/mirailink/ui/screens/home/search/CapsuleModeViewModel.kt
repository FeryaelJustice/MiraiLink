package com.feryaeljustice.mirailink.ui.screens.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.repository.CapsuleRepository
import com.feryaeljustice.mirailink.domain.repository.SearchPreferencesRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CapsuleModeViewModel(private val repository: CapsuleRepository, private val preferences: SearchPreferencesRepository,
    private val analytics: com.feryaeljustice.mirailink.domain.telemetry.AnalyticsTracker? = null) : ViewModel() {
    val mode = preferences.getSearchPreferences().map { it.discoveryMode }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "classic")
    val available: StateFlow<Boolean>
        field = MutableStateFlow(false)
    val availabilityLoading: StateFlow<Boolean>
        field = MutableStateFlow(false)
    val busy: StateFlow<Boolean>
        field = MutableStateFlow(false)
    val error: StateFlow<com.feryaeljustice.mirailink.ui.error.UiError?>
        field = MutableStateFlow<com.feryaeljustice.mirailink.ui.error.UiError?>(null)
    private var availabilityRequestId = 0L

    fun refresh() {
        val requestId = ++availabilityRequestId
        available.value = false
        availabilityLoading.value = true
        viewModelScope.launch {
            try {
                val isAvailable = repository.available()
                if (requestId == availabilityRequestId) available.value = isAvailable
            } finally {
                if (requestId == availabilityRequestId) availabilityLoading.value = false
            }
        }
    }
    fun select(mode: String) {
        if (busy.value || (mode == "capsule" && (availabilityLoading.value || !available.value))) return
        viewModelScope.launch {
            busy.value = true
            try {
                when(val result = preferences.saveSearchPreferences(preferences.getSearchPreferences().first().copy(discoveryMode = mode))) {
                    is MiraiLinkResult.Success -> {
                        error.value = null
                        analytics?.logEvent("capsule_discovery_mode", mapOf("mode" to mode))
                    }
                    is MiraiLinkResult.Error -> error.value = result.error.toUiError()
                }
            } finally { busy.value = false }
        }
    }
}
