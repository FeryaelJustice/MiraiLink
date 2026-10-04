package com.feryaeljustice.mirailink.ui.screens.subscription

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionDuration
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionOfferOption
import com.feryaeljustice.mirailink.domain.model.subscription.SubscriptionPlanType
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.error.asString
import com.feryaeljustice.mirailink.ui.utils.toast.showToast
import org.koin.compose.viewmodel.koinViewModel

private val GoldAccent = Color(0xFFFFB300)
private val GoldAccentLight = Color(0xFFFFD54F)
private val CyanAccent = Color(0xFF00E5FF)
private val CyanAccentLight = Color(0xFF80D8FF)

@Suppress("ktlint:standard:function-naming")
@Composable
fun SubscriptionPaywallScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SubscriptionPaywallViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity
    val purchaseSuccessMessage = stringResource(R.string.subscription_purchase_success_message)

    LaunchedEffect(uiState.isSuccess, purchaseSuccessMessage) {
        if (uiState.isSuccess) {
            showToast(
                context,
                purchaseSuccessMessage,
                Toast.LENGTH_LONG,
            )
            onBackClick()
        }
    }

    LaunchedEffect(uiState.messageResId) {
        uiState.messageResId?.let { resId ->
            showToast(context, context.resources.getString(resId), Toast.LENGTH_SHORT)
            viewModel.clearMessage()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            showToast(context, err.asString(context), Toast.LENGTH_LONG)
            viewModel.clearError()
        }
    }

    val isPlusSelected = uiState.selectedTier == SubscriptionPlanType.PLUS
    val themeAccentColor = if (isPlusSelected) CyanAccent else GoldAccent

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            if (isPlusSelected) CyanAccent.copy(alpha = 0.14f) else GoldAccent.copy(alpha = 0.14f),
            MaterialTheme.colorScheme.surface,
        ),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGradient),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top Bar: [X] button on left, Segmented Tabs in center
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Tier Selector (Plus / Premium)
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    modifier = Modifier.padding(horizontal = 4.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        TierTabButton(
                            title = stringResource(R.string.subscription_tier_plus),
                            isSelected = isPlusSelected,
                            selectedColor = CyanAccent,
                            onClick = { viewModel.selectTier(SubscriptionPlanType.PLUS) },
                        )
                        TierTabButton(
                            title = stringResource(R.string.subscription_tier_premium),
                            isSelected = !isPlusSelected,
                            selectedColor = GoldAccent,
                            onClick = { viewModel.selectTier(SubscriptionPlanType.PREMIUM) },
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Balance space for left icon
                Spacer(modifier = Modifier.size(36.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Emblem Badge (Otaku/Anime glowing emblem)
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier
                    .size(72.dp)
                    .border(2.dp, themeAccentColor.copy(alpha = 0.6f), CircleShape),
                shadowElevation = 6.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.ic_bolt),
                        contentDescription = null,
                        tint = themeAccentColor,
                        modifier = Modifier.size(38.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Headline
            val headlineText = if (isPlusSelected) {
                stringResource(R.string.subscription_paywall_plus_headline)
            } else {
                stringResource(R.string.subscription_paywall_premium_headline)
            }

            MiraiLinkText(
                text = headlineText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            Spacer(modifier = Modifier.height(6.dp))

            MiraiLinkText(
                text = stringResource(R.string.subscription_paywall_subtitle),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3 Horizontal Duration Cards (Tinder Gold style)
            DurationCardsRow(
                currentOffers = uiState.currentOffers,
                selectedDuration = uiState.selectedDuration,
                accentColor = themeAccentColor,
                onSelectDuration = { viewModel.selectDuration(it) },
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Included perks section
            val tierName = if (isPlusSelected) {
                stringResource(R.string.subscription_tier_plus)
            } else {
                stringResource(R.string.subscription_tier_premium)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.subscription_paywall_included_with, tierName),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Benefits list container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                ),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    if (isPlusSelected) {
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_plus_ads),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_plus_radius),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_plus_gender),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_plus_likes),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_plus_badge),
                            accentColor = themeAccentColor,
                        )
                    } else {
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_premium_plus_all),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_premium_likes),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_premium_passport),
                            accentColor = themeAccentColor,
                        )
                        PerkItemRow(
                            text = stringResource(R.string.subscription_paywall_perks_premium_badge),
                            accentColor = themeAccentColor,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Google Play Terms Disclaimer
            MiraiLinkText(
                text = stringResource(R.string.subscription_paywall_terms_notice),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent CTA Button
            val selectedOffer = uiState.currentOffer
            val priceText = selectedOffer?.formattedPrice ?: uiState.formattedPrice ?: ""
            val ctaLabel = if (priceText.isNotBlank()) {
                stringResource(R.string.subscription_paywall_continue_total, priceText)
            } else {
                stringResource(R.string.subscription_paywall_cta_button)
            }

            Button(
                onClick = { activity?.let { viewModel.startPurchase(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isPurchasing && !uiState.isRestoring,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isPlusSelected) Color(0xFF0091EA) else Color(0xFFFF8F00),
                    contentColor = Color.White,
                ),
            ) {
                if (uiState.isPurchasing) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp),
                    )
                } else {
                    MiraiLinkText(
                        text = ctaLabel,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Restore purchases button
            TextButton(
                onClick = { viewModel.restorePurchases() },
                enabled = !uiState.isPurchasing && !uiState.isRestoring,
            ) {
                if (uiState.isRestoring) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
                MiraiLinkText(
                    text = stringResource(R.string.subscription_paywall_restore_button),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun TierTabButton(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit,
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor.copy(alpha = 0.2f) else Color.Transparent,
        label = "tabBg",
    )
    val textColor = if (isSelected) selectedColor else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        MiraiLinkText(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = textColor,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun DurationCardsRow(
    modifier: Modifier = Modifier,
    currentOffers: List<SubscriptionOfferOption>,
    selectedDuration: SubscriptionDuration,
    accentColor: Color,
    onSelectDuration: (SubscriptionDuration) -> Unit,
) {
    val weeklyOffer = currentOffers.firstOrNull { it.duration == SubscriptionDuration.WEEKLY }
    val monthlyOffer = currentOffers.firstOrNull { it.duration == SubscriptionDuration.MONTHLY }
    val threeMonthOffer = currentOffers.firstOrNull { it.duration == SubscriptionDuration.THREE_MONTHS }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        DurationOptionCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.subscription_duration_monthly),
            pricePerPeriod = monthlyOffer?.formattedPricePerPeriod ?: "0,99 € / mes",
            totalPrice = stringResource(R.string.subscription_price_total, monthlyOffer?.formattedPrice ?: "0,99 €"),
            badgeText = monthlyOffer?.discountBadge ?: stringResource(R.string.subscription_badge_popular),
            isSelected = selectedDuration == SubscriptionDuration.MONTHLY,
            accentColor = accentColor,
            onClick = { onSelectDuration(SubscriptionDuration.MONTHLY) },
        )

        DurationOptionCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.subscription_duration_weekly),
            pricePerPeriod = weeklyOffer?.formattedPricePerPeriod ?: "0,49 € / sem",
            totalPrice = stringResource(R.string.subscription_price_total, weeklyOffer?.formattedPrice ?: "0,49 €"),
            badgeText = null,
            isSelected = selectedDuration == SubscriptionDuration.WEEKLY,
            accentColor = accentColor,
            onClick = { onSelectDuration(SubscriptionDuration.WEEKLY) },
        )

        DurationOptionCard(
            modifier = Modifier.weight(1f),
            title = stringResource(R.string.subscription_duration_three_months),
            pricePerPeriod = threeMonthOffer?.formattedPricePerPeriod ?: "0,83 € / mes",
            totalPrice = stringResource(R.string.subscription_price_total, threeMonthOffer?.formattedPrice ?: "2,49 €"),
            badgeText = threeMonthOffer?.discountBadge ?: stringResource(R.string.subscription_badge_best_value),
            isSelected = selectedDuration == SubscriptionDuration.THREE_MONTHS,
            accentColor = accentColor,
            onClick = { onSelectDuration(SubscriptionDuration.THREE_MONTHS) },
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun DurationOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    pricePerPeriod: String,
    totalPrice: String,
    badgeText: String?,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
) {
    val borderColor = if (isSelected) accentColor else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val containerColor = if (isSelected) {
        accentColor.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceContainerLow
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(16.dp))
                .background(containerColor)
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Badge or spacer
            if (badgeText != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = accentColor,
                    modifier = Modifier.padding(bottom = 6.dp),
                ) {
                    MiraiLinkText(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Duration title
            MiraiLinkText(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkmark indicator (circle)
            Surface(
                shape = CircleShape,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.size(24.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price per period
            MiraiLinkText(
                text = pricePerPeriod,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Total price
            MiraiLinkText(
                text = totalPrice,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun PerkItemRow(
    modifier: Modifier = Modifier,
    text: String,
    accentColor: Color,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MiraiLinkText(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f).padding(end = 12.dp),
        )

        Surface(
            shape = CircleShape,
            color = accentColor.copy(alpha = 0.2f),
            modifier = Modifier.size(26.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PerkItemRowPreview() {
    MaterialTheme {
        PerkItemRow(
            text = "See who liked your profile",
            accentColor = Color(0xFFFFB300),
            modifier = Modifier.padding(16.dp),
        )
    }
}
