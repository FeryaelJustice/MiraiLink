package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.error.DataError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MiraiLinkResultTest {
    @Test
    fun `error carries a typed app error`() {
        val result = MiraiLinkResult.Error(DataError.Network.NO_CONNECTION)

        assertThat(result.error).isEqualTo(DataError.Network.NO_CONNECTION)
    }

    @Test
    fun `map transforms successful data`() {
        val result = MiraiLinkResult.Success(4).map { it * 2 }

        assertThat(result).isEqualTo(MiraiLinkResult.Success(8))
    }

    @Test
    fun `map preserves typed error`() {
        val result =
            MiraiLinkResult.Error(DataError.Network.TIMEOUT)
                .map { value: Int -> value * 2 }

        assertThat(result).isEqualTo(MiraiLinkResult.Error(DataError.Network.TIMEOUT))
    }

    @Test
    fun `callbacks run only for their result branch`() {
        var successCalls = 0
        var errorCalls = 0

        MiraiLinkResult.Success("ok")
            .onSuccess { successCalls++ }
            .onError { errorCalls++ }

        MiraiLinkResult.Error(DataError.Network.SERVER)
            .onSuccess { successCalls++ }
            .onError { errorCalls++ }

        assertThat(successCalls).isEqualTo(1)
        assertThat(errorCalls).isEqualTo(1)
    }

    @Test
    fun `as empty result discards successful data`() {
        val result = MiraiLinkResult.Success("ignored").asEmptyResult()

        assertThat(result).isEqualTo(MiraiLinkResult.Success(Unit))
    }
}
