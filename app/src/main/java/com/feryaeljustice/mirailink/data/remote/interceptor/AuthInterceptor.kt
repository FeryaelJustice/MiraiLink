package com.feryaeljustice.mirailink.data.remote.interceptor

import android.util.Log
import com.feryaeljustice.mirailink.data.local.SessionManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
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
        val rawBody = response.body
        val responseContent = rawBody.string()

        val isVerified = try {
            if (responseContent.trim()?.startsWith("{") == true) {
                val json = JSONObject(responseContent)
                json.optBoolean("verified", true)
            } else {
                true
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "JSON parse error: ${e.message}")
            true
        }

        val shouldLogout = try {
            if (responseContent.trim().startsWith("{")) {
                val json = JSONObject(responseContent)
                json.optBoolean("shouldLogout", true)
            } else {
                true
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "JSON parse error: ${e.message}")
            true
        }

        if (!isVerified) {
            runBlocking {
                sessionManager.saveIsVerified(false)
            }
        }

        when (response.code) {
            401, 404, 502 -> {
                if (shouldLogout && !token.isNullOrBlank()) {
                    runBlocking {
                        sessionManager.clearSession()
                    }
                }
            }
        }

        // Reconstruir el body para que Retrofit/OkHttp puedan leerlo luego al haber accedido
        val mediaType = rawBody.contentType()
        val newBody = responseContent?.toResponseBody(mediaType)

        return newBody?.let { response.newBuilder().body(newBody).build() } ?: response
    }
}
