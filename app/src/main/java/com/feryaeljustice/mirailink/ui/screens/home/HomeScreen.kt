package com.feryaeljustice.mirailink.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.ui.components.SwipeCardStack
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
    }, modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is HomeUiState.Success -> {
                val visibleUsers = currentState.visibleUsers.take(2)
                val index = currentState.currentIndex

                if (visibleUsers.isNotEmpty() && index < visibleUsers.size) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        SwipeCardStack(
                            modifier = Modifier.padding(16.dp),
                            users = visibleUsers,
                            canUndo = canUndo,
                            onSwipeLeft = { viewModel.swipeLeft() },
                            onGoBack = {viewModel.undoSwipe()  },
                            onSwipeRight = { viewModel.swipeRight() },
                        )
                    }
                } else {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay más usuarios por ahora")
                    }
                }
            }

            is HomeUiState.Error -> {
                val error = state as HomeUiState.Error
                Text(
                    text = error.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
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