package com.feryaeljustice.mirailink.ui.screens.profile

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
import com.feryaeljustice.mirailink.ui.components.UserCard
import com.feryaeljustice.mirailink.ui.screens.profile.ProfileViewModel.ProfileUiState
import com.feryaeljustice.mirailink.ui.state.GlobalSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, sessionViewModel: GlobalSessionViewModel) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        sessionViewModel.showBars()
        sessionViewModel.enableBars()
        sessionViewModel.showTopBarSettingsIcon()
    }

    PullToRefreshBox(isRefreshing = state is ProfileUiState.Loading, onRefresh = {
        viewModel.getCurrentUser()
    }, modifier = Modifier.fillMaxSize()) {
        when (state) {
            is ProfileUiState.Success -> {
                (state as ProfileUiState.Success).user?.let {
                    Box(modifier = Modifier.padding(16.dp)) {
                        UserCard(
                            user = it,
                            isPreviewMode = true,
                            onEdit = {},
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }
            }

            is ProfileUiState.Error -> {
                val error = state as ProfileUiState.Error
                Text(
                    text = "Error: ${error.message} ${error.exception?.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            ProfileUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            ProfileUiState.Idle -> Unit
        }
    }
}