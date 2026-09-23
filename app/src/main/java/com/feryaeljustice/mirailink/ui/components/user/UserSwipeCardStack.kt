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
import androidx.compose.ui.graphics.luminance
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry
import kotlinx.coroutines.launch
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
    users: List<UserViewEntry>,
    canUndo: Boolean,
    onSwipeLeft: () -> Unit,
    onGoBack: (() -> Unit),
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (users.isEmpty()) return

    val topUser = users.first()

    key(topUser.id) {
        val scope = rememberCoroutineScope()
        val offsetX = remember { Animatable(0f) }
        val offsetY = remember { Animatable(0f) }
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

        fun settleCard() {
            scope.launch {
                offsetX.animateTo(0f, animationSpec = spring())
                offsetY.animateTo(0f, animationSpec = spring())
            }
        }

        fun completeSwipe(direction: SwipeDirection) {
            scope.launch {
                offsetX.animateTo(
                    targetValue =
                        if (direction == SwipeDirection.Like) SwipeExitOffsetPx else -SwipeExitOffsetPx,
                    animationSpec = spring(),
                )
                if (direction == SwipeDirection.Like) onSwipeRight() else onSwipeLeft()
            }
        }

        Box(modifier = modifier.fillMaxSize()) {
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
                                    when {
                                        offsetX.value >= SwipeConfirmationThresholdPx ->
                                            completeSwipe(SwipeDirection.Like)
                                        offsetX.value <= -SwipeConfirmationThresholdPx ->
                                            completeSwipe(SwipeDirection.Dislike)
                                        else -> settleCard()
                                    }
                                },
                                onDragCancel = ::settleCard,
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    scope.launch {
                                        offsetX.snapTo(offsetX.value + dragAmount.x)
                                        offsetY.snapTo(offsetY.value + dragAmount.y)
                                    }
                                },
                            )
                        },
                user = topUser,
                onSave = {},
                isPublicPresentation = true,
            )

            SwipeActionButtons(
                activeDirection = activeDirection,
                canUndo = canUndo,
                onDislike = { completeSwipe(SwipeDirection.Dislike) },
                onUndo = onGoBack,
                onLike = { completeSwipe(SwipeDirection.Like) },
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
    activeDirection: SwipeDirection?,
    canUndo: Boolean,
    onDislike: () -> Unit,
    onUndo: () -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier,
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
            icon = Icons.Default.Close,
            contentDescription = stringResource(R.string.discard),
            isActive = activeDirection == SwipeDirection.Dislike,
            activeColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag("discardBtn"),
            onClick = onDislike,
        )

        if (canUndo) {
            SwipeActionButton(
                icon = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.comeback),
                isActive = false,
                activeColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.testTag("returnSwipeBtn"),
                onClick = onUndo,
            )
        }

        SwipeActionButton(
            icon = Icons.Default.Favorite,
            contentDescription = stringResource(R.string.like),
            isActive = activeDirection == SwipeDirection.Like,
            activeColor = SwipeLikeRed,
            modifier = Modifier.testTag("likeBtn"),
            onClick = onLike,
        )
    }
}

@Composable
private fun SwipeActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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

    IconButton(
        onClick = onClick,
        modifier =
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
                ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor,
            modifier = Modifier.size(34.dp),
        )
    }
}
