package com.feryaeljustice.mirailink.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.appconfig.UpdateGate
import com.feryaeljustice.mirailink.ui.navigation.InitialNavigationAction
import com.feryaeljustice.mirailink.ui.utils.extensions.openPlayStore
import org.koin.androidx.compose.koinViewModel

@Suppress("EffectKeys", "ktlint:standard:function-naming", "ParamsComparedByRef")
@Composable
fun SplashScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    onInitialNavigation: (InitialNavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val updateDiagInfo by viewModel.updateDiagInfo.collectAsStateWithLifecycle()
    val showUpdateDialog by remember(updateDiagInfo) {
        derivedStateOf { updateDiagInfo != null && (updateDiagInfo?.mustUpdate == true || updateDiagInfo?.shouldUpdate == true) }
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        miraiLinkSession.hideBars()
        miraiLinkSession.disableBars()
    }

    // 1. Chequeo: si hay bloqueador de versión, mostramos ForceUpdateGate y no navegamos
    if (showUpdateDialog) {
        UpdateGate(
            modifier = modifier,
            message = updateDiagInfo?.message,
            force = updateDiagInfo?.mustUpdate == true && updateDiagInfo?.shouldUpdate == false,
            onDismiss = viewModel::onDismissUpdateGate,
            onOpenStore = {
                // Abre Play Store
                val playStoreUrl =
                    "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
                context.openPlayStore(playStoreUrl)
            },
        )
        return // Salimos para no seguir evaluando navegación
    }

    // 2. Flujo normal de splash
    when (val stateUi = uiState) {
        is SplashScreenViewModel.SplashUiState.Navigate -> {
            onInitialNavigation(stateUi.action)
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
