package com.feryaeljustice.mirailink.data.remote.interceptor

import android.util.Log
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val globalMiraiLinkSessionProvider: (() -> GlobalMiraiLinkSession?)? = null,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getCurrentTokenSync()


        val request =
            chain
                .request()
                .newBuilder()
                .apply {
                    if (!token.isNullOrEmpty()) {
                        addHeader("Authorization", "Bearer $token")
                    }
                }.build()

        val response = chain.proceed(request)

        // Ignore session side effects from requests belonging to the previous account or mode.
        val ownsSession = token == sessionManager.getCurrentTokenSync() && token != "DEMO_TOKEN"
        val planHeader = response.header("X-Subscription-Plan")
        if (planHeader != null && ownsSession) {
            val normalizedPlan = planHeader.trim().lowercase()
            val isPremium = normalizedPlan == "premium"
            val isPlus = normalizedPlan == "plus" || isPremium
            globalMiraiLinkSessionProvider?.invoke()?.setSubscriptionState(premium = isPremium, plus = isPlus)
        }

        val rawBody = response.body
        val responseContent = rawBody.string()

        val isVerified = parseJsonBoolean(responseContent, "verified", defaultValue = true)

        val shouldLogout = parseJsonBoolean(responseContent, "shouldLogout", defaultValue = false)

        if (!isVerified && ownsSession && token != null) {
            sessionManager.updateVerificationForTokenSync(token, false) // Fire-and-forget
        }

        if (response.code in setOf(401, 404, 502) && shouldLogout && !token.isNullOrBlank() && ownsSession) {
            sessionManager.clearSessionForTokenSync(token) // Fire-and-forget
        }

        // Reconstruir el body para que Retrofit/OkHttp puedan leerlo luego al haber accedido
        val mediaType = rawBody.contentType()
        val newBody = responseContent.toResponseBody(mediaType)

        return newBody.let { response.newBuilder().body(newBody).build() }
    }

    private fun parseJsonBoolean(
        json: String,
        key: String,
        defaultValue: Boolean,
    ): Boolean =
        try {
            if (json.trim().startsWith("{")) {
                JSONObject(json).optBoolean(key, defaultValue)
            } else {
                defaultValue
            }
        } catch (e: Exception) {
            Log.e("AuthInterceptor", "JSON parse error: ${e.message}")
            defaultValue
        }
}
