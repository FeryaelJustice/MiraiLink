package com.feryaeljustice.mirailink.ui.screens.explore

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.explore.CategoryCarouselCard
import com.feryaeljustice.mirailink.ui.components.explore.CategoryGridCard
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    miraiLinkSession: GlobalMiraiLinkSession,
    onNavigateToCategoryFeed: (categoryId: String, categoryName: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = koinViewModel(),
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        miraiLinkSession.showBars()
        miraiLinkSession.enableBars()
        miraiLinkSession.showTopBarSettingsIcon()
    }

    LifecycleResumeEffect(Unit) {
        viewModel.loadExploreHub()
        onPauseOrDispose { }
    }

    PullToRefreshBox(
        isRefreshing = state is ExploreViewModel.ExploreUiState.Loading,
        onRefresh = { viewModel.loadExploreHub() },
        modifier =
            modifier
                .fillMaxSize()
                .testTag("ExploreRefreshBox")
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
                (fadeIn() + scaleIn(initialScale = 0.94f))
                    .togetherWith(fadeOut() + scaleOut(targetScale = 0.94f))
            },
            label = "ExploreStateTransition",
        ) { currentState ->
            when (currentState) {
                is ExploreViewModel.ExploreUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is ExploreViewModel.ExploreUiState.Error -> {
                    MiraiLinkErrorContent(
                        error = currentState.error,
                        onAction = viewModel::performErrorAction,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                is ExploreViewModel.ExploreUiState.Success -> {
                    val hubData = currentState.hubData
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp),
                    ) {
                        // Hub Header
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
                            ) {
                                MiraiLinkText(
                                    text = stringResource(R.string.explore_title),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                MiraiLinkText(
                                    text = stringResource(R.string.explore_subtitle),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        // Recommendations Carousel (Bumble Inspiration)
                        if (hubData.recommendations.isNotEmpty()) {
                            item {
                                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)) {
                                    MiraiLinkText(
                                        text = stringResource(R.string.explore_section_recommended),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    ) {
                                        items(hubData.recommendations, key = { it.id }) { category ->
                                            CategoryCarouselCard(
                                                category = category,
                                                onClick = { onNavigateToCategoryFeed(category.id, category.title) },
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Thematic Grid Sections (Tinder Inspiration)
                        hubData.sections.forEach { section ->
                            item {
                                MiraiLinkText(
                                    text = section.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                                )
                            }

                            val pairedCategories = section.categories.chunked(2)
                            items(pairedCategories) { pair ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    CategoryGridCard(
                                        category = pair[0],
                                        onClick = { onNavigateToCategoryFeed(pair[0].id, pair[0].title) },
                                        modifier = Modifier.weight(1f),
                                    )
                                    if (pair.size > 1) {
                                        CategoryGridCard(
                                            category = pair[1],
                                            onClick = { onNavigateToCategoryFeed(pair[1].id, pair[1].title) },
                                            modifier = Modifier.weight(1f),
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                ExploreViewModel.ExploreUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}
