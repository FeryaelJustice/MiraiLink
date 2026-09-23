package com.feryaeljustice.mirailink.data.remote.interceptor

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class ImageDomainSecurityInterceptorTest {

    private val allowedDomains = "mirailink.xyz,cdn.myanimelist.net,media.rawg.io,images.igdb.com,10.0.2.2,localhost"
    private val interceptor = ImageDomainSecurityInterceptor(allowedDomains)

    private fun mockChain(url: String): Interceptor.Chain {
        val request = Request.Builder().url(url).build()
        val response = Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body("fake-body".toResponseBody())
            .build()

        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        return chain
    }

    @Test
    fun `allowed exact domain does not throw security exception on intercept`() {
        val chain = mockChain("https://cdn.myanimelist.net/images/anime/1015/138006.jpg")
        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
    }

    @Test
    fun `allowed subdomain is permitted`() {
        val chain = mockChain("https://api.mirailink.xyz/assets/img/profiles/test.webp")
        val response = interceptor.intercept(chain)

        assertEquals(200, response.code)
    }

    @Test
    fun `unauthorized external domain throws IOException`() {
        val chain = mockChain("https://malicious-external-site.com/avatar.png")
        try {
            interceptor.intercept(chain)
            fail("Expected IOException for unauthorized domain")
        } catch (e: IOException) {
            assertTrue(e.message?.contains("Dominio de imagen no autorizado") == true)
        }
    }

    @Test
    fun `subdomain of unauthorized domain is rejected`() {
        val chain = mockChain("https://evil.notmyanimelist.net/image.png")
        try {
            interceptor.intercept(chain)
            fail("Expected IOException for unauthorized domain")
        } catch (e: IOException) {
            assertTrue(e.message?.contains("Dominio de imagen no autorizado") == true)
        }
    }
}
