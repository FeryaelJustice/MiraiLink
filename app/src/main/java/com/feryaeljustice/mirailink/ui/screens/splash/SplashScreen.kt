package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onNavigateToAuth: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.hideBars()
        sessionViewModel.disableBars()
    }

    when (uiState) {
        SplashScreenViewModel.SplashUiState.NavigateToAuth -> {
            onNavigateToAuth()
        }

        SplashScreenViewModel.SplashUiState.NavigateToHome -> {
            onNavigateToHome()
        }

        is SplashScreenViewModel.SplashUiState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}