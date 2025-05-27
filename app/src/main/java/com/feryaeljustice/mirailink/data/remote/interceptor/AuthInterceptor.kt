package com.feryaeljustice.mirailink.data.remote.interceptor

import android.util.Log
import com.feryaeljustice.mirailink.core.JwtUtils
import com.feryaeljustice.mirailink.core.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionManager.getToken() }
        Log.d("AuthInterceptor", "Token: $token")
        var userId: String? = null

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
                userId = JwtUtils.extractUserId(token)
            }
        }.build()

        val response = chain.proceed(request)

        when (response.code) {
            403 -> {
                userId?.let {
                    runBlocking {
                        sessionManager.notifyNeedsToBeVerified(it)
                    }
                }
            }

            401, 404 -> {
                if (!token.isNullOrEmpty()) {
                    runBlocking {
                        sessionManager.notifyLogout()
                    }
                }
            }
        }

        return response
    }
}
