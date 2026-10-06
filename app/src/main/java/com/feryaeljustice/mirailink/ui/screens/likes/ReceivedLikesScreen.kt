package com.feryaeljustice.mirailink.ui.screens.likes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import org.koin.compose.viewmodel.koinViewModel

@Suppress("ktlint:standard:function-naming")
@Composable
fun ReceivedLikesScreen(
    modifier: Modifier = Modifier,
    miraiLinkSession: GlobalMiraiLinkSession,
    onNavigateToUserDetail: (String) -> Unit,
    onNavigateToPaywall: () -> Unit = {},
    onNavigateToAffinityPaywall: () -> Unit = onNavigateToPaywall,
    viewModel: ReceivedLikesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val matchSuccessTemplate = stringResource(R.string.received_likes_match_success)

    LaunchedEffect(Unit) {
        viewModel.loadLikes()
    }

    LaunchedEffect(viewModel, matchSuccessTemplate) {
        viewModel.events.collect { event ->
            when (event) {
                is ReceivedLikesUiEvent.MatchCreated -> {
                    showToast(
                        context,
                        java.lang.String.format(matchSuccessTemplate, event.nickname),
                    )
                }
            }
        }
    }

    LazyColumn(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(vertical = 16.dp)) {
        item {
            com.feryaeljustice.mirailink.ui.screens.affinity.AffinitySection(onNavigateToUserDetail, onNavigateToAffinityPaywall)
        }
        item { androidx.compose.material3.HorizontalDivider() }
        item { MiraiLinkText(text = stringResource(R.string.received_likes_title), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
        when {
            uiState.isLoading -> item { CircularProgressIndicator() }
            uiState.error != null -> item { MiraiLinkErrorContent(error = uiState.error!!, onAction = { viewModel.loadLikes() }) }
            uiState.isPremiumLocked -> item { PremiumLockedState(onNavigateToPaywall = onNavigateToPaywall) }
            uiState.likes.isEmpty() -> item { EmptyLikesState() }
            else -> items(uiState.likes, key = { it.likeId }) { item -> ReceivedLikeItemCard(item = item, onClick = { onNavigateToUserDetail(item.username) }, onMatchClick = { viewModel.matchUser(item) }) }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun ReceivedLikeItemCard(
    modifier: Modifier = Modifier,
    item: ReceivedLikeItemViewEntry,
    onClick: () -> Unit,
    onMatchClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            com.feryaeljustice.mirailink.ui.components.media.CrystalPhoto(
                photoPresentation = item.photoPresentation,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.avatarUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.logomirailink)
                    .error(R.drawable.logomirailink)
                    .build(),
                contentDescription = item.nickname,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                val displayName = buildString {
                    append(item.nickname)
                    if (item.age != null) {
                        append(", ${item.age}")
                    }
                }
                MiraiLinkText(
                    text = displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                MiraiLinkText(
                    text = "@${item.username}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onMatchClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                MiraiLinkText(
                    text = stringResource(R.string.received_likes_match_button),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun EmptyLikesState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(80.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_heart),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            MiraiLinkText(
                text = stringResource(R.string.received_likes_empty),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun PremiumLockedState(
    modifier: Modifier = Modifier,
    onNavigateToPaywall: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bolt),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            MiraiLinkText(
                text = stringResource(R.string.premium_locked_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            MiraiLinkText(
                text = stringResource(R.string.premium_locked_desc),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onNavigateToPaywall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_bolt),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                MiraiLinkText(
                    text = stringResource(R.string.premium_locked_cta_button),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun EmptyLikesStatePreview() {
    EmptyLikesState()
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun PremiumLockedStatePreview() {
    PremiumLockedState(onNavigateToPaywall = {})
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun ReceivedLikeItemCardPreview() {
    ReceivedLikeItemCard(
        item = ReceivedLikeItemViewEntry(
            likeId = "1",
            userId = "u1",
            username = "sakura",
            nickname = "Sakura",
            age = 22,
            avatarUrl = null,
            likedAt = "Hace 2 horas",
        ),
        onClick = {},
        onMatchClick = {},
    )
}
