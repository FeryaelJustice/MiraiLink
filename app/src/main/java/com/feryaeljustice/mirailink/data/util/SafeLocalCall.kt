package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import java.io.FileNotFoundException
import java.io.IOException
import java.util.concurrent.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

/** Señala media ilegible en datos para convertirla en INVALID_MEDIA. */
internal class InvalidMediaException : IllegalArgumentException()

/** Ejecuta trabajo local bloqueante en [dispatcher], conserva cancelación y clasifica fallos locales. */
suspend fun <T> safeLocalCall(
    dispatcher: CoroutineDispatcher,
    call: () -> T,
): MiraiLinkResult<T> =
    withContext(dispatcher) {
        try {
            MiraiLinkResult.Success(call())
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (_: InvalidMediaException) {
            MiraiLinkResult.Error(ValidationError.INVALID_MEDIA)
        } catch (_: FileNotFoundException) {
            MiraiLinkResult.Error(DataError.Local.NOT_FOUND)
        } catch (_: SecurityException) {
            MiraiLinkResult.Error(DataError.Local.ACCESS_DENIED)
        } catch (_: SerializationException) {
            MiraiLinkResult.Error(DataError.Local.CORRUPTED)
        } catch (_: IOException) {
            MiraiLinkResult.Error(DataError.Local.UNKNOWN)
        } catch (_: Exception) {
            MiraiLinkResult.Error(DataError.Local.UNKNOWN)
        }
    }
