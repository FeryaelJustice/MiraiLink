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
import androidx.compose.ui.platform.LocalContext
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.ui.components.appconfig.ForceUpdateGate
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel
import com.feryaeljustice.mirailink.ui.utils.extensions.openPlayStore

@Composable
fun SplashScreen(
    viewModel: SplashScreenViewModel,
    sessionViewModel: GlobalSessionViewModel,
    onInitialNavigation: (InitialNavigationAction) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateBlocker by viewModel.updateBlocker.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        sessionViewModel.hideBars()
        sessionViewModel.disableBars()
    }

    // 1. Chequeo: si hay bloqueador de versión, mostramos ForceUpdateGate y no navegamos
    if (updateBlocker != null) {
        ForceUpdateGate(
            result = updateBlocker!!,
            onOpenStore = {
                // Abre Play Store
                val playStoreUrl =
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                context.openPlayStore(playStoreUrl)
            }
        )
        return // Salimos para no seguir evaluando navegación
    }

    // 2. Flujo normal de splash
    when (uiState) {
        is SplashScreenViewModel.SplashUiState.Navigate -> {
            onInitialNavigation((uiState as SplashScreenViewModel.SplashUiState.Navigate).action)
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