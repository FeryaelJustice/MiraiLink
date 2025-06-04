package com.feryaeljustice.mirailink.data.remote.interceptor

import android.util.Log
import com.feryaeljustice.mirailink.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking { sessionManager.getCurrentToken() }
        Log.d("AuthInterceptor", "Token: $token")

        val request = chain.request().newBuilder().apply {
            if (!token.isNullOrEmpty()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        val response = chain.proceed(request)

        when (response.code) {
            403 -> {
                runBlocking {
                    sessionManager.saveIsVerified(false)
                }
            }

            401, 404 -> {
                // Token inválido o usuario no encontrado → Logout total
                if (!token.isNullOrBlank()) {
                    runBlocking {
                        sessionManager.clearSession()
                    }
                }
            }
        }

        return response
    }
}
