package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.data.model.response.generic.ApiErrorResponse
import com.feryaeljustice.mirailink.domain.error.AppError
import com.feryaeljustice.mirailink.domain.error.AuthError
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.error.LocationError
import com.feryaeljustice.mirailink.domain.error.SubscriptionError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException

/** Convierte fallos de transporte y códigos backend aprobados en valores estables [AppError]. */
object NetworkErrorMapper {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    /** Clasifica [throwable] según [operation] sin devolver el texto libre del servidor. */
    fun map(
        throwable: Throwable,
        operation: NetworkOperation = NetworkOperation.PUBLIC,
    ): AppError =
        when (throwable) {
            is HttpException -> mapHttpException(throwable, operation)
            is SocketTimeoutException -> DataError.Network.TIMEOUT
            is UnknownHostException,
            is ConnectException,
            is IOException,
            -> DataError.Network.NO_CONNECTION

            is SerializationException -> DataError.Network.SERIALIZATION
            else -> UnknownError
        }

    /** Obtiene un chat ID no vacío del payload de conflicto, o null si no está disponible. */
    fun existingChatId(exception: HttpException): String? =
        parsePayload(exception)
            ?.chatId
            ?.takeIf(String::isNotBlank)

    /** Prioriza códigos aprobados del payload; después clasifica el status HTTP según el endpoint. */
    private fun mapHttpException(
        exception: HttpException,
        operation: NetworkOperation,
    ): AppError {
        mapKnownPayload(parsePayload(exception), operation)?.let { return it }

        return when (exception.code()) {
            400, 422 -> DataError.Network.BAD_REQUEST
            401 ->
                if (operation == NetworkOperation.LOGIN || operation == NetworkOperation.REGISTER) {
                    AuthError.INVALID_CREDENTIALS
                } else {
                    AuthError.SESSION_EXPIRED
                }

            403 -> DataError.Network.FORBIDDEN
            404 -> DataError.Network.NOT_FOUND
            408 -> DataError.Network.TIMEOUT
            409 -> DataError.Network.CONFLICT
            413 -> DataError.Network.PAYLOAD_TOO_LARGE
            429 -> DataError.Network.RATE_LIMITED
            503 -> DataError.Network.SERVICE_UNAVAILABLE
            in 500..599 -> DataError.Network.SERVER
            else -> DataError.Network.UNKNOWN
        }
    }

    /** Lee solo campos permitidos y devuelve null para bodies ausentes, malformados o no soportados. */
    private fun parsePayload(exception: HttpException): ApiErrorResponse? {
        val body = exception.response()?.errorBody()?.string()?.takeIf(String::isNotBlank)
            ?: return null
        return try {
            json.decodeFromString<ApiErrorResponse>(body)
        } catch (_: SerializationException) {
            null
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    /** Interpreta códigos backend estables y listas cerradas de mensajes legacy. */
    private fun mapKnownPayload(
        payload: ApiErrorResponse?,
        operation: NetworkOperation,
    ): AppError? {
        if (payload == null) return null

        val stableCode =
            sequenceOf(payload.code, payload.error)
                .filterNotNull()
                .map(::normalizeCode)
                .firstNotNullOfOrNull(::mapStableCode)
        if (stableCode != null) return stableCode

        val normalizedMessage = payload.message?.trim()?.lowercase(Locale.ROOT)
        return when {
            operation == NetworkOperation.LOGIN &&
                normalizedMessage in invalidCredentialMessages -> AuthError.INVALID_CREDENTIALS

            operation == NetworkOperation.VERIFICATION &&
                normalizedMessage in invalidVerificationMessages -> AuthError.INVALID_VERIFICATION_CODE

            operation == NetworkOperation.TWO_FACTOR &&
                normalizedMessage in invalidTwoFactorMessages -> AuthError.INVALID_TWO_FACTOR_CODE

            else -> null
        }
    }

    /** Normaliza a mayúsculas con underscore para interpretar códigos backend estables. */
    private fun normalizeCode(value: String): String =
        value
            .trim()
            .uppercase(Locale.ROOT)
            .replace('-', '_')
            .replace(' ', '_')

    /** Convierte un código backend canónico reconocido en error de autenticación. */
    private fun mapStableCode(code: String): AppError? =
        when (code) {
            "INVALID_CREDENTIALS",
            "AUTH_INVALID_CREDENTIALS",
            "INVALID_LOGIN",
            -> AuthError.INVALID_CREDENTIALS

            "SESSION_EXPIRED",
            "TOKEN_EXPIRED",
            "UNAUTHORIZED",
            -> AuthError.SESSION_EXPIRED

            "VERIFICATION_REQUIRED",
            "EMAIL_VERIFICATION_REQUIRED",
            -> AuthError.VERIFICATION_REQUIRED

            "INVALID_VERIFICATION_CODE" -> AuthError.INVALID_VERIFICATION_CODE
            "INVALID_TWO_FACTOR_CODE",
            "INVALID_2FA_CODE",
            "INVALID_OTP",
            -> AuthError.INVALID_TWO_FACTOR_CODE

            "LOCATION_REQUIRED" -> LocationError.LOCATION_REQUIRED
            "RESIDENCE_COUNTRY_REQUIRED" -> LocationError.RESIDENCE_COUNTRY_REQUIRED

            "DAILY_LIKES_LIMIT_REACHED" -> SubscriptionError.DAILY_LIKES_LIMIT_REACHED
            "DAILY_UNDO_LIMIT_REACHED" -> SubscriptionError.DAILY_UNDO_LIMIT_REACHED
            "PREMIUM_RADIUS_REQUIRED" -> SubscriptionError.PREMIUM_RADIUS_REQUIRED
            "PREMIUM_PASSPORT_REQUIRED" -> SubscriptionError.PREMIUM_PASSPORT_REQUIRED

            else -> null
        }

    private val invalidCredentialMessages =
        setOf(
            "invalid credentials",
            "email or password is incorrect",
            "credenciales inválidas",
            "correo o contraseña incorrectos",
        )

    private val invalidVerificationMessages =
        setOf(
            "invalid verification code",
            "código de verificación inválido",
        )

    private val invalidTwoFactorMessages =
        setOf(
            "invalid two factor code",
            "invalid 2fa code",
            "código de dos factores inválido",
        )
}
