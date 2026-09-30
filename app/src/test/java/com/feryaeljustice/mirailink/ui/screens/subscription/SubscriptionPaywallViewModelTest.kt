package com.feryaeljustice.mirailink.ui.screens.subscription

import android.app.Activity
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.usecase.subscription.LaunchBillingFlowUseCase
import com.feryaeljustice.mirailink.domain.usecase.subscription.RestorePurchasesUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionPaywallViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val launchBillingFlowUseCase: LaunchBillingFlowUseCase = mockk()
    private val restorePurchasesUseCase: RestorePurchasesUseCase = mockk()
    private val subscriptionRepository: SubscriptionRepository = mockk()

    private val formattedPriceFlow = MutableStateFlow<String?>("1,99 €")
    private val subscriptionInfoFlow = MutableStateFlow(SubscriptionPlanInfo())
    private val purchaseEventsFlow = MutableSharedFlow<BillingPurchaseEvent>()
    private val sampleOffers = mapOf(
        SubscriptionPlanType.PLUS to listOf(
            com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption(
                duration = com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.WEEKLY,
                productId = "mirailink_plus",
                basePlanId = "weekly-autorenew",
                formattedPrice = "0,49 €",
                formattedPricePerPeriod = "0,49 € / sem",
            ),
            com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption(
                duration = com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.MONTHLY,
                productId = "mirailink_plus",
                basePlanId = "monthly-autorenew",
                formattedPrice = "0,99 €",
                formattedPricePerPeriod = "0,99 € / mes",
            ),
        ),
        SubscriptionPlanType.PREMIUM to listOf(
            com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption(
                duration = com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.WEEKLY,
                productId = "mirailink_premium",
                basePlanId = "weekly-autorenew",
                formattedPrice = "0,99 €",
                formattedPricePerPeriod = "0,99 € / sem",
            ),
            com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption(
                duration = com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.MONTHLY,
                productId = "mirailink_premium",
                basePlanId = "monthly-autorenew",
                formattedPrice = "1,99 €",
                formattedPricePerPeriod = "1,99 € / mes",
            ),
        ),
    )
    private val availableOffersFlow = MutableStateFlow(sampleOffers)

    private lateinit var viewModel: SubscriptionPaywallViewModel

    @Before
    fun setUp() {
        every { subscriptionRepository.formattedPrice } returns formattedPriceFlow
        every { subscriptionRepository.subscriptionInfo } returns subscriptionInfoFlow
        every { subscriptionRepository.purchaseEvents } returns purchaseEventsFlow
        every { subscriptionRepository.availableOffers } returns availableOffersFlow

        viewModel = SubscriptionPaywallViewModel(
            launchBillingFlowUseCase = launchBillingFlowUseCase,
            restorePurchasesUseCase = restorePurchasesUseCase,
            subscriptionRepository = subscriptionRepository,
        )
    }

    @Test
    fun `initial state reflects formatted price from repository`() = runTest {
        advanceUntilIdle()
        assertEquals("1,99 €", viewModel.uiState.value.formattedPrice)
        assertEquals(SubscriptionPlanType.PREMIUM, viewModel.uiState.value.selectedTier)
        assertEquals(com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.MONTHLY, viewModel.uiState.value.selectedDuration)
        assertFalse(viewModel.uiState.value.isPurchasing)
        assertFalse(viewModel.uiState.value.isRestoring)
        assertFalse(viewModel.uiState.value.isSuccess)
    }

    @Test
    fun `selectTier updates selectedTier and price`() = runTest {
        advanceUntilIdle()
        viewModel.selectTier(SubscriptionPlanType.PLUS)
        assertEquals(SubscriptionPlanType.PLUS, viewModel.uiState.value.selectedTier)
        assertEquals("0,99 €", viewModel.uiState.value.formattedPrice)
    }

    @Test
    fun `selectDuration updates selectedDuration and price`() = runTest {
        advanceUntilIdle()
        viewModel.selectDuration(com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.WEEKLY)
        assertEquals(com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration.WEEKLY, viewModel.uiState.value.selectedDuration)
        assertEquals("0,99 €", viewModel.uiState.value.formattedPrice)
    }

    @Test
    fun `startPurchase success initiates flow and keeps purchasing state`() = runTest {
        val mockActivity: Activity = mockk(relaxed = true)
        every { launchBillingFlowUseCase(mockActivity, any(), any()) } returns MiraiLinkResult.Success(Unit)

        viewModel.startPurchase(mockActivity)

        assertTrue(viewModel.uiState.value.isPurchasing)
    }

    @Test
    fun `startPurchase error updates error in state`() = runTest {
        val mockActivity: Activity = mockk(relaxed = true)
        every { launchBillingFlowUseCase(mockActivity, any(), any()) } returns MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE)

        viewModel.startPurchase(mockActivity)

        assertFalse(viewModel.uiState.value.isPurchasing)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `restorePurchases success with active subscription sets isSuccess true`() = runTest {
        val activeInfo = SubscriptionPlanInfo(
            planType = SubscriptionPlanType.PREMIUM,
            isPremium = true,
            status = "active",
        )
        coEvery { restorePurchasesUseCase() } returns MiraiLinkResult.Success(activeInfo)

        viewModel.restorePurchases()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isRestoring)
        assertTrue(viewModel.uiState.value.isSuccess)
        assertEquals(R.string.subscription_restore_success_message, viewModel.uiState.value.messageResId)
    }

    @Test
    fun `restorePurchases success without active subscription shows empty message`() = runTest {
        val freeInfo = SubscriptionPlanInfo(
            planType = SubscriptionPlanType.FREE,
            isPremium = false,
        )
        coEvery { restorePurchasesUseCase() } returns MiraiLinkResult.Success(freeInfo)

        viewModel.restorePurchases()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isRestoring)
        assertFalse(viewModel.uiState.value.isSuccess)
        assertEquals(R.string.subscription_restore_empty_message, viewModel.uiState.value.messageResId)
    }
}
