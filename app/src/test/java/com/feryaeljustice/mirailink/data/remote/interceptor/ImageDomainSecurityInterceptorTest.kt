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

    private val allowedDomains = "mirailink.xyz,cdn.myanimelist.net,media.rawg.io,images.igdb.com,10.0.2.2,localhost,openstreetmap.org"
    private val interceptor = ImageDomainSecurityInterceptor(allowedDomains)

    @Test
    fun `configured image client permits Cloudinary delivery URLs`() {
        val configuredInterceptor = ImageDomainSecurityInterceptor()
        listOf("cloudinary.com", "res.cloudinary.com").forEach { host ->
            val response = configuredInterceptor.intercept(
                mockChain("https://$host/demo/image/upload/sample.jpg"),
            )
            assertEquals(200, response.code)
        }
    }

    @Test
    fun `configured image client rejects Cloudinary lookalike hosts`() {
        val configuredInterceptor = ImageDomainSecurityInterceptor()
        listOf("res.cloudinary.com.evil.example", "fakecloudinary.com").forEach { host ->
            try {
                configuredInterceptor.intercept(mockChain("https://$host/sample.jpg"))
                fail("Expected IOException for unauthorized domain")
            } catch (e: IOException) {
                assertTrue(e.message?.contains("Dominio de imagen no autorizado") == true)
            }
        }
    }

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
    fun `openstreetmap tile subdomain is permitted`() {
        val chain = mockChain("https://tile.openstreetmap.org/10/500/500.png")
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
