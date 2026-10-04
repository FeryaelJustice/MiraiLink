package com.feryaeljustice.mirailink.ui.screens.explore.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.enum.TargetSearchGender
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategoryDiscoverySettingsSheet(
    modifier: Modifier = Modifier,
    categoryName: String,
    initialRadiusKm: Int,
    initialTargetGender: TargetSearchGender? = null,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSaveSettings: (Int, TargetSearchGender?) -> Unit,
    isPlusOrPremium: Boolean = false,
    userGender: String? = null,
    onNavigateToPaywall: (() -> Unit)? = null,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sliderValue by remember(initialRadiusKm) {
        mutableFloatStateOf(initialRadiusKm.coerceIn(10, 500).toFloat())
    }
    var selectedGender by remember(initialTargetGender, userGender) {
        mutableStateOf(initialTargetGender ?: TargetSearchGender.defaultFor(userGender))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
        ) {
            MiraiLinkText(
                text = stringResource(R.string.category_settings_title, categoryName),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            MiraiLinkText(
                text = stringResource(R.string.category_settings_notice, categoryName),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.category_settings_distance_label),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                MiraiLinkText(
                    text = stringResource(R.string.category_settings_distance_value, sliderValue.roundToInt()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 10f..500f,
                steps = 48,
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                MiraiLinkText(
                    text = "10 km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.weight(1f))
                MiraiLinkText(
                    text = "500 km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val isRadiusLockedForSave = sliderValue > 250f && !isPlusOrPremium
            if (isRadiusLockedForSave) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        MiraiLinkText(
                            text = stringResource(R.string.search_radius_free_limit_note),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                        )
                        if (onNavigateToPaywall != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkButton(
                                onClick = {
                                    onDismiss()
                                    onNavigateToPaywall()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp),
                            ) {
                                MiraiLinkText(
                                    text = stringResource(R.string.search_upgrade_to_unlock),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de genero de busqueda
            MiraiLinkText(
                text = stringResource(R.string.search_gender_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            val effectiveGender = if (isPlusOrPremium) {
                selectedGender
            } else {
                TargetSearchGender.defaultFor(userGender)
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Mujeres
                FilterChip(
                    selected = effectiveGender == TargetSearchGender.FEMALE,
                    onClick = {
                        if (isPlusOrPremium) {
                            selectedGender = TargetSearchGender.FEMALE
                        } else {
                            onDismiss()
                            onNavigateToPaywall?.invoke()
                        }
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiraiLinkText(text = stringResource(R.string.search_gender_female))
                            if (!isPlusOrPremium) {
                                MiraiLinkText(
                                    text = " (${stringResource(R.string.search_gender_locked_badge)})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(),
                )

                // Hombres
                FilterChip(
                    selected = effectiveGender == TargetSearchGender.MALE,
                    onClick = {
                        if (isPlusOrPremium) {
                            selectedGender = TargetSearchGender.MALE
                        } else {
                            onDismiss()
                            onNavigateToPaywall?.invoke()
                        }
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiraiLinkText(text = stringResource(R.string.search_gender_male))
                            if (!isPlusOrPremium) {
                                MiraiLinkText(
                                    text = " (${stringResource(R.string.search_gender_locked_badge)})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(),
                )

                // Todos
                FilterChip(
                    selected = effectiveGender == TargetSearchGender.ALL,
                    onClick = {
                        selectedGender = TargetSearchGender.ALL
                    },
                    label = {
                        MiraiLinkText(text = stringResource(R.string.search_gender_all))
                    },
                    colors = FilterChipDefaults.filterChipColors(),
                )
            }

            if (!isPlusOrPremium) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        MiraiLinkText(
                            text = stringResource(R.string.search_gender_free_locked_disclaimer),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        if (onNavigateToPaywall != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkButton(
                                onClick = {
                                    onDismiss()
                                    onNavigateToPaywall()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp),
                            ) {
                                MiraiLinkText(
                                    text = stringResource(R.string.search_upgrade_to_unlock),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            MiraiLinkButton(
                onClick = {
                    val finalGender = if (isPlusOrPremium) selectedGender else null
                    onSaveSettings(sliderValue.roundToInt(), finalGender)
                },
                enabled = !isSaving && !isRadiusLockedForSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                MiraiLinkText(
                    text = stringResource(R.string.category_settings_update_button),
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
private fun CategoryDiscoverySettingsSheetPreview() {
    com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme {
        CategoryDiscoverySettingsSheet(
            categoryName = "Anime Fans",
            initialRadiusKm = 50,
            initialTargetGender = TargetSearchGender.FEMALE,
            isSaving = false,
            onDismiss = {},
            onSaveSettings = { _, _ -> },
            isPlusOrPremium = false,
            userGender = "male",
        )
    }
}

