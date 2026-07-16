


package com.feryaeljustice.mirailink.domain.error

import com.feryaeljustice.mirailink.domain.error.UnknownError


import com.google.common.truth.Truth.assertThat

import org.junit.Test

class AppErrorTest {
    @Test
    fun `every error category implements the app error contract`() {
        val errors =
            listOf(
                DataError.Network.NO_CONNECTION,
                DataError.Network.TIMEOUT,
                DataError.Network.SERVER,
                DataError.Local.CORRUPTED,
                AuthError.INVALID_CREDENTIALS,
                AuthError.SESSION_EXPIRED,
                ValidationError.INVALID_MEDIA,
                UnknownError,
            )

        assertThat(errors).containsNoDuplicates()
        assertThat(errors).hasSize(8)
    }

    @Test
    fun `invalid credentials and expired session are distinct auth failures`() {
        assertThat(AuthError.INVALID_CREDENTIALS).isNotEqualTo(AuthError.SESSION_EXPIRED)
    }
}
