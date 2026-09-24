package com.feryaeljustice.mirailink.ui.screens.explore.feed

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.components.user.UserSwipeCardStack
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import com.feryaeljustice.mirailink.ui.utils.toast.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFeedScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    viewModel: CategoryFeedViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    val radiusKm by viewModel.radiusKm.collectAsStateWithLifecycle()
    val isSavingPreferences by viewModel.isSavingPreferences.collectAsStateWithLifecycle()
    val showSettingsSheet by viewModel.showSettingsSheet.collectAsStateWithLifecycle()
    val canUndo = viewModel.canUndo()

    val settingsUpdatedMessage = stringResource(R.string.category_settings_updated_toast, viewModel.categoryName)

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
    ) {
        // Dedicated Category Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            MiraiLinkText(
                text = viewModel.categoryName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
            )

            IconButton(onClick = { viewModel.openSettingsSheet() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter_list),
                    contentDescription = stringResource(R.string.content_description_category_settings),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        PullToRefreshBox(
            isRefreshing = state is CategoryFeedViewModel.CategoryFeedUiState.Loading,
            onRefresh = { viewModel.loadFeed() },
            modifier = Modifier
                .fillMaxSize()
                .testTag("CategoryFeedRefreshBox"),
        ) {
            AnimatedContent(
                targetState = state,
                modifier = Modifier.fillMaxSize(),
                transitionSpec = {
                    (fadeIn() + scaleIn(initialScale = 0.94f))
                        .togetherWith(fadeOut() + scaleOut(targetScale = 0.94f))
                },
                label = "CategoryFeedTransition",
            ) { currentState ->
                when (currentState) {
                    is CategoryFeedViewModel.CategoryFeedUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    is CategoryFeedViewModel.CategoryFeedUiState.Error -> {
                        MiraiLinkErrorContent(
                            error = currentState.error,
                            onAction = viewModel::performErrorAction,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    is CategoryFeedViewModel.CategoryFeedUiState.Empty -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_explore),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(64.dp),
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                MiraiLinkText(
                                    text = stringResource(R.string.explore_category_feed_empty_title),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                MiraiLinkText(
                                    text = stringResource(R.string.explore_category_feed_empty_desc),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                MiraiLinkButton(
                                    onClick = { viewModel.openSettingsSheet() },
                                ) {
                                    MiraiLinkText(
                                        text = stringResource(R.string.content_description_category_settings),
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                        }
                    }

                    is CategoryFeedViewModel.CategoryFeedUiState.Success -> {
                        val visibleUsers = currentState.visibleUsers.take(2)
                        val index = currentState.currentIndex

                        if (visibleUsers.isNotEmpty() && index < visibleUsers.size) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                UserSwipeCardStack(
                                    modifier = Modifier.testTag("swipeCategoryFeed"),
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
                                        text = stringResource(R.string.explore_category_feed_empty_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    MiraiLinkText(
                                        text = stringResource(R.string.explore_category_feed_empty_desc),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }

                    CategoryFeedViewModel.CategoryFeedUiState.Idle -> {
                        Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }

    if (showSettingsSheet) {
        CategoryDiscoverySettingsSheet(
            categoryName = viewModel.categoryName,
            initialRadiusKm = radiusKm,
            isSaving = isSavingPreferences,
            onDismiss = { viewModel.closeSettingsSheet() },
            onSaveRadius = { newRadius ->
                viewModel.updateRadius(newRadius) {
                    showToast(context, settingsUpdatedMessage, Toast.LENGTH_SHORT)
                }
            },
        )
    }
}
