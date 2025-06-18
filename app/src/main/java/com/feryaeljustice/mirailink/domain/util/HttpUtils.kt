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
        val message = try {
            val json = JSONObject(errorBody ?: "{}")
            json.optString("message", "Error desconocido")
        } catch (ex: Exception) {
            Log.w(logTag, "No se pudo parsear errorBody como JSON: ${ex.message}")
            errorBody ?: "Error HTTP"
        }

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