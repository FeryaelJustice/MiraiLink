package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.Gender
import com.feryaeljustice.mirailink.domain.util.GeoUtils
import com.feryaeljustice.mirailink.domain.util.nicknameElseUsername
import com.feryaeljustice.mirailink.domain.util.toAgeOrNull
import com.feryaeljustice.mirailink.ui.components.media.PhotoCarousel
import com.feryaeljustice.mirailink.ui.components.media.PhotoCarouselController
import com.feryaeljustice.mirailink.ui.utils.extensions.localizedLabel
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

@Composable
internal fun PublicUserCard(
    user: UserViewEntry,
    onLongPressOnImage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val photoCarouselController = remember { PhotoCarouselController() }
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val overlayBase = if (isDark) Color(0xFF07111F) else Color(0xFFF8FAFF)
    val contentColor = if (isDark) Color.White else Color(0xFF10131A)
    val outlineColor = if (isDark) Color.Black.copy(alpha = 0.85f) else Color.White.copy(alpha = 0.92f)

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .testTag("userCard")
                .pointerInput(photoCarouselController) {
                    detectTapGestures(
                        onTap = { offset ->
                            if (offset.x < size.width / 2f) {
                                photoCarouselController.previous()
                            } else {
                                photoCarouselController.next()
                            }
                        },
                        onLongPress = { photoCarouselController.openCurrent() },
                    )
                },
    ) {
        val publicContentTopPadding = maxHeight * 0.48f

        PhotoCarousel(
            photoUrls = user.photos.map { it.url },
            onLongPressOnImage = onLongPressOnImage,
            modifier = Modifier.fillMaxSize(),
            immersive = true,
            controller = photoCarouselController,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops =
                                arrayOf(
                                    0f to Color.Black.copy(alpha = 0.34f),
                                    0.18f to Color.Transparent,
                                    0.48f to Color.Transparent,
                                    0.72f to overlayBase.copy(alpha = 0.5f),
                                    1f to overlayBase.copy(alpha = 0.98f),
                                ),
                        ),
                    ),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(publicContentTopPadding))
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedOverlayText(
                    text = user.nicknameElseUsername(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = contentColor,
                    outlineColor = outlineColor,
                    fontWeight = FontWeight.ExtraBold,
                )

                PublicSectionHeader(
                    title = stringResource(R.string.profile_section_basic),
                    icon = Icons.Default.Info,
                    contentColor = contentColor,
                )

                OutlinedOverlayText(
                    text = if (!user.bio.isNullOrBlank()) user.bio else stringResource(R.string.bio_not_set),
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor,
                    outlineColor = outlineColor,
                    fontStyle = FontStyle.Italic,
                )

                Gender.fromRealValue(user.gender)?.let { gender ->
                    PublicInfoLine(
                        text = stringResource(R.string.gender_presentation, gender.localizedLabel()),
                        color = contentColor,
                    )
                }
                user.birthdate.toAgeOrNull()?.takeIf { it.isNotBlank() }?.let { age ->
                    PublicInfoLine(
                        text = stringResource(R.string.age_presentation, age),
                        color = contentColor,
                    )
                }

                PublicResidence(user = user, contentColor = contentColor)

                PublicSectionHeader(
                    title = stringResource(R.string.profile_section_interests),
                    icon = Icons.Default.Favorite,
                    contentColor = contentColor,
                )
                PublicInterestChips(
                    labels = (user.animes.map { it.name } + user.games.map { it.name }).distinct(),
                    contentColor = contentColor,
                    isDark = isDark,
                )
                Spacer(modifier = Modifier.height(116.dp))
            }
        }
    }
}

@Composable
private fun PublicResidence(
    user: UserViewEntry,
    contentColor: Color,
) {
    val countryName =
        user.residenceCountryCode?.let { code ->
            java.util.Locale("", code).getDisplayCountry(java.util.Locale.getDefault())
        }
    val place = listOfNotNull(user.residenceCity, countryName).filter { it.isNotBlank() }.joinToString(", ")
    val distance = GeoUtils.formatDistance(user.distanceKm)

    if (place.isNotBlank() || distance != null || user.isTraveler) {
        PublicSectionHeader(
            title = stringResource(R.string.profile_section_residence),
            icon = Icons.Default.LocationOn,
            contentColor = contentColor,
        )
        if (place.isNotBlank()) {
            PublicInfoLine(text = stringResource(R.string.card_lives_in, place), color = contentColor)
        }
        distance?.let { PublicInfoLine(text = it, color = contentColor) }
        if (user.isTraveler) {
            PublicInfoLine(text = stringResource(R.string.card_traveler_badge), color = contentColor)
        }
    }
}

@Composable
private fun PublicSectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(22.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor,
        )
    }
}

@Composable
private fun PublicInfoLine(
    text: String,
    color: Color,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = Modifier.padding(start = 4.dp),
    )
}

@Composable
private fun PublicInterestChips(
    labels: List<String>,
    contentColor: Color,
    isDark: Boolean,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        labels.forEach { label ->
            Surface(
                color = if (isDark) Color.Black.copy(alpha = 0.46f) else Color.White.copy(alpha = 0.64f),
                contentColor = contentColor,
                shape = RoundedCornerShape(50),
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun OutlinedOverlayText(
    text: String,
    style: TextStyle,
    color: Color,
    outlineColor: Color,
    fontWeight: FontWeight = FontWeight.Normal,
    fontStyle: FontStyle = FontStyle.Normal,
) {
    Box {
        Text(
            text = text,
            style = style.copy(drawStyle = Stroke(width = 4f)),
            color = outlineColor,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            modifier = Modifier.clearAndSetSemantics {},
        )
        Text(
            text = text,
            style = style,
            color = color,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
        )
    }
}
