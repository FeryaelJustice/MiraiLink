package com.feryaeljustice.mirailink.data.repository

import android.app.Activity
import com.android.billingclient.api.BillingClient
import com.feryaeljustice.mirailink.data.billing.BillingClientManager
import com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent
import com.feryaeljustice.mirailink.data.model.request.subscription.CancelSubscriptionIntentRequest
import com.feryaeljustice.mirailink.data.model.request.subscription.VerifySubscriptionRequest
import com.feryaeljustice.mirailink.data.model.response.subscription.SubscriptionStatusDto
import com.feryaeljustice.mirailink.data.remote.SubscriptionApiService
import com.feryaeljustice.mirailink.data.util.NetworkOperation
import com.feryaeljustice.mirailink.data.util.safeApiCall
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession

import com.android.billingclient.api.ProductDetails
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption

class SubscriptionRepositoryImpl(
    private val apiService: SubscriptionApiService,
    private val billingClientManager: BillingClientManager,
    private val scope: CoroutineScope,
    private val session: GlobalMiraiLinkSession? = null,
) : SubscriptionRepository {

    private val restoreMutex = Mutex()

    private val _subscriptionInfo = MutableStateFlow(SubscriptionPlanInfo())
    override val subscriptionInfo: StateFlow<SubscriptionPlanInfo> = _subscriptionInfo.asStateFlow()

    private val _purchaseEvents = MutableSharedFlow<BillingPurchaseEvent>(extraBufferCapacity = 5)
    override val purchaseEvents: Flow<BillingPurchaseEvent> = _purchaseEvents.asSharedFlow()

    override val formattedPrice: StateFlow<String?> = billingClientManager.productDetails
        .map { details ->
            details?.subscriptionOfferDetails
                ?.firstOrNull { it.basePlanId == BillingClientManager.BASE_PLAN_MONTHLY }
                ?.pricingPhases
                ?.pricingPhaseList
                ?.firstOrNull()
                ?.formattedPrice
                ?: details?.subscriptionOfferDetails
                    ?.firstOrNull()
                    ?.pricingPhases
                    ?.pricingPhaseList
                    ?.firstOrNull()
                    ?.formattedPrice
        }.stateIn(scope, SharingStarted.Eagerly, null)

    override val availableOffers: StateFlow<Map<SubscriptionPlanType, List<SubscriptionOfferOption>>> =
        billingClientManager.productDetailsMap
            .map { map -> mapOffers(map) }
            .stateIn(scope, SharingStarted.Eagerly, mapOffers(emptyMap()))

    init {
        // Play retains purchased tokens across process death. Retry server delivery without
        // persisting payment tokens locally, and stop recovery when the account changes.
        if (session != null) {
            scope.launch {
                combine(
                    billingClientManager.isConnected,
                    session.currentUserId,
                    session.isAuthenticated,
                    session.isDemoMode,
                ) { connected, userId, authenticated, demo ->
                    userId?.takeIf { connected && authenticated && !demo }
                }.collectLatest { userId ->
                    if (userId != null) {
                        while (isActive) {
                            val result = restorePurchases()
                            delay(if (result is MiraiLinkResult.Error) 60_000L else 3_600_000L)
                        }
                    }
                }
            }
        }
        scope.launch {
            billingClientManager.purchaseEvents.collect { event ->
                if (event is BillingPurchaseEvent.PurchaseSuccess) {
                    val result = verifyPurchase(
                        purchaseToken = event.purchase.purchaseToken,
                        productId = event.purchase.products.firstOrNull() ?: BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID,
                        basePlanId = BillingClientManager.BASE_PLAN_MONTHLY,
                        orderId = event.purchase.orderId,
                    )
                    // Google accepting payment is not sufficient to grant server entitlements.
                    when (result) {
                        is MiraiLinkResult.Success -> _purchaseEvents.emit(event)
                        is MiraiLinkResult.Error -> _purchaseEvents.emit(
                            BillingPurchaseEvent.VerificationFailed(result.error),
                        )
                    }
                } else {
                    _purchaseEvents.emit(event)
                }
            }
        }
    }

    override suspend fun fetchSubscriptionStatus(): MiraiLinkResult<SubscriptionPlanInfo> {
        val result = safeApiCall(NetworkOperation.AUTHENTICATED) {
            apiService.getSubscriptionStatus()
        }
        return when (result) {
            is MiraiLinkResult.Success -> {
                val info = result.data.toDomain(formattedPrice.value)
                _subscriptionInfo.value = info
                session?.setSubscriptionState(premium = info.isPremium, plus = info.isPlus)
                MiraiLinkResult.Success(info)
            }
            is MiraiLinkResult.Error -> result
        }
    }

    override fun launchBillingFlow(
        activity: Activity,
        productId: String,
        basePlanId: String,
    ): MiraiLinkResult<Unit> {
        val billingResult = billingClientManager.launchBillingFlow(activity, productId, basePlanId)
        return when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> MiraiLinkResult.Success(Unit)
            BillingClient.BillingResponseCode.ITEM_UNAVAILABLE -> MiraiLinkResult.Error(SubscriptionError.PLAN_UNAVAILABLE)
            else -> MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE)
        }
    }

    override suspend fun verifyPurchase(
        purchaseToken: String,
        productId: String,
        basePlanId: String,
        orderId: String?,
    ): MiraiLinkResult<SubscriptionPlanInfo> {
        val request = VerifySubscriptionRequest(
            purchaseToken = purchaseToken,
            productId = productId,
            basePlanId = basePlanId,
            orderId = orderId,
        )
        val result = safeApiCall(NetworkOperation.AUTHENTICATED) {
            apiService.verifySubscription(request)
        }
        return when (result) {
            is MiraiLinkResult.Success -> {
                val info = result.data.toDomain(formattedPrice.value)
                _subscriptionInfo.value = info
                session?.setSubscriptionState(premium = info.isPremium, plus = info.isPlus)
                MiraiLinkResult.Success(info)
            }
            is MiraiLinkResult.Error -> result
        }
    }

    override suspend fun restorePurchases(): MiraiLinkResult<SubscriptionPlanInfo> = restoreMutex.withLock {
        val purchases = billingClientManager.queryActivePurchases()
        val active = purchases.filter {
            it.products.contains(BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID) ||
                it.products.contains(BillingClientManager.PLUS_SUBSCRIPTION_PRODUCT_ID)
        }

        var failure: MiraiLinkResult.Error? = null
        var verified: MiraiLinkResult.Success<SubscriptionPlanInfo>? = null
        for (purchase in active) {
            val prodId = purchase.products.first {
                it == BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID || it == BillingClientManager.PLUS_SUBSCRIPTION_PRODUCT_ID
            }

            val result = verifyPurchase(
                purchaseToken = purchase.purchaseToken,
                productId = prodId,
                basePlanId = BillingClientManager.BASE_PLAN_MONTHLY,
                orderId = purchase.orderId,
            )
            when (result) {
                is MiraiLinkResult.Error -> failure = result
                is MiraiLinkResult.Success -> verified = result
            }
        }
        // The server resolves replacements and returns the authoritative final tier.
        failure ?: verified ?: fetchSubscriptionStatus()
    }

    override suspend fun requestCancelIntent(): MiraiLinkResult<String> {
        val result = safeApiCall(NetworkOperation.AUTHENTICATED) {
            apiService.cancelSubscriptionIntent(CancelSubscriptionIntentRequest())
        }
        return when (result) {
            is MiraiLinkResult.Success -> MiraiLinkResult.Success(result.data.playStoreUrl)
            is MiraiLinkResult.Error -> result
        }
    }

    private fun SubscriptionStatusDto.toDomain(priceString: String?): SubscriptionPlanInfo =
        SubscriptionPlanInfo(
            planType = when {
                isPremium || plan == "premium" -> SubscriptionPlanType.PREMIUM
                isPlus || plan == "plus" -> SubscriptionPlanType.PLUS
                else -> SubscriptionPlanType.FREE
            },
            isPremium = isPremium || plan == "premium",
            isPlus = isPlus || isPremium || plan == "plus" || plan == "premium",
            status = status,
            productId = productId,
            basePlanId = basePlanId,
            formattedPrice = priceString,
            expiresAt = expiresAt,
            autoRenewing = autoRenewing,
        )

    private fun mapOffers(map: Map<String, ProductDetails>): Map<SubscriptionPlanType, List<SubscriptionOfferOption>> {
        val plusDetails = map[BillingClientManager.PLUS_SUBSCRIPTION_PRODUCT_ID]
        val premiumDetails = map[BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID]

        return mapOf(
            SubscriptionPlanType.PLUS to buildTierOffers(
                details = plusDetails,
                productId = BillingClientManager.PLUS_SUBSCRIPTION_PRODUCT_ID,
                defaultWeeklyPrice = "0,49 €",
                defaultMonthlyPrice = "0,99 €",
                defaultThreeMonthsPrice = "2,49 €",
                defaultThreeMonthsPerPeriod = "0,83 € / mes",
            ),
            SubscriptionPlanType.PREMIUM to buildTierOffers(
                details = premiumDetails,
                productId = BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID,
                defaultWeeklyPrice = "0,99 €",
                defaultMonthlyPrice = "1,99 €",
                defaultThreeMonthsPrice = "4,99 €",
                defaultThreeMonthsPerPeriod = "1,66 € / mes",
            ),
        )
    }

    private fun buildTierOffers(
        details: ProductDetails?,
        productId: String,
        defaultWeeklyPrice: String,
        defaultMonthlyPrice: String,
        defaultThreeMonthsPrice: String,
        defaultThreeMonthsPerPeriod: String,
    ): List<SubscriptionOfferOption> {
        val weeklyOffer = details?.subscriptionOfferDetails?.firstOrNull {
            it.basePlanId == BillingClientManager.BASE_PLAN_WEEKLY || it.basePlanId.contains("week", ignoreCase = true)
        }
        val monthlyOffer = details?.subscriptionOfferDetails?.firstOrNull {
            it.basePlanId == BillingClientManager.BASE_PLAN_MONTHLY ||
                (it.basePlanId.contains("month", ignoreCase = true) && !it.basePlanId.contains("three", ignoreCase = true) && !it.basePlanId.contains("3", ignoreCase = true))
        }
        val threeMonthOffer = details?.subscriptionOfferDetails?.firstOrNull {
            it.basePlanId == BillingClientManager.BASE_PLAN_THREEE_MONTHS ||
                it.basePlanId == BillingClientManager.BASE_PLAN_THREE_MONTHS ||
                it.basePlanId.contains("threee-month", ignoreCase = true) ||
                it.basePlanId.contains("three-month", ignoreCase = true) ||
                it.basePlanId.contains("3-month", ignoreCase = true)
        }

        val weeklyPrice = weeklyOffer?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: defaultWeeklyPrice
        val monthlyPrice = monthlyOffer?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: defaultMonthlyPrice
        val threeMonthsPrice = threeMonthOffer?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: defaultThreeMonthsPrice

        val defaultThreeMonthPlanId = if (productId == BillingClientManager.PREMIUM_SUBSCRIPTION_PRODUCT_ID) {
            BillingClientManager.BASE_PLAN_THREEE_MONTHS
        } else {
            BillingClientManager.BASE_PLAN_THREE_MONTHS
        }

        return listOf(
            SubscriptionOfferOption(
                duration = SubscriptionDuration.WEEKLY,
                productId = productId,
                basePlanId = weeklyOffer?.basePlanId ?: BillingClientManager.BASE_PLAN_WEEKLY,
                formattedPrice = weeklyPrice,
                formattedPricePerPeriod = "$weeklyPrice / sem",
                discountBadge = null,
                offerToken = weeklyOffer?.offerToken ?: "",
            ),
            SubscriptionOfferOption(
                duration = SubscriptionDuration.MONTHLY,
                productId = productId,
                basePlanId = monthlyOffer?.basePlanId ?: BillingClientManager.BASE_PLAN_MONTHLY,
                formattedPrice = monthlyPrice,
                formattedPricePerPeriod = "$monthlyPrice / mes",
                discountBadge = "Popular",
                offerToken = monthlyOffer?.offerToken ?: "",
            ),
            SubscriptionOfferOption(
                duration = SubscriptionDuration.THREE_MONTHS,
                productId = productId,
                basePlanId = threeMonthOffer?.basePlanId ?: defaultThreeMonthPlanId,
                formattedPrice = threeMonthsPrice,
                formattedPricePerPeriod = defaultThreeMonthsPerPeriod,
                discountBadge = "Ahorras 16%",
                offerToken = threeMonthOffer?.offerToken ?: "",
            ),
        )
    }
}
