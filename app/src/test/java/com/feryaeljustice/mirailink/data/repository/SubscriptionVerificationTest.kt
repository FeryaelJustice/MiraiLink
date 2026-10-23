package com.feryaeljustice.mirailink.data.repository

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.feryaeljustice.mirailink.data.billing.BillingClientManager
import com.feryaeljustice.mirailink.data.billing.BillingPurchaseEvent
import com.feryaeljustice.mirailink.data.model.response.subscription.SubscriptionStatusDto
import com.feryaeljustice.mirailink.data.remote.SubscriptionApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceTimeBy
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionVerificationTest {
    @Test
    fun `reconnection recovers tokens without visiting the paywall and stops after logout`() = runTest {
        val connected = MutableStateFlow(false)
        val user = MutableStateFlow<String?>("user")
        val authenticated = MutableStateFlow(true)
        val demo = MutableStateFlow(false)
        val session = mockk<GlobalMiraiLinkSession>(relaxed = true) {
            every { currentUserId } returns user
            every { isAuthenticated } returns authenticated
            every { isDemoMode } returns demo
        }
        val manager = manager(MutableSharedFlow())
        every { manager.isConnected } returns connected
        coEvery { manager.queryActivePurchases() } returns listOf(purchase())
        val api = mockk<SubscriptionApiService>()
        val status = SubscriptionStatusDto(isPremium = true, plan = "premium", status = "active")
        coEvery { api.verifySubscription(any()) } returns status
        coEvery { api.getSubscriptionStatus() } returns status
        SubscriptionRepositoryImpl(api, manager, backgroundScope, session)
        runCurrent()
        coVerify(exactly = 0) { api.verifySubscription(any()) }

        connected.value = true
        runCurrent()
        coVerify(exactly = 1) { api.verifySubscription(any()) }
        connected.value = false
        runCurrent()
        connected.value = true
        runCurrent()
        coVerify(exactly = 2) { api.verifySubscription(any()) }

        authenticated.value = false
        user.value = null
        runCurrent()
        advanceTimeBy(3_600_001)
        runCurrent()
        coVerify(exactly = 2) { api.verifySubscription(any()) }
    }

    @Test
    fun `automatic recovery retries failed server delivery`() = runTest {
        val session = mockk<GlobalMiraiLinkSession>(relaxed = true) {
            every { currentUserId } returns MutableStateFlow<String?>("user")
            every { isAuthenticated } returns MutableStateFlow(true)
            every { isDemoMode } returns MutableStateFlow(false)
        }
        val manager = manager(MutableSharedFlow())
        every { manager.isConnected } returns MutableStateFlow(true)
        coEvery { manager.queryActivePurchases() } returns listOf(purchase())
        val api = mockk<SubscriptionApiService>()
        val status = SubscriptionStatusDto(isPremium = true, plan = "premium", status = "active")
        coEvery { api.verifySubscription(any()) } throws IOException("offline") andThen status
        coEvery { api.getSubscriptionStatus() } returns status
        val repository = SubscriptionRepositoryImpl(api, manager, backgroundScope, session)
        runCurrent()
        assertFalse(repository.subscriptionInfo.value.isPremium)
        advanceTimeBy(60_001)
        runCurrent()
        coVerify(exactly = 2) { api.verifySubscription(any()) }
        assertTrue(repository.subscriptionInfo.value.isPremium)
    }

    @Test
    fun `restore sends every purchased token even when one verification fails`() = runTest {
        val manager = manager(MutableSharedFlow())
        val second = purchase("second-token")
        coEvery { manager.queryActivePurchases() } returns listOf(purchase(), second)
        val api = mockk<SubscriptionApiService>()
        coEvery { api.verifySubscription(match { it.purchaseToken == "test-token" }) } throws IOException("offline")
        coEvery { api.verifySubscription(match { it.purchaseToken == "second-token" }) } returns SubscriptionStatusDto()
        val repository = SubscriptionRepositoryImpl(api, manager, backgroundScope)
        val result = repository.restorePurchases()
        assertTrue(result is com.feryaeljustice.mirailink.domain.util.MiraiLinkResult.Error)
        coVerify(exactly = 1) { api.verifySubscription(match { it.purchaseToken == "second-token" }) }
    }

    @Test
    fun `Play payment with rejected verification emits failure and does not grant access`() = runTest {
        val events = MutableSharedFlow<BillingPurchaseEvent>()
        val manager = manager(events)
        val api = mockk<SubscriptionApiService>()
        coEvery { api.verifySubscription(any()) } throws IOException("Server unavailable")
        val repository = SubscriptionRepositoryImpl(api, manager, backgroundScope)
        val received = mutableListOf<BillingPurchaseEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.purchaseEvents.collect { received.add(it) }
        }
        runCurrent()

        events.emit(BillingPurchaseEvent.PurchaseSuccess(purchase()))
        runCurrent()

        assertTrue(received.single() is BillingPurchaseEvent.VerificationFailed)
        assertFalse(repository.subscriptionInfo.value.isPremium)
    }

    @Test
    fun `verified payment updates entitlement before forwarding success`() = runTest {
        val events = MutableSharedFlow<BillingPurchaseEvent>()
        val api = mockk<SubscriptionApiService>()
        coEvery { api.verifySubscription(any()) } returns SubscriptionStatusDto(isPremium = true, plan = "premium", status = "active")
        val repository = SubscriptionRepositoryImpl(api, manager(events), backgroundScope)
        val received = mutableListOf<BillingPurchaseEvent>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            repository.purchaseEvents.collect {
                assertTrue(repository.subscriptionInfo.value.isPremium)
                received.add(it)
            }
        }
        runCurrent()

        events.emit(BillingPurchaseEvent.PurchaseSuccess(purchase()))
        runCurrent()

        assertTrue(received.single() is BillingPurchaseEvent.PurchaseSuccess)
    }

    private fun manager(events: MutableSharedFlow<BillingPurchaseEvent>) = mockk<BillingClientManager> {
        every { purchaseEvents } returns events
        every { productDetails } returns MutableStateFlow<ProductDetails?>(null)
        every { productDetailsMap } returns MutableStateFlow(emptyMap())
    }

    private fun purchase(token: String = "test-token") = mockk<Purchase> {
        every { purchaseToken } returns token
        every { products } returns listOf("mirailink_premium")
        every { orderId } returns "test-order"
    }
}
