package com.feryaeljustice.mirailink.data.time

import android.content.Context
import com.feryaeljustice.mirailink.data.remote.interceptor.ServerTimeInterceptor
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class TrustedTimeProviderTest {

    private lateinit var context: Context
    private lateinit var provider: TrustedTimeProviderImpl

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        provider = TrustedTimeProviderImpl(context)
    }

    @Test
    fun `currentTimeMillis returns valid epoch when server time is synchronized`() {
        val serverTime = 1775000000000L
        provider.syncWithServerTime(serverTime)

        assertTrue(provider.isTimeTrusted())
        val current = provider.currentTimeMillis()
        assertTrue("Expected current ($current) >= serverTime ($serverTime)", current >= serverTime)
    }

    @Test
    fun `ServerTimeInterceptor parses HTTP Date header and syncs provider`() {
        val mockProvider = mockk<TrustedTimeProvider>(relaxed = true)
        val interceptor = ServerTimeInterceptor(mockProvider)

        val targetDateStr = "Sun, 04 Oct 2026 12:00:00 GMT"
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        val expectedEpoch = format.parse(targetDateStr)!!.time

        val chain = mockk<Interceptor.Chain>()
        val request = Request.Builder().url("https://api.mirailink.com/api/test").build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .header("Date", targetDateStr)
            .body("{}".toResponseBody("application/json".toMediaType()))
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val resultResponse = interceptor.intercept(chain)

        assertEquals(200, resultResponse.code)
        val slot = slot<Long>()
        verify { mockProvider.syncWithServerTime(capture(slot)) }
        assertEquals(expectedEpoch, slot.captured)
    }
}
