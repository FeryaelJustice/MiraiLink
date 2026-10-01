package com.feryaeljustice.mirailink.ui.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Decide si el gate de sesión debe sustituir la pila de navegación actual.
 *
 * Navigation 3 restaura la pila guardada antes de evaluar esta política.
 * Una sesión autenticada dentro del grafo principal conserva destino y ViewModel;
 * el flujo obligatorio de fotografía es la excepción.
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
