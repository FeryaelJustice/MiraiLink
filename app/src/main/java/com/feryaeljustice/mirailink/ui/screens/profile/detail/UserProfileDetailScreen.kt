package com.feryaeljustice.mirailink.ui.screens.profile.detail

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.ui.components.user.ChipFlowRow
import com.feryaeljustice.mirailink.ui.components.user.GamerPromptCard
import com.feryaeljustice.mirailink.ui.components.user.buildPersonalChips
import coil.request.ImageRequest
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.calculateAge
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.media.FullscreenImagePreview
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun UserProfileDetailScreen(
    username: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    canInteract: Boolean = true,
    viewModel: UserProfileDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var fullscreenImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(username) {
        viewModel.loadProfile(username)
    }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                is UserProfileDetailUiEvent.MatchCreated -> {
                    showToast(
                        context,
                        context.getString(R.string.received_likes_match_success, event.nickname),
                    )
                }
                UserProfileDetailUiEvent.Disliked -> {
                    onBackClick()
                }
            }
        }
    }

    val shareText = stringResource(R.string.share_profile_text, username)
    val shareActionTitle = stringResource(R.string.action_share)

    val onShare: () -> Unit = {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(shareIntent, shareActionTitle))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    MiraiLinkText(
                        text = "@$username",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.action_share),
                        )
                    }
                },
                windowInsets = WindowInsets(0.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            if (canInteract && uiState.user != null && !uiState.isInteracted) {
                BottomInteractionBar(
                    onLike = { viewModel.likeUser() },
                    onDislike = { viewModel.dislikeUser() },
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                MiraiLinkErrorContent(
                    error = uiState.error!!,
                    onAction = { viewModel.loadProfile(username) },
                )
            } else if (uiState.user != null) {
                UserProfileDetailContent(
                    user = uiState.user!!,
                    onPhotoLongPress = { url -> fullscreenImageUrl = url },
                )
            }
        }
    }

    fullscreenImageUrl?.let { url ->
        FullscreenImagePreview(
            imageUrl = url,
            onDismiss = { fullscreenImageUrl = null },
            closeContentDescription = stringResource(R.string.close),
            imageContentDescription = username,
        )
    }
}

@Composable
private fun UserProfileDetailContent(
    user: UserViewEntry,
    onPhotoLongPress: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        // Carousel
        val photos = user.photos.map { it.url }.ifEmpty { listOf(null) }
        val pagerState = rememberPagerState(pageCount = { photos.size })

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                val photoUrl = photos[page]
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photoUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.logomirailink)
                        .error(R.drawable.logomirailink)
                        .build(),
                    contentDescription = user.nickname,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(photoUrl) {
                            detectTapGestures(
                                onLongPress = {
                                    photoUrl?.let { onPhotoLongPress(it) }
                                },
                            )
                        },
                )
            }

            // Pager Indicator Dots
            if (photos.size > 1) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    repeat(photos.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        Box(
                            modifier = Modifier
                                .size(if (isSelected) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                ),
                        )
                    }
                }
            }
        }

        // Content Sections
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Nickname & Age
            val age = calculateAge(user.birthdate)
            val headerText = buildString {
                append(user.nickname)
                if (age != null) {
                    append(", $age")
                }
            }
            MiraiLinkText(
                text = headerText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            // Gender & Residence & Distance
            val locationParts = listOfNotNull(
                user.residenceCity?.takeIf { it.isNotBlank() },
                user.residenceRegion?.takeIf { it.isNotBlank() },
                user.residenceCountry?.takeIf { it.isNotBlank() },
            ).joinToString(", ")

            if (locationParts.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                MiraiLinkText(
                    text = stringResource(R.string.card_lives_in, locationParts),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            user.distanceKm?.let { dist ->
                Spacer(modifier = Modifier.height(4.dp))
                MiraiLinkText(
                    text = stringResource(R.string.card_distance_km, dist.toInt()),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            // Bio
            if (!user.bio.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                ) {
                    MiraiLinkText(
                        text = user.bio,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp),
                    )
                }
            }

            // Gamer Prompts / Facts
            if (user.prompts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                MiraiLinkText(
                    text = stringResource(R.string.profile_section_facts),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                user.prompts.take(3).forEach { prompt ->
                    GamerPromptCard(prompt = prompt)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Personal Information
            val personalChips = buildPersonalChips(user)
            if (personalChips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                MiraiLinkText(
                    text = stringResource(R.string.profile_section_personal),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChipFlowRow(items = personalChips)
            }

            // Interests: Animes & Games
            val allInterests = (user.animes.map { it.name } + user.games.map { it.name }).filter { it.isNotBlank() }
            if (allInterests.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                MiraiLinkText(
                    text = stringResource(R.string.profile_section_interests),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChipFlowRow(items = allInterests)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun BottomInteractionBar(
    onLike: () -> Unit,
    onDislike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FilledTonalButton(
                onClick = onDislike,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                MiraiLinkText(
                    text = stringResource(R.string.profile_detail_reject),
                    fontWeight = FontWeight.Bold,
                )
            }

            Button(
                onClick = onLike,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_heart),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                MiraiLinkText(
                    text = stringResource(R.string.profile_detail_like),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
