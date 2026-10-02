package com.feryaeljustice.mirailink.ui.screens.settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.settings.SearchPreferences
import com.feryaeljustice.mirailink.domain.model.settings.SearchScope
import com.feryaeljustice.mirailink.domain.util.isCountryCodeValid
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.map.SearchRadiusMinimap

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchSettingsSection(
    modifier: Modifier = Modifier,
    radiusKm: Float,
    onRadiusChange: (Float) -> Unit,
    scope: SearchScope,
    onScopeChange: (SearchScope) -> Unit,
    targetCountry: String?,
    onTargetCountryChange: (String?) -> Unit,
    hasUnsavedChanges: Boolean,
    isSaving: Boolean,
    onSaveClick: () -> Unit,
    latitude: Double = 39.5696,
    longitude: Double = 2.6502,
    isMapVisible: Boolean = false,
    onRequestLocationPermission: (() -> Unit)? = null,
    onRefreshLocation: () -> Unit = {},
    isRefreshingLocation: Boolean = false,
    isPlus: Boolean = false,
    isPremium: Boolean = false,
    onNavigateToPaywall: (() -> Unit)? = null,
) {
    val isRadiusUnlocked = isPlus || isPremium
    val isPassportUnlocked = isPremium

    val displayRadius = radiusKm.toInt()
    val isCountryValid = targetCountry.isNullOrBlank() || targetCountry.isCountryCodeValid()
    val isCountryInvalidForSave = scope == SearchScope.SPECIFIC_COUNTRY && (targetCountry.isNullOrBlank() || !targetCountry.isCountryCodeValid())

    val isRadiusLockedForSave = displayRadius > SearchPreferences.PREMIUM_RADIUS_THRESHOLD_KM && !isRadiusUnlocked
    val isScopeLockedForSave = (scope == SearchScope.SPECIFIC_COUNTRY || scope == SearchScope.WORLD) && !isPassportUnlocked

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Titulo de la seccion
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    MiraiLinkText(
                        text = stringResource(R.string.search_settings_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    MiraiLinkText(
                        text = stringResource(R.string.search_settings_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // El minimapa aparece al volver a ajustar el radio local.
            AnimatedVisibility(visible = isMapVisible) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    SearchRadiusMinimap(
                        radiusKm = displayRadius,
                        onRadiusChange = { onRadiusChange(it.toFloat()) },
                        latitude = latitude,
                        longitude = longitude,
                        onRefreshLocation = onRefreshLocation,
                        isRefreshingLocation = isRefreshingLocation,
                        minRadiusKm = SearchPreferences.MIN_RADIUS_KM.toInt(),
                        maxRadiusKm = SearchPreferences.MAX_RADIUS_KM.toInt(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Slider de distancia en kilometros (10 km a 800 km)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkText(
                    text = stringResource(R.string.search_radius_label, displayRadius),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )

                if (displayRadius >= SearchPreferences.PREMIUM_RADIUS_THRESHOLD_KM) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isRadiusUnlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        MiraiLinkText(
                            text = if (isRadiusUnlocked) {
                                stringResource(R.string.search_premium_active_badge)
                            } else {
                                stringResource(R.string.search_premium_radius_future_note)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isRadiusUnlocked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }

            Slider(
                value = radiusKm,
                onValueChange = {
                    onRadiusChange(it)
                    onRequestLocationPermission?.invoke()
                },
                valueRange = SearchPreferences.MIN_RADIUS_KM..SearchPreferences.MAX_RADIUS_KM,
                steps = 78, // pasos de 10 km entre 10 y 800 km
                modifier = Modifier.fillMaxWidth(),
                enabled = scope.isRadiusScope(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MiraiLinkText(
                    text = "${SearchPreferences.MIN_RADIUS_KM.toInt()} km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                MiraiLinkText(
                    text = if (isRadiusUnlocked) "250 km (Plus/Premium)" else "250 km (Límite gratis)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                MiraiLinkText(
                    text = "${SearchPreferences.MAX_RADIUS_KM.toInt()} km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            AnimatedVisibility(visible = isRadiusLockedForSave) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
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
                                onClick = onNavigateToPaywall,
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

            Spacer(modifier = Modifier.height(16.dp))

            // País y Alcance de búsqueda
            MiraiLinkText(
                text = stringResource(R.string.search_scope_title),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                FilterChip(
                    selected = scope == SearchScope.RADIUS_RESIDENCE,
                    onClick = { onScopeChange(SearchScope.RADIUS_RESIDENCE) },
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_radius_residence)) },
                    colors = FilterChipDefaults.filterChipColors(),
                )
                FilterChip(
                    selected = scope == SearchScope.RADIUS_ACTIVE,
                    onClick = {
                        onScopeChange(SearchScope.RADIUS_ACTIVE)
                        onRequestLocationPermission?.invoke()
                    },
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_radius_active)) },
                    colors = FilterChipDefaults.filterChipColors(),
                )
                FilterChip(
                    selected = scope == SearchScope.MY_COUNTRY,
                    onClick = { onScopeChange(SearchScope.MY_COUNTRY) },
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_country)) },
                    colors = FilterChipDefaults.filterChipColors(),
                )
                FilterChip(
                    selected = scope == SearchScope.WORLD,
                    onClick = { onScopeChange(SearchScope.WORLD) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiraiLinkText(text = stringResource(R.string.search_scope_world))
                            if (!isPassportUnlocked) {
                                MiraiLinkText(
                                    text = " (Premium)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(),
                )
                FilterChip(
                    selected = scope == SearchScope.SPECIFIC_COUNTRY,
                    onClick = { onScopeChange(SearchScope.SPECIFIC_COUNTRY) },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            MiraiLinkText(text = stringResource(R.string.search_scope_passport))
                            if (!isPassportUnlocked) {
                                MiraiLinkText(
                                    text = " (Premium)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(),
                )
            }

            AnimatedVisibility(visible = isScopeLockedForSave) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.45f),
                    ),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        MiraiLinkText(
                            text = stringResource(R.string.search_passport_locked_disclaimer),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                        if (onNavigateToPaywall != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            MiraiLinkButton(
                                onClick = onNavigateToPaywall,
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

            // Entrada de código de país con validación regex en tiempo real
            AnimatedVisibility(visible = scope == SearchScope.SPECIFIC_COUNTRY) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    val countryValue = targetCountry ?: ""
                    MiraiLinkOutlinedTextField(
                        value = countryValue,
                        onValueChange = { input ->
                            // Filtrar solo letras mayúsculas de hasta 2 caracteres
                            val filtered = input.filter { it.isLetter() }.uppercase().take(2)
                            onTargetCountryChange(filtered)
                        },
                        label = stringResource(R.string.search_target_country_label),
                        modifier = Modifier.fillMaxWidth(),
                        isError = countryValue.isNotBlank() && !isCountryValid,
                        supportingText = if (countryValue.isNotBlank() && !isCountryValid) {
                            stringResource(R.string.search_invalid_country_code)
                        } else null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Boton de guardado explicito (deshabilitado si país es inválido o funciones bloqueadas)
            MiraiLinkButton(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && !isCountryInvalidForSave && !isRadiusLockedForSave && !isScopeLockedForSave,
                content = {
                    if (isSaving) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        MiraiLinkText(
                            text = if (hasUnsavedChanges) {
                                "${stringResource(R.string.search_settings_save)} *"
                            } else {
                                stringResource(R.string.search_settings_save)
                            },
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
private fun SearchSettingsSectionPreview() {
    com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme {
        SearchSettingsSection(
            radiusKm = 50f,
            onRadiusChange = {},
            scope = SearchScope.RADIUS_RESIDENCE,
            onScopeChange = {},
            targetCountry = null,
            onTargetCountryChange = {},
            hasUnsavedChanges = false,
            isSaving = false,
            onSaveClick = {},
        )
    }
}

