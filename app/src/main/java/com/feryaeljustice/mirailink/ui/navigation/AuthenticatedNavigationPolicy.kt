package com.feryaeljustice.mirailink.ui.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Decides whether the session gate must replace the current navigation stack.
 *
 * A configuration change restores Navigation 3's saved back stack before this
 * policy runs. Therefore, an authenticated user already inside the main graph
 * must keep the restored destination and its ViewModel. The only exception is
 * the mandatory profile-picture flow.
 */
internal fun shouldResetAuthenticatedNavigation(
    currentTopLevel: NavKey,
    currentMainChild: NavKey?,
    hasProfilePicture: Boolean?,
): Boolean =
    when {
        currentTopLevel != ScreensSubgraphs.Main -> true
        hasProfilePicture == false -> currentMainChild != AppScreen.ProfilePictureScreen
        else -> currentMainChild == AppScreen.ProfilePictureScreen
    }
