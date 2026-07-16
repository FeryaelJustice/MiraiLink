package com.feryaeljustice.mirailink.state

import com.feryaeljustice.mirailink.data.datastore.SessionManager
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.usecase.photos.CheckProfilePictureUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

/** State, command and profile-observation tests for the application session facade. */
@OptIn(ExperimentalCoroutinesApi::class)
class GlobalMiraiLinkSessionTest {
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val authenticated = MutableStateFlow(false)
    private val verified = MutableStateFlow(false)
    private val userId = MutableStateFlow<String?>(null)
    private val logout = MutableSharedFlow<Unit>()
    private val sessionManager =
        mockk<SessionManager> {
            every { isAuthenticatedFlow } returns authenticated
            every { isVerifiedFlow } returns verified
            every { userIdFlow } returns userId
            every { onLogout } returns logout
        }
    private val checkProfilePicture = mockk<CheckProfilePictureUseCase>()

    /** Verifies authentication, verification and user identity mirror SessionManager flows. */
    @Test
    fun `session exposes datastore state changes`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        val session = createSession()
        runCurrent()

        // When
        authenticated.value = true
        verified.value = true
        userId.value = ""
        runCurrent()

        // Then
        assertThat(session.currentAuth()).isTrue()
        assertThat(session.isVerified.value).isTrue()
        assertThat(session.currentUserId.value).isEmpty()
    }

    /** Verifies session commands delegate asynchronously to the persistence boundary. */
    @Test
    fun `session commands delegate to session manager`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        coEvery { sessionManager.saveSession(any(), any()) } returns Unit
        coEvery { sessionManager.saveIsVerified(any()) } returns Unit
        coEvery { sessionManager.clearSession() } returns Unit
        val session = createSession()

        // When
        session.saveSession(token = "token", userId = "42")
        session.saveIsVerified(true)
        session.clearSession()
        runCurrent()

        // Then
        coVerify(exactly = 1) { sessionManager.saveSession("token", "42") }
        coVerify(exactly = 1) { sessionManager.saveIsVerified(true) }
        coVerify(exactly = 1) { sessionManager.clearSession() }
    }

    /** Verifies all top and bottom bar mutations remain independent and reversible. */
    @Test
    fun `bar configuration commands update only requested properties`() =
        runTest(mainCoroutineRule.scheduler) {
            // Given
            val session = createSession()

            // When and then
            session.hideBars()
            assertThat(session.topBarConfig.value.showTopBar).isFalse()
            assertThat(session.topBarConfig.value.showBottomBar).isFalse()

            session.showBars()
            session.disableBars()
            assertThat(session.topBarConfig.value.showTopBar).isTrue()
            assertThat(session.topBarConfig.value.showBottomBar).isTrue()
            assertThat(session.topBarConfig.value.disableTopBar).isTrue()
            assertThat(session.topBarConfig.value.disableBottomBar).isTrue()

            session.enableBars()
            session.hideTopBarSettingsIcon()
            assertThat(session.topBarConfig.value.disableTopBar).isFalse()
            assertThat(session.topBarConfig.value.disableBottomBar).isFalse()
            assertThat(session.topBarConfig.value.showSettingsIcon).isFalse()

            session.showHideTopBar(false)
            session.showHideBottomBar(false)
            session.showTopBarSettingsIcon()
            assertThat(session.topBarConfig.value.showTopBar).isFalse()
            assertThat(session.topBarConfig.value.showBottomBar).isFalse()
            assertThat(session.topBarConfig.value.showSettingsIcon).isTrue()
        }

    /** Verifies an explicit refresh updates the cache only after a successful domain result. */
    @Test
    fun `refresh profile picture updates successful value and preserves it on error`() =
        runTest(mainCoroutineRule.scheduler) {
            // Given
            coEvery { checkProfilePicture("42") } returnsMany
                listOf(
                    MiraiLinkResult.Success(true),
                    MiraiLinkResult.Error(DataError.Network.NO_CONNECTION),
                )
            val session = createSession()

            // When
            session.refreshHasProfilePicture("42")
            session.refreshHasProfilePicture("42")

            // Then
            assertThat(session.hasProfilePicture.value).isTrue()
            coVerify(exactly = 2) { checkProfilePicture("42") }
        }

    /** Verifies a non-blank user starts profile-photo observation immediately. */
    @Test
    fun `user id starts profile picture observation`() = runTest(mainCoroutineRule.scheduler) {
        // Given
        coEvery { checkProfilePicture("42") } returns MiraiLinkResult.Success(true)
        val session = createSession()
        runCurrent()

        // When
        userId.value = "42"
        runCurrent()

        // Then
        assertThat(session.currentUserId.value).isEqualTo("42")
        assertThat(session.hasProfilePicture.value).isTrue()
        coVerify(exactly = 1) { checkProfilePicture("42") }

        session.stopObservingHasProfilePicture()
    }

    /** Creates the session with runTest backgroundScope so long-lived collectors are auto-cancelled. */
    private fun kotlinx.coroutines.test.TestScope.createSession() =
        GlobalMiraiLinkSession(
            sessionManager = sessionManager,
            checkProfilePictureUseCase = checkProfilePicture,
            appScope = backgroundScope,
        )
}
