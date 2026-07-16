package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.error.AppError

/** Result of an application operation with a typed [AppError] failure branch. */
sealed interface MiraiLinkResult<out T> {
    /** Successful operation containing [data]. */
    data class Success<out T>(val data: T) : MiraiLinkResult<T>

    /** Controlled failure containing no raw message or exception. */
    data class Error(val error: AppError) : MiraiLinkResult<Nothing>

    companion object {
        /** Creates a successful result. */
        fun <T> success(data: T): MiraiLinkResult<T> = Success(data)

        /** Creates a typed failure result. */
        fun error(error: AppError): MiraiLinkResult<Nothing> = Error(error)
    }
}

typealias EmptyResult = MiraiLinkResult<Unit>

/** Transforms success data and preserves the original error unchanged. */
inline fun <T, R> MiraiLinkResult<T>.map(transform: (T) -> R): MiraiLinkResult<R> =
    when (this) {
        is MiraiLinkResult.Success -> MiraiLinkResult.Success(transform(data))
        is MiraiLinkResult.Error -> this
    }

/** Transforms only the failure category and preserves successful data. */
inline fun <T> MiraiLinkResult<T>.mapError(
    transform: (AppError) -> AppError,
): MiraiLinkResult<T> =
    when (this) {
        is MiraiLinkResult.Success -> this
        is MiraiLinkResult.Error -> MiraiLinkResult.Error(transform(error))
    }

/** Runs [action] for success and returns this result. */
inline fun <T> MiraiLinkResult<T>.onSuccess(action: (T) -> Unit): MiraiLinkResult<T> =
    apply {
        if (this is MiraiLinkResult.Success) action(data)
    }

/** Runs [action] for a typed failure and returns this result. */
inline fun <T> MiraiLinkResult<T>.onError(action: (AppError) -> Unit): MiraiLinkResult<T> =
    apply {
        if (this is MiraiLinkResult.Error) action(error)
    }

/** Converts successful data to Unit while preserving a typed error. */
fun <T> MiraiLinkResult<T>.asEmptyResult(): EmptyResult = map { }
