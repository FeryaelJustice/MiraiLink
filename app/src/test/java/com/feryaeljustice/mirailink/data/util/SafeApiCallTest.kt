package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import java.io.IOException
import java.util.concurrent.CancellationException
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import org.junit.Assert.fail
import org.junit.Test

class SafeApiCallTest {
    @Test
    fun `successful call returns data`() =
        runTest {
            val result = safeApiCall { "value" }

            assertThat(result).isEqualTo(MiraiLinkResult.Success("value"))
        }

    @Test
    fun `io failure returns a typed network error`() =
        runTest {
            val result =
                safeApiCall<Unit> {
                    throw IOException("private network detail")
                }

            assertThat(result)
                .isEqualTo(MiraiLinkResult.Error(DataError.Network.NO_CONNECTION))
        }

    @Test
    fun `cancellation is never converted to an app error`() =
        runTest {
            try {
                safeApiCall<Unit> {
                    throw CancellationException("cancel")
                }
                fail("CancellationException must be rethrown")
            } catch (_: CancellationException) {
            }
        }

    @Test
    fun `unsuccessful retrofit response is classified instead of reported as success`() =
        runTest {
            val result =
                safeApiUnitResponse(NetworkOperation.AUTHENTICATED) {
                    Response.error(
                        503,
                        "{}".toResponseBody("application/json".toMediaType()),
                    )
                }

            assertThat(result)
                .isEqualTo(MiraiLinkResult.Error(DataError.Network.SERVICE_UNAVAILABLE))
        }
}
