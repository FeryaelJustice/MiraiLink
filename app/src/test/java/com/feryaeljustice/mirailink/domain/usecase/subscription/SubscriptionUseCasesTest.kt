package com.feryaeljustice.mirailink.domain.usecase.subscription

import android.app.Activity
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.repository.SubscriptionRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubscriptionUseCasesTest {

    private val repository: SubscriptionRepository = mockk(relaxed = true)

    private val sampleInfo = SubscriptionPlanInfo(
        planType = SubscriptionPlanType.PREMIUM,
        isPremium = true,
        status = "active",
        productId = "mirailink_premium",
        formattedPrice = "0,99 €",
        autoRenewing = true,
    )

    @Before
    fun setup() {
        every { repository.subscriptionInfo } returns MutableStateFlow(sampleInfo)
        every { repository.formattedPrice } returns MutableStateFlow("0,99 €")
    }

    @Test
    fun getSubscriptionStatusUseCase_returns_success() = runTest {
        coEvery { repository.fetchSubscriptionStatus() } returns MiraiLinkResult.Success(sampleInfo)

        val useCase = GetSubscriptionStatusUseCase(repository)
        val result = useCase()

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(sampleInfo, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.fetchSubscriptionStatus() }
    }

    @Test
    fun launchBillingFlowUseCase_delegates_to_repository() {
        val activity = mockk<Activity>()
        every { repository.launchBillingFlow(activity, any(), any()) } returns MiraiLinkResult.Success(Unit)

        val useCase = LaunchBillingFlowUseCase(repository)
        val result = useCase(activity)

        assertTrue(result is MiraiLinkResult.Success)
        verify(exactly = 1) { repository.launchBillingFlow(activity, any(), any()) }
    }

    @Test
    fun restorePurchasesUseCase_returns_restored_info() = runTest {
        coEvery { repository.restorePurchases() } returns MiraiLinkResult.Success(sampleInfo)

        val useCase = RestorePurchasesUseCase(repository)
        val result = useCase()

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(true, (result as MiraiLinkResult.Success).data.isPremium)
        coVerify(exactly = 1) { repository.restorePurchases() }
    }

    @Test
    fun cancelSubscriptionIntentUseCase_returns_playStoreUrl() = runTest {
        val testUrl = "https://play.google.com/store/account/subscriptions"
        coEvery { repository.requestCancelIntent() } returns MiraiLinkResult.Success(testUrl)

        val useCase = CancelSubscriptionIntentUseCase(repository)
        val result = useCase()

        assertTrue(result is MiraiLinkResult.Success)
        assertEquals(testUrl, (result as MiraiLinkResult.Success).data)
        coVerify(exactly = 1) { repository.requestCancelIntent() }
    }

    @Test
    fun cancelSubscriptionIntentUseCase_handles_error() = runTest {
        coEvery { repository.requestCancelIntent() } returns MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE)

        val useCase = CancelSubscriptionIntentUseCase(repository)
        val result = useCase()

        assertTrue(result is MiraiLinkResult.Error)
        assertEquals(DataError.Network.SERVICE_UNAVAILABLE, (result as MiraiLinkResult.Error).error)
    }
}
