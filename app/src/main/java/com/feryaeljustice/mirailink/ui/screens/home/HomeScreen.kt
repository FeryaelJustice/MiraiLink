package com.feryaeljustice.mirailink.ui.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack
import com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel.HomeUiState
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ParamsComparedByRef", "ktlint:standard:function-naming", "EffectKeys")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val state by viewModel.state.collectAsStateWithLifecycle()
    val canUndo = viewModel.canUndo()

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
    }

    PullToRefreshBox(
        isRefreshing = state is HomeUiState.Loading,
        onRefresh = {
            viewModel.loadUsers()
        },
        modifier =
            modifier
                .fillMaxSize()
                .testTag("HomeRefreshBox")
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
    ) {
        AnimatedContent(
            targetState = state,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                (fadeIn() + scaleIn(initialScale = 0.92f))
                    .togetherWith(fadeOut() + scaleOut(targetScale = 0.92f))
            },
            label = "HomeStateTransition",
        ) { currentState ->
            when (currentState) {
                is HomeUiState.Success -> {
                    val visibleUsers = currentState.visibleUsers.take(2)
                    val index = currentState.currentIndex

                    if (visibleUsers.isNotEmpty() && index < visibleUsers.size) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            UserSwipeCardStack(
                                modifier =
                                    Modifier
                                        .padding(16.dp)
                                        .testTag("swipeHome"),
                                users = visibleUsers,
                                canUndo = canUndo,
                                onSwipeLeft = { viewModel.swipeLeft() },
                                onGoBack = { viewModel.undoSwipe() },
                                onSwipeRight = { viewModel.swipeRight() },
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                MiraiLinkText(
                                    text = stringResource(R.string.users_empty_by_now),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                MiraiLinkText(
                                    text = stringResource(R.string.search_location_permission_needed_desc),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                is HomeUiState.Error -> {
                    MiraiLinkErrorContent(
                        error = currentState.error,
                        onAction = viewModel::performErrorAction,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                HomeUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                HomeUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
