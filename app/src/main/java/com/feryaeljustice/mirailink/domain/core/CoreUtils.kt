package com.feryaeljustice.mirailink.domain.core

import android.util.Base64
import android.util.Log
import org.json.JSONObject

/*@Composable
fun rememberInitializedStateFlow(
    isInitializedFlow: StateFlow<Boolean>,
    timeoutMillis: Long = 5000L
): Boolean {
    val isInitialized by isInitializedFlow.collectAsState()
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(isInitialized) {
        if (isInitialized) {
            show = true
        } else {
            delay(timeoutMillis) // previene quedarse colgado si falla
            show = true // fallback (opcional)
        }
    }

    return show
}*/

object JwtUtils {
    fun extractUserId(token: String): String? {
        return try {
            val payload = token.split(".").getOrNull(1)
                ?: return null

            val decoded = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP))
            val json = JSONObject(decoded)

            json.optString("id")
        } catch (e: Throwable) {
            Log.e("extractUserId", "Error extracting user ID from token", e)
            null
        }
    }
}
