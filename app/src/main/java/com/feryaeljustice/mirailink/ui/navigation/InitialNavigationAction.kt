package com.feryaeljustice.mirailink.ui.navigation

sealed class InitialNavigationAction {
    object GoToHome : InitialNavigationAction()
    object GoToAuth : InitialNavigationAction()
    object GoToOnboarding : InitialNavigationAction()
}