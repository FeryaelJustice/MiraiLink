package com.feryaeljustice.mirailink.ui.screens.subscription

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.billing.BillingClientManager
import com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.usecase.subscription.LaunchBillingFlowUseCase
import com.feryaeljustice.mirailink.domain.usecase.subscription.RestorePurchasesUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.ui.error.UiError
import com.feryaeljustice.mirailink.ui.error.toUiError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SubscriptionPaywallUiState(
    val selectedTier: SubscriptionPlanType = SubscriptionPlanType.PREMIUM,
    val selectedDuration: SubscriptionDuration = SubscriptionDuration.MONTHLY,
    val availableOffers: Map<SubscriptionPlanType, List<SubscriptionOfferOption>> = emptyMap(),
    val formattedPrice: String? = null,
    val isPurchasing: Boolean = false,
    val isRestoring: Boolean = false,
    val isSuccess: Boolean = false,
    val messageResId: Int? = null,
    val error: UiError? = null,
) {
    val currentOffers: List<SubscriptionOfferOption>
        get() = availableOffers[selectedTier] ?: emptyList()

    val currentOffer: SubscriptionOfferOption?
        get() = currentOffers.firstOrNull { it.duration == selectedDuration } ?: currentOffers.firstOrNull()
}

class SubscriptionPaywallViewModel(
    private val launchBillingFlowUseCase: LaunchBillingFlowUseCase,
    private val restorePurchasesUseCase: RestorePurchasesUseCase,
    private val subscriptionRepository: SubscriptionRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SubscriptionPaywallUiState())
    val uiState: StateFlow<SubscriptionPaywallUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            subscriptionRepository.availableOffers.collect { offers ->
                _uiState.update { current ->
                    current.copy(
                        availableOffers = offers,
                        formattedPrice = offers[current.selectedTier]
                            ?.firstOrNull { it.duration == current.selectedDuration }
                            ?.formattedPrice,
                    )
                }
            }
        }

        viewModelScope.launch {
            subscriptionRepository.subscriptionInfo.collect { info ->
                if (info.isPremium || info.isPlus) {
                    _uiState.update { it.copy(isSuccess = true, isPurchasing = false) }
                }
            }
        }

        viewModelScope.launch {
            subscriptionRepository.purchaseEvents.collect { event ->
                when (event) {
                    is BillingPurchaseEvent.PurchaseSuccess,
                    is BillingPurchaseEvent.PurchaseCanceled,
                    is BillingPurchaseEvent.PurchaseFailed -> {
                        _uiState.update { it.copy(isPurchasing = false) }
                    }
                }
            }
        }
    }

    fun selectTier(tier: SubscriptionPlanType) {
        if (tier == SubscriptionPlanType.FREE) return
        _uiState.update { current ->
            val newOffers = current.availableOffers[tier] ?: emptyList()
            val newPrice = newOffers.firstOrNull { it.duration == current.selectedDuration }?.formattedPrice
            current.copy(
                selectedTier = tier,
                formattedPrice = newPrice,
            )
        }
    }

    fun selectDuration(duration: SubscriptionDuration) {
        _uiState.update { current ->
            val offer = current.availableOffers[current.selectedTier]?.firstOrNull { it.duration == duration }
            current.copy(
                selectedDuration = duration,
                formattedPrice = offer?.formattedPrice ?: current.formattedPrice,
            )
        }
    }

    fun startPurchase(activity: Activity) {
        val state = _uiState.value
        val offer = state.currentOffer
        val productId = offer?.productId
            ?: if (state.selectedTier == SubscriptionPlanType.PLUS) {
                BillingClientManager.PLUS_SUBSCRIPTION_PRODUCT_ID
            } else {
                BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID
            }
        val basePlanId = offer?.basePlanId ?: when (state.selectedDuration) {
            SubscriptionDuration.WEEKLY -> BillingClientManager.BASE_PLAN_WEEKLY
            SubscriptionDuration.MONTHLY -> BillingClientManager.BASE_PLAN_MONTHLY
            SubscriptionDuration.THREE_MONTHS -> if (productId == BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID) {
                BillingClientManager.BASE_PLAN_THREEE_MONTHS
            } else {
                BillingClientManager.BASE_PLAN_THREE_MONTHS
            }
        }

        _uiState.update { it.copy(isPurchasing = true, error = null) }
        when (val result = launchBillingFlowUseCase(activity, productId, basePlanId)) {
            is MiraiLinkResult.Success -> {
                // Billing flow initiated, wait for purchaseEvents
            }
            is MiraiLinkResult.Error -> {
                _uiState.update {
                    it.copy(
                        isPurchasing = false,
                        error = result.error.toUiError(),
                    )
                }
            }
        }
    }

    fun restorePurchases() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRestoring = true, error = null) }
            when (val result = restorePurchasesUseCase()) {
                is MiraiLinkResult.Success -> {
                    val hasActiveSub = result.data.isPremium || result.data.isPlus
                    _uiState.update {
                        it.copy(
                            isRestoring = false,
                            isSuccess = hasActiveSub,
                            messageResId = if (hasActiveSub) {
                                R.string.subscription_restore_success_message
                            } else {
                                R.string.subscription_restore_empty_message
                            },
                        )
                    }
                }
                is MiraiLinkResult.Error -> {
                    _uiState.update {
                        it.copy(
                            isRestoring = false,
                            error = result.error.toUiError(),
                        )
                    }
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(messageResId = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
