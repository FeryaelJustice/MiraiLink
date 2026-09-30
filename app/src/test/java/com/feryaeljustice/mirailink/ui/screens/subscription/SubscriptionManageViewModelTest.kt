package com.feryaeljustice.mirailink.ui.screens.subscription

import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanInfo
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.domain.usecase.subscription.CancelSubscriptionIntentUseCase
import com.feryaeljustice.mirailink.domain.usecase.subscription.GetSubscriptionStatusUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionManageViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val getSubscriptionStatusUseCase: GetSubscriptionStatusUseCase = mockk()
    private val cancelSubscriptionIntentUseCase: CancelSubscriptionIntentUseCase = mockk()

    private val initialInfo = SubscriptionPlanInfo(
        planType = SubscriptionPlanType.PREMIUM,
        isPremium = true,
        status = "active",
        expiresAt = "2026-10-30T12:00:00Z",
    )
    private val subscriptionInfoFlow = MutableStateFlow(initialInfo)

    private lateinit var viewModel: SubscriptionManageViewModel

    @Before
    fun setUp() {
        every { getSubscriptionStatusUseCase.subscriptionInfo } returns subscriptionInfoFlow
        coEvery { getSubscriptionStatusUseCase() } returns MiraiLinkResult.Success(initialInfo)

        viewModel = SubscriptionManageViewModel(
            getSubscriptionStatusUseCase = getSubscriptionStatusUseCase,
            cancelSubscriptionIntentUseCase = cancelSubscriptionIntentUseCase,
        )
    }

    @Test
    fun `initialization loads status and updates state`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.subscriptionInfo.isPremium)
        assertEquals("active", viewModel.uiState.value.subscriptionInfo.status)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `refreshStatus failure updates error state`() = runTest {
        coEvery { getSubscriptionStatusUseCase() } returns MiraiLinkResult.Error(DataError.Network.NO_CONNECTION)

        viewModel.refreshStatus()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isLoading)
        assertNotNull(viewModel.uiState.value.error)
    }

    @Test
    fun `requestCancelIntent success sets cancelPlayStoreUrl`() = runTest {
        val playStoreUrl = "https://play.google.com/store/account/subscriptions?package=com.feryaeljustice.mirailink"
        coEvery { cancelSubscriptionIntentUseCase() } returns MiraiLinkResult.Success(playStoreUrl)

        viewModel.requestCancelIntent()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCanceling)
        assertEquals(playStoreUrl, viewModel.uiState.value.cancelPlayStoreUrl)

        viewModel.clearCancelUrl()
        assertNull(viewModel.uiState.value.cancelPlayStoreUrl)
    }

    @Test
    fun `requestCancelIntent failure sets error`() = runTest {
        coEvery { cancelSubscriptionIntentUseCase() } returns MiraiLinkResult.Error(DataError.Network.SERVER)

        viewModel.requestCancelIntent()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.isCanceling)
        assertNotNull(viewModel.uiState.value.error)
    }
}
