package com.feryaeljustice.mirailink.domain.util

sealed class MiraiLinkResult<out T> {
    data class Success<out T>(val data: T): MiraiLinkResult<T>()
    data class Error(val message: String, val exception: Throwable? = null): MiraiLinkResult<Nothing>()

    companion object {
        fun <T> success(data: T): MiraiLinkResult<T> = Success(data)
        fun error(message: String, exception: Throwable? = null): MiraiLinkResult<Nothing> = Error(message, exception)
    }
}