package com.feryaeljustice.mirailink.ui.util

import android.content.Context
import android.content.res.Resources
import com.feryaeljustice.mirailink.R
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class InterestImageFallbackTest {

    @Test
    fun getFallbackDrawableRes_whenGokuExists_returnsGokuResId() {
        val mockContext = mockk<Context>()
        val mockResources = mockk<Resources>()
        val gokuDummyId = 12345

        every { mockContext.packageName } returns "com.feryaeljustice.mirailink"
        every { mockContext.resources } returns mockResources
        every { mockResources.getIdentifier("goku", "drawable", "com.feryaeljustice.mirailink") } returns gokuDummyId

        val result = InterestImageFallback.getFallbackDrawableRes(mockContext)

        assertEquals(gokuDummyId, result)
    }

    @Test
    fun getFallbackDrawableRes_whenGokuDoesNotExist_returnsMiraiLinkLogo() {
        val mockContext = mockk<Context>()
        val mockResources = mockk<Resources>()

        every { mockContext.packageName } returns "com.feryaeljustice.mirailink"
        every { mockContext.resources } returns mockResources
        every { mockResources.getIdentifier("goku", "drawable", "com.feryaeljustice.mirailink") } returns 0

        val result = InterestImageFallback.getFallbackDrawableRes(mockContext)

        assertEquals(R.drawable.logomirailink, result)
    }

    @Test
    fun getFallbackDrawableRes_whenExceptionThrown_returnsMiraiLinkLogo() {
        val mockContext = mockk<Context>()

        every { mockContext.packageName } throws RuntimeException("Resources unavailable")

        val result = InterestImageFallback.getFallbackDrawableRes(mockContext)

        assertEquals(R.drawable.logomirailink, result)
    }
}
