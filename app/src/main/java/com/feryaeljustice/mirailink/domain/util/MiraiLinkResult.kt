package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.error.AppError

/** Result of an application operation with a typed [AppError] failure branch. */
sealed interface MiraiLinkResult<out T> {
    /** Successful operation containing [data]. */
    data class Success<out T>(val data: T) : MiraiLinkResult<T>

    /** Controlled failure containing no raw message or exception. */
    data class Error(val error: AppError) : MiraiLinkResult<Nothing>

    companion object {
        /** Construye un resultado correcto. */
        fun <T> success(data: T): MiraiLinkResult<T> = Success(data)

        /** Construye un resultado de error tipado. */
        fun error(error: AppError): MiraiLinkResult<Nothing> = Error(error)
    }
}

typealias EmptyResult = MiraiLinkResult<Unit>

/** Transforma datos correctos y conserva el error original. */
inline fun <T, R> MiraiLinkResult<T>.map(transform: (T) -> R): MiraiLinkResult<R> =
    when (this) {
        is MiraiLinkResult.Success -> MiraiLinkResult.Success(transform(data))
        is MiraiLinkResult.Error -> this
    }

/** Transforma solo la categoría de error y conserva los datos correctos. */
inline fun <T> MiraiLinkResult<T>.mapError(
    transform: (AppError) -> AppError,
): MiraiLinkResult<T> =
    when (this) {
        is MiraiLinkResult.Success -> this
        is MiraiLinkResult.Error -> MiraiLinkResult.Error(transform(error))
    }

/** Ejecuta [action] en caso de éxito y devuelve este resultado. */
inline fun <T> MiraiLinkResult<T>.onSuccess(action: (T) -> Unit): MiraiLinkResult<T> =
    apply {
        if (this is MiraiLinkResult.Success) action(data)
    }

/** Ejecuta [action] para un error tipado y devuelve este resultado. */
inline fun <T> MiraiLinkResult<T>.onError(action: (AppError) -> Unit): MiraiLinkResult<T> =
    apply {
        if (this is MiraiLinkResult.Error) action(error)
    }

/** Convierte los datos correctos a Unit y conserva los errores tipados. */
fun <T> MiraiLinkResult<T>.asEmptyResult(): EmptyResult = map { }
