package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfilePicture: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        SplashScreenViewModel.SplashUiState.NavigateToAuth -> onNavigateToAuth()
        SplashScreenViewModel.SplashUiState.NavigateToHome -> onNavigateToHome()
        SplashScreenViewModel.SplashUiState.NavigateToProfilePicture -> onNavigateToProfilePicture()
        SplashScreenViewModel.SplashUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {}
    }
}