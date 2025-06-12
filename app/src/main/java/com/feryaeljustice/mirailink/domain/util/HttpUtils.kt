package com.feryaeljustice.mirailink.domain.util

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

fun parseMiraiLinkHttpError(
    e: Throwable,
    logTag: String,
    logMsgOrigin: String = ""
): MiraiLinkResult<Nothing> {
    return if (e is HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        val json = JSONObject(errorBody ?: "{}")

        val message = json.optString("message", "Error desconocido")

        Log.e(
            logTag,
            if (logMsgOrigin.isNotBlank()) "$logMsgOrigin - Error HTTP: message -> $message" else "Error HTTP Body: message -> $message"
        )
        MiraiLinkResult.error(
            if (!message.isNullOrBlank()) message else "Error HTTP",
            e
        )
    } else {
        Log.e(logTag, logMsgOrigin.ifBlank { "Unknown error" }, e)
        MiraiLinkResult.error("$logTag: ${e.message}", e)
    }
}