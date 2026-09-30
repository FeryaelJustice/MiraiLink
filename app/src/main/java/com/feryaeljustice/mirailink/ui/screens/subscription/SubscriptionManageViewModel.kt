package com.feryaeljustice.mirailink.ui.screens.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.usecase.subscription.CancelSubscriptionIntentUseCase
import com.feryaeljustice.mirailink.domain.usecase.subscription.GetSubscriptionStatusUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SubscriptionManageUiState(
    val isLoading: Boolean = false,
    val isCanceling: Boolean = false,
    val subscriptionInfo: SubscriptionPlanInfo = SubscriptionPlanInfo(),
    val cancelPlayStoreUrl: String? = null,
    val error: UiError? = null,
)

class SubscriptionManageViewModel(
    private val getSubscriptionStatusUseCase: GetSubscriptionStatusUseCase,
    private val cancelSubscriptionIntentUseCase: CancelSubscriptionIntentUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionManageUiState())
    val uiState: StateFlow<SubscriptionManageUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getSubscriptionStatusUseCase.subscriptionInfo.collect { info ->
                _uiState.update { it.copy(subscriptionInfo = info) }
            }
        }
        refreshStatus()
    }

    fun refreshStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getSubscriptionStatusUseCase()) {
                is MiraiLinkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            subscriptionInfo = result.data,
                        )
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error.toUiError(),
                        )
                    }
                }
            }
        }
    }

    fun requestCancelIntent() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCanceling = true, error = null) }
            when (val result = cancelSubscriptionIntentUseCase()) {
                is MiraiLinkResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isCanceling = false,
                            cancelPlayStoreUrl = result.data,
                        )
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isCanceling = false,
                            error = result.error.toUiError(),
                        )
                    }
                }
            }
        }
    }

    fun clearCancelUrl() {
        _uiState.update { it.copy(cancelPlayStoreUrl = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
