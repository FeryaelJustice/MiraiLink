package com.feryaeljustice.mirailink.domain.util

import android.net.Uri

object AppLinkPolicy {
    private val hosts = setOf("mirailink-backend.onrender.com", "mirailink.xyz", "www.mirailink.xyz", "mirailink.com", "www.mirailink.com")
    private val verification = setOf("verify", "verification")
    private val recovery = setOf("reset-password", "recover_password", "recover-password")

    fun accepts(uri: Uri): Boolean = runCatching {
        if (!uri.isHierarchical || uri.userInfo != null || uri.port != -1 || uri.fragment != null) return false
        val custom = uri.scheme == "mirailink"
        if (!custom && (uri.scheme != "https" || uri.host !in hosts)) return false
        val destination = if (custom) uri.host else uri.pathSegments.firstOrNull()
        if (destination in verification || destination in recovery) {
            if (custom && uri.pathSegments.isNotEmpty()) return false
            if (!custom && uri.pathSegments.size != 1) return false
            val allowed = if (destination in verification) setOf("token", "userId", "user_id") else setOf("token", "email")
            if (uri.queryParameterNames.any { it !in allowed || uri.getQueryParameters(it).size != 1 }) return false
            if (uri.queryParameterNames.any { name -> uri.getQueryParameter(name).orEmpty().let { it.length > 254 || it.any { char -> char.code < 32 } } }) return false
            if (uri.getQueryParameter("userId") != null && uri.getQueryParameter("user_id") != null) return false
            val token = uri.getQueryParameter("token")
            return token == null || token.matches(Regex("[A-Za-z0-9_-]{6,64}"))
        }
        destination in setOf("chat", "affinities", "requests", "user", "login")
    }.getOrDefault(false)
}
