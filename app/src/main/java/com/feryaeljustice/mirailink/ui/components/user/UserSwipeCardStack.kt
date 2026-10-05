package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.mappers.ui.toUserViewEntry
import com.feryaeljustice.mirailink.domain.usecase.haptics.CalculateHeartbeatAffinityUseCase
import com.feryaeljustice.mirailink.domain.usecase.users.GetCurrentUserUseCase
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import com.feryaeljustice.mirailink.state.GlobalMiraiLinkSession
import com.feryaeljustice.mirailink.ui.components.haptics.hapticHeartbeatLikeTrigger
import com.feryaeljustice.mirailink.ui.haptics.HapticHeartbeatController
import com.feryaeljustice.mirailink.ui.holo.HoloRenderController
import com.feryaeljustice.mirailink.ui.holo.BindHoloController
import com.feryaeljustice.mirailink.ui.holo.observeHoloTouch
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.math.abs

private const val SwipeConfirmationThresholdPx = 300f
private const val SwipeExitOffsetPx = 1000f
private val SwipeLikeRed = Color(0xFFE53935)

private enum class SwipeDirection {
    Dislike,
    Like,
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserSwipeCardStack(
    modifier: Modifier = Modifier,
    users: List<UserViewEntry>,
    canUndo: Boolean,
    onSwipeLeft: () -> Unit,
    onGoBack: (() -> Unit),
    onSwipeRight: () -> Unit,
    currentUser: UserViewEntry? = null,
) {
    if (androidx.compose.ui.platform.LocalInspectionMode.current) {
        if (users.isEmpty()) return
        val topUser = users.first()
        Box(modifier = modifier.fillMaxSize()) {
            UserCard(
                modifier = Modifier.fillMaxSize(),
                user = topUser,
                onSave = {},
                isPublicPresentation = true,
            )
            SwipeActionButtons(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(2f),
                activeDirection = null,
                canUndo = canUndo,
                onDislike = onSwipeLeft,
                onUndo = onGoBack,
                onLike = onSwipeRight,
            )
        }
        return
    }

    UserSwipeCardStack(
        modifier = modifier,
        users = users,
        canUndo = canUndo,
        onSwipeLeft = onSwipeLeft,
        onGoBack = onGoBack,
        onSwipeRight = onSwipeRight,
        currentUser = currentUser,
        hapticController = koinInject(),
        affinityUseCase = koinInject(),
        getCurrentUserUseCase = koinInject(),
        miraiLinkSession = koinInject(),
        holoController = koinInject(),
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun UserSwipeCardStack(
    modifier: Modifier = Modifier,
    users: List<UserViewEntry>,
    canUndo: Boolean,
    onSwipeLeft: () -> Unit,
    onGoBack: (() -> Unit),
    onSwipeRight: () -> Unit,
    currentUser: UserViewEntry? = null,
    hapticController: HapticHeartbeatController,
    affinityUseCase: CalculateHeartbeatAffinityUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    miraiLinkSession: GlobalMiraiLinkSession,
    holoController: HoloRenderController? = null,
) {
    if (users.isEmpty()) return

    val topUser = users.first()
    var localCurrentUser by remember { mutableStateOf(currentUser) }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            localCurrentUser = currentUser
        }
    }

    LaunchedEffect(Unit) {
        if (localCurrentUser == null) {
            val res = getCurrentUserUseCase()
            if (res is MiraiLinkResult.Success) {
                localCurrentUser = res.data.toUserViewEntry()
            }
        }
    }

    val affinity =
        remember(topUser.id, localCurrentUser?.id) {
            affinityUseCase(
                userGameIds = localCurrentUser?.games?.map { it.id }?.toSet().orEmpty(),
                userAnimeIds = localCurrentUser?.animes?.map { it.id }?.toSet().orEmpty(),
                userGoalIds = localCurrentUser?.relationshipGoalIds?.toSet().orEmpty(),
                candidateGameIds = topUser.games.map { it.id }.toSet(),
                candidateAnimeIds = topUser.animes.map { it.id }.toSet(),
                candidateGoalIds = topUser.relationshipGoalIds.toSet(),
            )
        }

    var holdProgress by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        onDispose {
            hapticController.stopHeartbeat()
            miraiLinkSession.hideHeartbeatOverlay()
        }
    }

    key(topUser.id) {
        val scope = rememberCoroutineScope()
        val offsetX = remember { Animatable(0f) }
        val offsetY = remember { Animatable(0f) }
        BindHoloController(holoController, moving = abs(offsetX.value) > 0.5f || abs(offsetY.value) > 0.5f)
        val rotation = (offsetX.value / 60).coerceIn(-40f, 40f)
        val alphaAnim by animateFloatAsState(
            targetValue = 1 - (abs(offsetX.value) / SwipeExitOffsetPx),
            label = "swipeCardAlpha",
        )
        val activeDirection =
            when {
                offsetX.value >= SwipeConfirmationThresholdPx -> SwipeDirection.Like
                offsetX.value <= -SwipeConfirmationThresholdPx -> SwipeDirection.Dislike
                else -> null
            }
        val dragDistance = kotlin.math.hypot(offsetX.value, offsetY.value)
        val dragProgress = (dragDistance / 50f).coerceIn(0f, 1f)
        val cornerRadius = (28 * dragProgress).dp
        val cardShape = RoundedCornerShape(cornerRadius)
        val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
        val borderColor =
            if (isDark) {
                Color.White.copy(alpha = 0.32f * dragProgress)
            } else {
                Color.White.copy(alpha = 0.85f * dragProgress)
            }
        val cardScale = 1.02f - (0.06f * dragProgress)

        var isSwipeHeartbeatActive by remember { mutableStateOf(false) }

        fun settleCard() {
            if (isSwipeHeartbeatActive) {
                isSwipeHeartbeatActive = false
                holdProgress = 0f
                hapticController.stopHeartbeat()
                miraiLinkSession.hideHeartbeatOverlay()
            }
            scope.launch {
                offsetX.animateTo(0f, animationSpec = spring())
                offsetY.animateTo(0f, animationSpec = spring())
            }
        }

        fun completeSwipe(direction: SwipeDirection) {
            if (isSwipeHeartbeatActive) {
                isSwipeHeartbeatActive = false
                holdProgress = 0f
                miraiLinkSession.hideHeartbeatOverlay()
            }
            scope.launch {
                offsetX.animateTo(
                    targetValue =
                        if (direction == SwipeDirection.Like) SwipeExitOffsetPx else -SwipeExitOffsetPx,
                    animationSpec = spring(),
                )
                if (direction == SwipeDirection.Like) onSwipeRight() else onSwipeLeft()
            }
        }

        Box(modifier = modifier.fillMaxSize().observeHoloTouch(holoController)) {
            users.getOrNull(1)?.let { nextUser ->
                UserCard(
                    modifier =
                        Modifier
                            .graphicsLayer {
                                scaleX = 1.02f
                                scaleY = 1.02f
                            }.alpha(0.5f),
                    user = nextUser,
                    onSave = {},
                    isPublicPresentation = true,
                )
            }

            UserCard(
                modifier =
                    Modifier
                        .graphicsLayer {
                            translationX = offsetX.value
                            translationY = offsetY.value
                            rotationZ = rotation
                            scaleX = cardScale
                            scaleY = cardScale
                            alpha = alphaAnim
                        }
                        .shadow(
                            elevation = 16.dp * dragProgress,
                            shape = cardShape,
                            clip = false,
                        )
                        .clip(cardShape)
                        .border(
                            width = 1.5.dp,
                            color = borderColor,
                            shape = cardShape,
                        )
                        .pointerInput(topUser.id) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (isSwipeHeartbeatActive) {
                                        isSwipeHeartbeatActive = false
                                        holdProgress = 0f
                                        miraiLinkSession.hideHeartbeatOverlay()
                                    }
                                    when {
                                        offsetX.value >= SwipeConfirmationThresholdPx -> {
                                            hapticController.triggerLikeConfirmation()
                                            completeSwipe(SwipeDirection.Like)
                                        }
                                        offsetX.value <= -SwipeConfirmationThresholdPx -> {
                                            hapticController.stopHeartbeat()
                                            completeSwipe(SwipeDirection.Dislike)
                                        }
                                        else -> {
                                            hapticController.stopHeartbeat()
                                            settleCard()
                                        }
                                    }
                                },
                                onDragCancel = {
                                    if (isSwipeHeartbeatActive) {
                                        isSwipeHeartbeatActive = false
                                        holdProgress = 0f
                                        hapticController.stopHeartbeat()
                                        miraiLinkSession.hideHeartbeatOverlay()
                                    }
                                    settleCard()
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    val newX = offsetX.value + dragAmount.x
                                    val newY = offsetY.value + dragAmount.y
                                    scope.launch {
                                        offsetX.snapTo(newX)
                                        offsetY.snapTo(newY)
                                    }

                                    if (newX >= 60f) {
                                        val progress = (newX / SwipeConfirmationThresholdPx).coerceIn(0f, 1f)
                                        if (!isSwipeHeartbeatActive) {
                                            isSwipeHeartbeatActive = true
                                            hapticController.startHeartbeat(affinity.ratio)
                                            miraiLinkSession.showHeartbeatOverlay(
                                                affinity = affinity,
                                                progress = progress,
                                                targetNickname = topUser.nickname,
                                                isSwipe = true,
                                            )
                                        }
                                        holdProgress = progress
                                        miraiLinkSession.updateHeartbeatProgress(progress)
                                    } else {
                                        if (isSwipeHeartbeatActive) {
                                            isSwipeHeartbeatActive = false
                                            holdProgress = 0f
                                            hapticController.stopHeartbeat()
                                            miraiLinkSession.hideHeartbeatOverlay()
                                        }
                                    }
                                },
                            )
                        },
                user = topUser,
                onSave = {},
                isPublicPresentation = true,
                holoController = holoController,
            )

            val likeGestureModifier =
                Modifier.hapticHeartbeatLikeTrigger(
                    onTap = { completeSwipe(SwipeDirection.Like) },
                    onHoldStart = {
                        hapticController.startHeartbeat(affinity.ratio)
                        miraiLinkSession.showHeartbeatOverlay(
                            affinity = affinity,
                            progress = 0f,
                            targetNickname = topUser.nickname,
                            isSwipe = false,
                        )
                    },
                    onHoldProgress = {
                        holdProgress = it
                        miraiLinkSession.updateHeartbeatProgress(it)
                    },
                    onHoldComplete = {
                        holdProgress = 0f
                        miraiLinkSession.hideHeartbeatOverlay()
                        hapticController.triggerLikeConfirmation()
                        completeSwipe(SwipeDirection.Like)
                    },
                    onHoldCancel = {
                        holdProgress = 0f
                        hapticController.stopHeartbeat()
                        miraiLinkSession.hideHeartbeatOverlay()
                    },
                )

            SwipeActionButtons(
                activeDirection = activeDirection,
                canUndo = canUndo,
                onDislike = { completeSwipe(SwipeDirection.Dislike) },
                onUndo = onGoBack,
                onLike = { completeSwipe(SwipeDirection.Like) },
                likeGestureModifier = likeGestureModifier,
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(2f),
            )
        }
    }
}

@Composable
private fun SwipeActionButtons(
    modifier: Modifier = Modifier,
    activeDirection: SwipeDirection?,
    canUndo: Boolean,
    onDislike: () -> Unit,
    onUndo: () -> Unit,
    onLike: () -> Unit,
    likeGestureModifier: Modifier? = null,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(96.dp)
                .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SwipeActionButton(
            modifier = Modifier.testTag("discardBtn"),
            icon = Icons.Default.Close,
            contentDescription = stringResource(R.string.discard),
            isActive = activeDirection == SwipeDirection.Dislike,
            activeColor = MaterialTheme.colorScheme.onSurface,
            onClick = onDislike,
        )

        if (canUndo) {
            SwipeActionButton(
                modifier = Modifier.testTag("returnSwipeBtn"),
                icon = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.comeback),
                isActive = false,
                activeColor = MaterialTheme.colorScheme.tertiary,
                onClick = onUndo,
            )
        }

        SwipeActionButton(
            modifier = Modifier.testTag("likeBtn"),
            icon = Icons.Default.Favorite,
            contentDescription = stringResource(R.string.like),
            isActive = activeDirection == SwipeDirection.Like,
            activeColor = SwipeLikeRed,
            onClick = onLike,
            gestureModifier = likeGestureModifier,
        )
    }
}

@Composable
private fun SwipeActionButton(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    gestureModifier: Modifier? = null,
) {
    val containerColor by animateColorAsState(
        targetValue =
            if (isActive) activeColor else MaterialTheme.colorScheme.surfaceContainerHigh,
        label = "swipeActionContainer",
    )
    val contentColor by animateColorAsState(
        targetValue =
            if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "swipeActionContent",
    )
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.14f else 1f,
        animationSpec = spring(),
        label = "swipeActionScale",
    )

    val baseModifier =
        modifier
            .size(72.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }.shadow(elevation = if (isActive) 12.dp else 6.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(containerColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape,
            )

    if (gestureModifier != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = baseModifier.then(gestureModifier),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(34.dp),
            )
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier = baseModifier,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = contentColor,
                modifier = Modifier.size(34.dp),
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun UserSwipeCardStackPreview() {
    com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme {
        UserSwipeCardStack(
            users =
                listOf(
                    UserViewEntry(
                        id = "1",
                        username = "sakura",
                        nickname = "Sakura",
                        email = null,
                        phoneNumber = null,
                        bio = "Anime fan and gamer",
                        gender = "female",
                        birthdate = "2000-01-01",
                        games = emptyList(),
                        animes = emptyList(),
                    ),
                ),
            canUndo = true,
            onSwipeLeft = {},
            onGoBack = {},
            onSwipeRight = {},
        )
    }
}

