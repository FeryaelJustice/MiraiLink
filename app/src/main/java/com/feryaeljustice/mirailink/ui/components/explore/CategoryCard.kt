package com.feryaeljustice.mirailink.ui.components.explore

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.explore.ExploreCategory
import com.feryaeljustice.mirailink.domain.model.explore.ExploreSectionGroup
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@DrawableRes
fun resolveCategoryIconDrawable(iconKey: String): Int =
    when (iconKey.lowercase()) {
        "sparkles", "ic_sparkles" -> R.drawable.ic_bolt
        "anime", "ic_anime", "manga", "ic_manga" -> R.drawable.ic_user
        "cosplay", "ic_cosplay" -> R.drawable.ic_user
        "ramen", "ic_ramen" -> R.drawable.ic_explore
        "gaming", "ic_gaming", "gamepad", "ic_gamepad" -> R.drawable.ic_bolt
        "coop", "ic_coop", "casual", "ic_casual" -> R.drawable.ic_bolt
        "love", "ic_love", "heart", "ic_heart" -> R.drawable.ic_heart
        "coffee", "ic_coffee" -> R.drawable.ic_chat
        "friends", "ic_friends", "connections" -> R.drawable.ic_chat
        else -> R.drawable.ic_explore
    }

fun getSectionGradient(group: ExploreSectionGroup): Brush =
    when (group) {
        ExploreSectionGroup.OTAKU ->
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF8E24AA).copy(alpha = 0.85f),
                    Color(0xFFE91E63).copy(alpha = 0.85f),
                ),
            )
        ExploreSectionGroup.GAMING ->
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF3949AB).copy(alpha = 0.85f),
                    Color(0xFF00ACC1).copy(alpha = 0.85f),
                ),
            )
        ExploreSectionGroup.CONNECTIONS ->
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF4511E).copy(alpha = 0.85f),
                    Color(0xFFE91E63).copy(alpha = 0.85f),
                ),
            )
    }

@Composable
fun CategoryCarouselCard(
    category: ExploreCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getSectionGradient(category.sectionGroup))
                .padding(14.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = resolveCategoryIconDrawable(category.iconKey)),
                            contentDescription = category.title,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    if (category.activeCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.35f),
                        ) {
                            TextCountBadge(
                                text = stringResource(R.string.explore_people_count, category.activeCount),
                            )
                        }
                    }
                }

                Column {
                    MiraiLinkText(
                        text = category.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (category.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        MiraiLinkText(
                            text = category.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGridCard(
    category: ExploreCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(getSectionGradient(category.sectionGroup))
                .padding(14.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(id = resolveCategoryIconDrawable(category.iconKey)),
                            contentDescription = category.title,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp),
                        )
                    }

                    if (category.activeCount > 0) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.35f),
                        ) {
                            TextCountBadge(
                                text = stringResource(R.string.explore_people_count, category.activeCount),
                            )
                        }
                    }
                }

                Column {
                    MiraiLinkText(
                        text = category.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    if (category.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        MiraiLinkText(
                            text = category.description,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TextCountBadge(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF4CAF50)),
        )
        Spacer(modifier = Modifier.width(5.dp))
        MiraiLinkText(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
