package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.domain.error.AuthError
import com.feryaeljustice.mirailink.domain.error.DataError
import com.google.common.truth.Truth.assertThat
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class NetworkErrorMapperTest {
    @Test
    fun `connection failures are classified without exposing their message`() {
        assertThat(NetworkErrorMapper.map(UnknownHostException("secret host")))
            .isEqualTo(DataError.Network.NO_CONNECTION)
        assertThat(NetworkErrorMapper.map(SocketTimeoutException("technical timeout")))
            .isEqualTo(DataError.Network.TIMEOUT)
        assertThat(NetworkErrorMapper.map(SerializationException("raw payload")))
            .isEqualTo(DataError.Network.SERIALIZATION)
    }

    @Test
    fun `http status codes map to stable network categories`() {
        val expected =
            mapOf(
                400 to DataError.Network.BAD_REQUEST,
                403 to DataError.Network.FORBIDDEN,
                404 to DataError.Network.NOT_FOUND,
                408 to DataError.Network.TIMEOUT,
                409 to DataError.Network.CONFLICT,
                413 to DataError.Network.PAYLOAD_TOO_LARGE,
                429 to DataError.Network.RATE_LIMITED,
                500 to DataError.Network.SERVER,
                503 to DataError.Network.SERVICE_UNAVAILABLE,
            )

        expected.forEach { (status, error) ->
            assertThat(NetworkErrorMapper.map(httpException(status), NetworkOperation.PUBLIC))
                .isEqualTo(error)
        }
    }

    @Test
    fun `login unauthorized maps to invalid credentials`() {
        val error = NetworkErrorMapper.map(httpException(401), NetworkOperation.LOGIN)

        assertThat(error).isEqualTo(AuthError.INVALID_CREDENTIALS)
    }

    @Test
    fun `authenticated unauthorized maps to expired session`() {
        val error = NetworkErrorMapper.map(httpException(401), NetworkOperation.AUTHENTICATED)

        assertThat(error).isEqualTo(AuthError.SESSION_EXPIRED)
    }

    @Test
    fun `stable server code has priority over generic bad request`() {
        val error =
            NetworkErrorMapper.map(
                httpException(400, """{"code":"INVALID_TWO_FACTOR_CODE","message":"ignored"}"""),
                NetworkOperation.TWO_FACTOR,
            )

        assertThat(error).isEqualTo(AuthError.INVALID_TWO_FACTOR_CODE)
    }

    @Test
    fun `known legacy verification message is parsed only inside data`() {
        val error =
            NetworkErrorMapper.map(
                httpException(400, """{"message":"Código de verificación inválido"}"""),
                NetworkOperation.VERIFICATION,
            )

        assertThat(error).isEqualTo(AuthError.INVALID_VERIFICATION_CODE)
    }

    @Test
    fun `unknown or malformed backend text falls back to status category`() {
        val unknownText =
            NetworkErrorMapper.map(
                httpException(400, """{"message":"internal SQL detail"}"""),
                NetworkOperation.LOGIN,
            )
        val malformed =
            NetworkErrorMapper.map(
                httpException(500, "not-json"),
                NetworkOperation.AUTHENTICATED,
            )

        assertThat(unknownText).isEqualTo(DataError.Network.BAD_REQUEST)
        assertThat(malformed).isEqualTo(DataError.Network.SERVER)
    }

    @Test
    fun `chat id can be recovered without carrying backend message`() {
        val exception =
            httpException(
                409,
                """{"message":"already exists","chatId":"chat-123"}""",
            )

        assertThat(NetworkErrorMapper.existingChatId(exception)).isEqualTo("chat-123")
        assertThat(NetworkErrorMapper.existingChatId(httpException(409, "{}"))).isNull()
    }

    private fun httpException(
        status: Int,
        body: String = "{}",
    ): HttpException {
        val response =
            Response.error<Unit>(
                status,
                body.toResponseBody("application/json".toMediaType()),
            )
        return HttpException(response)
    }
}
