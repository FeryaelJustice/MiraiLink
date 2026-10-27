package com.feryaeljustice.mirailink.data.remote.interceptor

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DebugSecurityLinkInterceptorTest {
    private val key = "test-private-key-at-least-32-characters"

    @Test
    fun onlySelectedHttpsDebugRequestsReceiveTheKey() {
        for ((enabled, url) in listOf(
            false to "https://example.com/api/auth/register",
            true to "http://example.com/api/auth/register",
            true to "https://example.com/api/auth/login",
        )) {
            val request = Request.Builder().url(url).build()
            val chain = mockk<Interceptor.Chain>()
            val sent = slot<Request>()
            every { chain.request() } returns request
            every { chain.proceed(capture(sent)) } answers { response(sent.captured, 200, "{}") }
            DebugSecurityLinkInterceptor(enabled, key).intercept(chain)
            assertNull(sent.captured.header("X-MiraiLink-Debug-Key"))
        }
    }

    @Test
    fun mailFailureEmitsOneLinkAndPreservesResponse() = runTest {
        val request = Request.Builder().url("https://example.com/api/auth/password/request-reset").build()
        val chain = mockk<Interceptor.Chain>()
        val sent = slot<Request>()
        every { chain.request() } returns request
        every { chain.proceed(capture(sent)) } answers {
            response(sent.captured, 503, """{"debugLink":"https://example.com/reset-password?token=123456","debugMailDelivered":false}""")
        }
        val event = async { DebugSecurityLinks.events.first() }
        val result = DebugSecurityLinkInterceptor(true, key).intercept(chain)
        assertEquals(key, sent.captured.header("X-MiraiLink-Debug-Key"))
        assertEquals(503, result.code)
        assertEquals(false, event.await().mailDelivered)
        assertEquals(true, result.body.string().contains("debugLink"))
        assertNull(kotlinx.coroutines.withTimeoutOrNull(10) { DebugSecurityLinks.events.first() })
    }

    private fun response(request: Request, code: Int, body: String): Response = Response.Builder()
        .request(request).protocol(Protocol.HTTP_1_1).code(code).message("test")
        .body(body.toResponseBody("application/json".toMediaType())).build()
}
