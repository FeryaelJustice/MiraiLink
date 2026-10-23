package com.feryaeljustice.mirailink.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
class BillingClientManagerTest {
    @After
    fun tearDown() = unmockkStatic(BillingClient::class)

    @Test
    fun `missing Plus never launches Premium fallback`() = runTest {
        val client = client()
        val manager = BillingClientManager(context(), backgroundScope, { "user" })
        val premium = details("mirailink_premium", "monthly-autorenew")
        setFlow(manager, "_productDetailsMap", mapOf("mirailink_premium" to premium))
        setFlow(manager, "_productDetails", premium)

        val result = manager.launchBillingFlow(mockk(), "mirailink_plus", "monthly-autorenew")

        assertEquals(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE, result.responseCode)
        verify(exactly = 0) { client.launchBillingFlow(any(), any()) }
    }

    @Test
    fun `missing plan never launches another duration or spelling`() = runTest {
        val client = client()
        val manager = BillingClientManager(context(), backgroundScope, { "user" })
        setFlow(manager, "_productDetailsMap", mapOf("mirailink_premium" to details("mirailink_premium", "threee-month-autorenew")))

        for (plan in listOf("weekly-autorenew", "monthly-autorenew", "three-month-autorenew")) {
            val result = manager.launchBillingFlow(mockk(), "mirailink_premium", plan)
            assertEquals(BillingClient.BillingResponseCode.ITEM_UNAVAILABLE, result.responseCode)
        }
        verify(exactly = 0) { client.launchBillingFlow(any(), any()) }
    }

    @Test
    fun `available exact plan opens native billing`() = runTest {
        val client = client()
        val manager = BillingClientManager(context(), backgroundScope, { "user" })
        setFlow(manager, "_productDetailsMap", mapOf("mirailink_plus" to details("mirailink_plus", "weekly-autorenew")))
        every { client.launchBillingFlow(any(), any()) } returns BillingResult.newBuilder().setResponseCode(0).build()

        val result = manager.launchBillingFlow(mockk<Activity>(), "mirailink_plus", "weekly-autorenew")

        assertEquals(BillingClient.BillingResponseCode.OK, result.responseCode)
        verify(exactly = 1) { client.launchBillingFlow(any(), any()) }
    }

    private fun client(): BillingClient {
        mockkStatic(BillingClient::class)
        val client = mockk<BillingClient>(relaxed = true)
        val builder = mockk<BillingClient.Builder>(relaxed = true)
        every { BillingClient.newBuilder(any()) } returns builder
        every { builder.setListener(any()) } returns builder
        every { builder.enableAutoServiceReconnection() } returns builder
        every { builder.enablePendingPurchases(any()) } returns builder
        every { builder.build() } returns client
        every { client.isReady } returns false
        return client
    }

    private fun context(): Context = mockk<Context>().also { every { it.applicationContext } returns it }

    private fun details(product: String, plan: String): ProductDetails = mockk(relaxed = true) {
        every { productId } returns product
        every { productType } returns BillingClient.ProductType.SUBS
        every { oneTimePurchaseOfferDetails } returns null
        every { subscriptionOfferDetails } returns listOf(mockk<ProductDetails.SubscriptionOfferDetails> {
            every { basePlanId } returns plan
            every { offerToken } returns "offer-token"
            every { offerId } returns null
        })
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> setFlow(manager: BillingClientManager, name: String, value: T) {
        val field = BillingClientManager::class.java.getDeclaredField(name).apply { isAccessible = true }
        (field.get(manager) as MutableStateFlow<T>).value = value
    }
}
