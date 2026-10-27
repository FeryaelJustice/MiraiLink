package com.feryaeljustice.mirailink.data.remote.interceptor

import com.feryaeljustice.mirailink.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import okhttp3.Interceptor
import okhttp3.Response

data class DebugSecurityLink(val url: String, val mailDelivered: Boolean)

object DebugSecurityLinks {
    private val channel = Channel<DebugSecurityLink>(capacity = Channel.BUFFERED)
    val events = channel.receiveAsFlow()
    internal fun emit(link: DebugSecurityLink) { channel.trySend(link) }
}

class DebugSecurityLinkInterceptor(
    private val enabled: Boolean = BuildConfig.DEBUG,
    private val key: String = BuildConfig.DEBUG_SECURITY_KEY,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val paths = setOf("/api/auth/register", "/api/auth/verification/request", "/api/auth/password/request-reset")
        if (!enabled || key.length < 32 || original.url.scheme != "https" || original.url.encodedPath !in paths) {
            return chain.proceed(original)
        }
        val response = chain.proceed(original.newBuilder().header("X-MiraiLink-Debug-Key", key).build())
        if (response.isSuccessful || response.code == 503) {
            val link = runCatching {
                val json = Json.parseToJsonElement(response.peekBody(16_384).string()).jsonObject
                val url = json["debugLink"]?.jsonPrimitive?.content ?: return@runCatching null
                DebugSecurityLink(url, json["debugMailDelivered"]?.jsonPrimitive?.booleanOrNull == true)
            }.getOrNull()
            if (link != null) DebugSecurityLinks.emit(link)
        }
        return response
    }
}
