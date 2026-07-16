package com.feryaeljustice.mirailink.data.util

import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.google.common.truth.Truth.assertThat
import java.io.FileNotFoundException
import java.util.concurrent.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.fail
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SafeLocalCallTest {
    @Test
    fun `local failures map to stable categories`() =
        runTest {
            val dispatcher = UnconfinedTestDispatcher(testScheduler)

            val missing =
                safeLocalCall<Unit>(dispatcher) {
                    throw FileNotFoundException()
                }
            val denied =
                safeLocalCall<Unit>(dispatcher) {
                    throw SecurityException()
                }
            val invalid =
                safeLocalCall<Unit>(dispatcher) {
                    throw InvalidMediaException()
                }

            assertThat(missing).isEqualTo(MiraiLinkResult.Error(DataError.Local.NOT_FOUND))
            assertThat(denied).isEqualTo(MiraiLinkResult.Error(DataError.Local.ACCESS_DENIED))
            assertThat(invalid).isEqualTo(MiraiLinkResult.Error(ValidationError.INVALID_MEDIA))
        }

    @Test
    fun `local cancellation is rethrown`() =
        runTest {
            val dispatcher = UnconfinedTestDispatcher(testScheduler)

            try {
                safeLocalCall<Unit>(dispatcher) {
                    throw CancellationException()
                }
                fail("CancellationException must be rethrown")
            } catch (_: CancellationException) {
            }
        }
}
