package com.feryaeljustice.mirailink.ui.navigation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthenticatedNavigationPolicyTest {
    @Test
    fun `enters main graph from splash for an authenticated session`() {
        assertTrue(
            shouldResetAuthenticatedNavigation(
                currentTopLevel = AppScreen.SplashScreen,
                currentMainChild = AppScreen.HomeScreen,
                hasProfilePicture = true,
            ),
        )
    }

    @Test
    fun `enforces profile picture flow when it is missing`() {
        assertTrue(
            shouldResetAuthenticatedNavigation(
                currentTopLevel = ScreensSubgraphs.Main,
                currentMainChild = AppScreen.ProfileScreen,
                hasProfilePicture = false,
            ),
        )
    }

    @Test
    fun `keeps restored profile screen when profile picture exists`() {
        assertFalse(
            shouldResetAuthenticatedNavigation(
                currentTopLevel = ScreensSubgraphs.Main,
                currentMainChild = AppScreen.ProfileScreen,
                hasProfilePicture = true,
            ),
        )
    }

    @Test
    fun `leaves mandatory profile picture screen once the picture is available`() {
        assertTrue(
            shouldResetAuthenticatedNavigation(
                currentTopLevel = ScreensSubgraphs.Main,
                currentMainChild = AppScreen.ProfilePictureScreen,
                hasProfilePicture = true,
            ),
        )
    }
}
