package com.feryaeljustice.mirailink.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack
import com.feryaeljustice.mirailink.ui.screens.home.HomeViewModel.HomeUiState
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, sessionViewModel: GlobalSessionViewModel) {
    val state by viewModel.state.collectAsState()
    val canUndo = viewModel.canUndo()

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.showTopBarSettingsIcon()
    }

    PullToRefreshBox(isRefreshing = state is HomeUiState.Loading, onRefresh = {
        viewModel.loadUsers()
    }, modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.displayCutout)
    ) {
        when (val currentState = state) {
            is HomeUiState.Success -> {
                val visibleUsers = currentState.visibleUsers.take(2)
                val index = currentState.currentIndex

                if (visibleUsers.isNotEmpty() && index < visibleUsers.size) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        UserSwipeCardStack(
                            modifier = Modifier.padding(16.dp),
                            users = visibleUsers,
                            canUndo = canUndo,
                            onSwipeLeft = { viewModel.swipeLeft() },
                            onGoBack = { viewModel.undoSwipe() },
                            onSwipeRight = { viewModel.swipeRight() },
                        )
                    }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        MiraiLinkText(text = stringResource(R.string.users_empty_by_now))
                    }
                }
            }

            is HomeUiState.Error -> {
                val error = state as HomeUiState.Error
                MiraiLinkText(
                    text = error.message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            HomeUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            HomeUiState.Idle -> Unit
        }
    }
}