package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.domain.model.geography.GeographicPlace
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult
import org.koin.compose.koinInject
import java.text.Normalizer
import java.util.Locale

@Composable
fun ResidenceSelector(
    countryId: String,
    country: String,
    regionId: String,
    region: String,
    city: String,
    onPlaceSelected: (TextFieldType, GeographicPlace) -> Unit,
    onTextChanged: (TextFieldType, String) -> Unit,
    onClear: (TextFieldType) -> Unit,
    modifier: Modifier = Modifier,
    catalogRepository: CatalogRepository = koinInject(),
) {
    val countries by produceState(emptyList(), catalogRepository) {
        value = catalogRepository.getCountries().orEmpty()
    }
    val regions by produceState(emptyList(), countryId, catalogRepository) {
        value = countryId.takeIf { it.isNotBlank() }?.let {
            catalogRepository.getRegions(it).orEmpty()
        }.orEmpty()
    }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_country), value = country,
            enabled = true, options = countries,
            resetKey = "country",
            onTextChanged = { onTextChanged(TextFieldType.RESIDENCE_COUNTRY, it) },
            onClear = { onClear(TextFieldType.RESIDENCE_COUNTRY) },
            onSelect = { onPlaceSelected(TextFieldType.RESIDENCE_COUNTRY, it) },
        )
        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_region), value = region,
            enabled = countryId.isNotBlank(), options = regions,
            resetKey = countryId,
            onTextChanged = { onTextChanged(TextFieldType.RESIDENCE_REGION, it) },
            onClear = { onClear(TextFieldType.RESIDENCE_REGION) },
            onSelect = { onPlaceSelected(TextFieldType.RESIDENCE_REGION, it) },
        )
        CityAutocompleteField(
            regionId = regionId,
            city = city,
            catalogRepository = catalogRepository,
            onTextChanged = { onTextChanged(TextFieldType.RESIDENCE_CITY, it) },
            onClear = { onClear(TextFieldType.RESIDENCE_CITY) },
            onSelect = {
                onPlaceSelected(TextFieldType.RESIDENCE_CITY, it)
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResidenceAutocompleteField(
    label: String,
    value: String,
    enabled: Boolean,
    options: List<GeographicPlace>,
    resetKey: String,
    onTextChanged: (String) -> Unit,
    onClear: () -> Unit,
    onSelect: (GeographicPlace) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var query by remember(resetKey, value) { mutableStateOf(value) }
    val normalizedQuery = query.trim().normalizeResidenceSearch()
    val matchingOptions =
        options.distinctBy { it.id }.distinctBy { it.name.normalizeResidenceSearch() }
            .filter { option ->
                normalizedQuery.isBlank() || option.name.lowercase().contains(normalizedQuery) ||
                        option.aliases.any { it.contains(normalizedQuery) }
            }
    ExposedDropdownMenuBox(
        expanded = enabled && expanded && matchingOptions.isNotEmpty(),
        onExpandedChange = { if (enabled) expanded = !expanded },
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; onTextChanged(it); expanded = true },
            readOnly = false,
            enabled = enabled,
            label = { Text(label) },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = {
                    query = ""; onClear(); expanded = false
                }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                } else ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(
            expanded = enabled && expanded && matchingOptions.isNotEmpty(),
            onDismissRequest = { expanded = false },
        ) {
            matchingOptions.take(25).forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        query = option.name
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun String.normalizeResidenceSearch(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "")
        .lowercase(Locale.ROOT)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CityAutocompleteField(
    regionId: String,
    city: String,
    catalogRepository: CatalogRepository,
    onTextChanged: (String) -> Unit,
    onClear: () -> Unit,
    onSelect: (GeographicPlace) -> Unit,
) {
    var query by remember(regionId, city) { mutableStateOf(city) }
    var expanded by remember { mutableStateOf(false) }
    val cities by produceState(emptyList(), regionId, query, catalogRepository) {
        value = if (regionId.isNotBlank() && query.trim().length >= 2) {
            catalogRepository.getCities(regionId, query.trim()).orEmpty().distinctBy { it.id }
        } else emptyList()
    }
    ExposedDropdownMenuBox(
        expanded = expanded && cities.isNotEmpty(),
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it; onTextChanged(it); expanded = true },
            enabled = regionId.isNotBlank(),
            label = { Text(stringResource(R.string.profile_residence_city)) },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) IconButton(onClick = {
                    query = ""; onClear(); expanded = false
                }) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                } else ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        )
        ExposedDropdownMenu(
            expanded = expanded && cities.isNotEmpty(),
            onDismissRequest = { expanded = false }) {
            cities.forEach { option ->
                DropdownMenuItem(text = { Text(option.name) }, onClick = {
                    query = option.name
                    onSelect(option)
                    expanded = false
                })
            }
        }
    }
}

private fun MiraiLinkResult<List<GeographicPlace>>.orEmpty(): List<GeographicPlace> =
    (this as? MiraiLinkResult.Success)?.data.orEmpty()
