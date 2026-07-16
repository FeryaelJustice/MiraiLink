


package com.feryaeljustice.mirailink.ui.error

import com.feryaeljustice.mirailink.domain.error.UnknownError


import com.feryaeljustice.mirailink.R

import com.feryaeljustice.mirailink.domain.error.AuthError

import com.feryaeljustice.mirailink.domain.error.DataError

import com.google.common.truth.Truth.assertThat

import org.junit.Test

class AppErrorUiMapperTest {
    @Test
    fun `network failures provide a neutral message and retry action`() {
        val uiError = DataError.Network.NO_CONNECTION.toUiError()

        assertThat(uiError.message).isEqualTo(UiText.Resource(R.string.error_no_connection))
        assertThat(uiError.actionLabel).isEqualTo(UiText.Resource(R.string.action_retry))
        assertThat(uiError.recovery).isEqualTo(ErrorRecovery.RETRY)
    }

    @Test
    fun `invalid credentials never expose a server message`() {
        val uiError = AuthError.INVALID_CREDENTIALS.toUiError()

        assertThat(uiError.message).isEqualTo(UiText.Resource(R.string.error_invalid_credentials))
        assertThat(uiError.recovery).isEqualTo(ErrorRecovery.REVIEW_INPUT)
    }

    @Test
    fun `expired sessions request a new sign in instead of a blind retry`() {
        val uiError = AuthError.SESSION_EXPIRED.toUiError()

        assertThat(uiError.actionLabel).isEqualTo(UiText.Resource(R.string.action_sign_in_again))
        assertThat(uiError.recovery).isEqualTo(ErrorRecovery.SIGN_IN_AGAIN)
    }

    @Test
    fun `unknown failures remain actionable`() {
        val uiError = UnknownError.toUiError()

        assertThat(uiError.message).isEqualTo(UiText.Resource(R.string.error_unknown))
        assertThat(uiError.actionLabel).isEqualTo(UiText.Resource(R.string.action_retry))
    }
}
