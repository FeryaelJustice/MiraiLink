package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    isAuthenticated: Boolean = false,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val authenticated by rememberUpdatedState(isAuthenticated)
    LaunchedEffect(authenticated) {
        when (authenticated) {
            true -> onNavigateToHome()
            false -> onNavigateToAuth()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}