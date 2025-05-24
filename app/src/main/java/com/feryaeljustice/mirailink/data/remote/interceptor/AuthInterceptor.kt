package com.feryaeljustice.mirailink.data.remote.interceptor

import android.util.Log
import com.feryaeljustice.mirailink.core.SessionManager
import com.feryaeljustice.mirailink.data.local.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { tokenManager.getToken() }
        Log.d("AuthInterceptor", "Token: $token")
        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        val response = chain.proceed(request)

        if (!token.isNullOrEmpty() && (response.code == 401 || response.code == 403)) {
            runBlocking {
                sessionManager.notifyLogout()
            }
        }

        return response
    }
}
