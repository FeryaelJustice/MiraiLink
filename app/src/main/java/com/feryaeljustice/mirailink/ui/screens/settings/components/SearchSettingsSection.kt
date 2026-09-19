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
import androidx.compose.material3.Switch
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
    radiusKm: Float,
    onRadiusChange: (Float) -> Unit,
    scope: SearchScope,
    onScopeChange: (SearchScope) -> Unit,
    targetCountry: String?,
    onTargetCountryChange: (String?) -> Unit,
    matchLiveLocation: Boolean,
    onMatchLiveLocationChange: (Boolean) -> Unit,
    hasUnsavedChanges: Boolean,
    isSaving: Boolean,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    latitude: Double = 39.5696,
    longitude: Double = 2.6502,
    isMapVisible: Boolean = false,
    onRequestLocationPermission: (() -> Unit)? = null,
) {
    val displayRadius = radiusKm.toInt()
    val isCountryValid = targetCountry.isNullOrBlank() || targetCountry.isCountryCodeValid()
    val isCountryInvalidForSave = scope == SearchScope.SPECIFIC_COUNTRY && (targetCountry.isNullOrBlank() || !targetCountry.isCountryCodeValid())

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

            // Minimapa interactivo: solo visible si el usuario interactua o hace scroll
            AnimatedVisibility(visible = isMapVisible) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    SearchRadiusMinimap(
                        radiusKm = displayRadius,
                        onRadiusChange = { onRadiusChange(it.toFloat()) },
                        latitude = latitude,
                        longitude = longitude,
                        minRadiusKm = SearchPreferences.MIN_RADIUS_KM.toInt(),
                        maxRadiusKm = SearchPreferences.MAX_RADIUS_KM.toInt(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Slider de distancia en kilometros (10 km a 300 km)
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

                // Preparación para el futuro modelo Premium: a partir de 150 km
                if (displayRadius >= SearchPreferences.PREMIUM_RADIUS_THRESHOLD_KM) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        MiraiLinkText(
                            text = stringResource(R.string.search_premium_radius_future_note),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                steps = 28, // pasos aproximados de 10 km
                modifier = Modifier.fillMaxWidth(),
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
                    text = "150 km (Premium futuro)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                MiraiLinkText(
                    text = "${SearchPreferences.MAX_RADIUS_KM.toInt()} km",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
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
                    selected = scope == SearchScope.RADIUS,
                    onClick = {
                        onScopeChange(SearchScope.RADIUS)
                        onRequestLocationPermission?.invoke()
                    },
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_local)) },
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
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_world)) },
                    colors = FilterChipDefaults.filterChipColors(),
                )
                FilterChip(
                    selected = scope == SearchScope.SPECIFIC_COUNTRY,
                    onClick = { onScopeChange(SearchScope.SPECIFIC_COUNTRY) },
                    label = { MiraiLinkText(text = stringResource(R.string.search_scope_passport)) },
                    colors = FilterChipDefaults.filterChipColors(),
                )
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

            Spacer(modifier = Modifier.height(16.dp))

            // Switch para viajeros
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MiraiLinkText(
                            text = stringResource(R.string.search_match_travelers_title),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            MiraiLinkText(
                                text = "Beta",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    MiraiLinkText(
                        text = stringResource(R.string.search_match_travelers_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = matchLiveLocation,
                    onCheckedChange = {
                        onMatchLiveLocationChange(it)
                        if (it) onRequestLocationPermission?.invoke()
                    },
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Boton de guardado explicito (deshabilitado o bloqueado si el código de país es inválido)
            MiraiLinkButton(
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSaving && !isCountryInvalidForSave,
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
