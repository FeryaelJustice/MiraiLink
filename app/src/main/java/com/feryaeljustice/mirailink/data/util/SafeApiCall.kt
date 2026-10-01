package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.util.concurrent.CancellationException
import retrofit2.HttpException
import retrofit2.Response

/** Ejecuta la llamada API, propaga cancelación y devuelve el resultado clasificado. */
suspend inline fun <T> safeApiCall(
    operation: NetworkOperation = NetworkOperation.PUBLIC,
    crossinline call: suspend () -> T,
): MiraiLinkResult<T> =
    try {
        MiraiLinkResult.Success(call())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        MiraiLinkResult.Error(NetworkErrorMapper.map(throwable, operation))
    }

/** Valida el status HTTP de la Response Unit antes de comunicar éxito. */
suspend inline fun safeApiUnitResponse(
    operation: NetworkOperation = NetworkOperation.PUBLIC,
    crossinline call: suspend () -> Response<Unit>,
): MiraiLinkResult<Unit> =
    try {
        val response = call()
        if (response.isSuccessful) {
            MiraiLinkResult.Success(Unit)
        } else {
            MiraiLinkResult.Error(
                NetworkErrorMapper.map(HttpException(response), operation),
            )
        }
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (throwable: Throwable) {
        MiraiLinkResult.Error(NetworkErrorMapper.map(throwable, operation))
    }

/** Recupera un valor aprobado ante un error HTTP y clasifica los fallos sin recuperación. */
suspend inline fun <T> safeApiCallRecoveringHttp(
    operation: NetworkOperation,
    crossinline recover: (HttpException) -> T?,
    crossinline call: suspend () -> T,
): MiraiLinkResult<T> =
    try {
        MiraiLinkResult.Success(call())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (httpException: HttpException) {
        recover(httpException)
            ?.let { MiraiLinkResult.Success(it) }
            ?: MiraiLinkResult.Error(
                NetworkErrorMapper.map(httpException, operation),
            )
    } catch (throwable: Throwable) {
        MiraiLinkResult.Error(NetworkErrorMapper.map(throwable, operation))
    }
