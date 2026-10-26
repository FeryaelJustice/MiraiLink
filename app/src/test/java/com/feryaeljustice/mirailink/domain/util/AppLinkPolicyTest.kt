package com.feryaeljustice.mirailink.domain.util

import android.net.Uri
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppLinkPolicyTest {
    @Test
    fun `accepts Render verification and reset and legacy custom scheme`() {
        listOf(
            "https://mirailink-backend.onrender.com/verify?token=123456&userId=user-id",
            "https://mirailink-backend.onrender.com/reset-password?token=123456&email=a%2Bb%40example.com",
            "mirailink://verify?token=123456",
            "mirailink://reset-password?token=123456&email=a%40example.com",
            "https://mirailink.xyz/verification?token=123456",
        ).forEach { assertTrue(it, AppLinkPolicy.accepts(Uri.parse(it))) }
    }

    @Test
    fun `rejects untrusted or ambiguous links`() {
        listOf(
            "https://evil.test/verify?token=123456",
            "http://mirailink-backend.onrender.com/verify?token=123456",
            "https://mirailink-backend.onrender.com/api/auth/2fa/disable",
            "https://mirailink-backend.onrender.com/verify/extra?token=123456",
            "https://mirailink-backend.onrender.com/verify?token=123456&token=654321",
            "https://mirailink-backend.onrender.com/verify?redirect=evil",
            "https://mirailink-backend.onrender.com/verify?token=123456&userId=a&user_id=b",
            "https://mirailink-backend.onrender.com/verify?token=%3Cscript%3E",
            "https://user@mirailink-backend.onrender.com/verify?token=123456",
            "mirailink://verify/extra?token=123456",
            "mirailink://2fa?token=123456",
        ).forEach { assertFalse(it, AppLinkPolicy.accepts(Uri.parse(it))) }
    }
}
