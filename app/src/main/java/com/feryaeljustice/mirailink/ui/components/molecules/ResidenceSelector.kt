package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Modifier
import androidx.compose.runtime.produceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.location.Geocoder
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.domain.enums.TextFieldType
import com.feryaeljustice.mirailink.R
import androidx.compose.ui.res.stringResource
import java.text.Normalizer
import java.util.Locale

private data class CountryOption(val code: String, val name: String)
private data class PlaceOption(val name: String, val latitude: Double, val longitude: Double)

private fun countryOptions(): List<CountryOption> =
    Locale.getISOCountries()
        .map { code -> CountryOption(code, Locale("", code).getDisplayCountry(Locale.getDefault())) }
        .filter { it.name.isNotBlank() }
        .sortedBy { it.name }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidenceSelector(
    country: String,
    countryCode: String,
    region: String,
    city: String,
    onValueChange: (TextFieldType, String) -> Unit,
    onCoordinatesSelected: (Double, Double) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    val countries = remember { countryOptions() }
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_country),
            value = country,
            enabled = true,
            options = countries.rankFor(country),
            optionLabel = { it.name },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_COUNTRY, it) },
            onSelect = { option -> onValueChange(TextFieldType.RESIDENCE_COUNTRY, option.name) },
        )

        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_region),
            value = region,
            enabled = countryCode.isNotBlank(),
            options = geocoderSuggestions(region, country, "region"),
            optionLabel = { it.name },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_REGION, it) },
            onSelect = { option -> onValueChange(TextFieldType.RESIDENCE_REGION, option.name) },
        )

        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_city),
            value = city,
            enabled = countryCode.isNotBlank() && region.isNotBlank(),
            options = geocoderSuggestions(city, "$region, $country", "city"),
            optionLabel = { it.name },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_CITY, it) },
            onSelect = { option ->
                onValueChange(TextFieldType.RESIDENCE_CITY, option.name)
                onCoordinatesSelected(option.latitude, option.longitude)
            },
        )
    }
}

@Composable
private fun geocoderSuggestions(query: String, contextText: String, kind: String): List<PlaceOption> {
    val context = LocalContext.current
    val result by produceState(initialValue = emptyList(), query, contextText, kind) {
        value = if (query.length < 2 || !Geocoder.isPresent()) {
            emptyList()
        } else {
            withContext(Dispatchers.IO) {
                runCatching {
                    @Suppress("DEPRECATION")
                    Geocoder(context).getFromLocationName("$query, $contextText", 8)
                        ?.mapNotNull { address ->
                            val name = if (kind == "region") address.adminArea else address.locality
                            name?.takeIf { it.isNotBlank() }?.let {
                                PlaceOption(it, address.latitude, address.longitude)
                            }
                        }
                        ?.distinctBy { it.name }
                        .orEmpty()
                }.getOrDefault(emptyList())
            }
        }
    }
    return result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> ResidenceAutocompleteField(
    label: String,
    value: String,
    enabled: Boolean,
    options: List<T>,
    optionLabel: (T) -> String,
    onValueChange: (String) -> Unit,
    onSelect: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var hasFocus by remember { mutableStateOf(false) }
    val visibleOptions = options.take(8)
    val commitClosestOption = {
        closestOption(value, options, optionLabel)
            ?.takeIf { optionLabel(it) != value }
            ?.let(onSelect)
    }

    ExposedDropdownMenuBox(
        expanded = enabled && expanded && visibleOptions.isNotEmpty(),
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
                if (!expanded) commitClosestOption()
            }
        },
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            enabled = enabled,
            label = { Text(label) },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                    .onFocusChanged { focusState ->
                        if (hasFocus && !focusState.isFocused) commitClosestOption()
                        hasFocus = focusState.isFocused
                    },
        )
        ExposedDropdownMenu(
            expanded = enabled && expanded && visibleOptions.isNotEmpty(),
            onDismissRequest = {
                expanded = false
                commitClosestOption()
            },
        ) {
            visibleOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun <T> closestOption(
    value: String,
    options: List<T>,
    optionLabel: (T) -> String,
): T? {
    val normalizedValue = value.normalizedResidenceText()
    if (normalizedValue.length < 2) return null

    return options
        .map { option -> option to optionLabel(option).normalizedResidenceText() }
        .map { (option, label) ->
            val distance = levenshteinDistance(normalizedValue, label)
            val similarity = 1f - distance.toFloat() / maxOf(normalizedValue.length, label.length)
            Triple(option, label, similarity)
        }
        .filter { (_, label, similarity) ->
            label.startsWith(normalizedValue) ||
                label.contains(normalizedValue) ||
                similarity >= 0.6f
        }
        .maxByOrNull { (_, label, similarity) ->
            if (label.startsWith(normalizedValue)) similarity + 1f else similarity
        }
        ?.first
}

private fun List<CountryOption>.rankFor(query: String): List<CountryOption> {
    val normalizedQuery = query.normalizedResidenceText()
    if (normalizedQuery.isBlank()) return this

    return sortedWith(
        compareByDescending<CountryOption> { option ->
            val normalizedName = option.name.normalizedResidenceText()
            when {
                normalizedName.startsWith(normalizedQuery) -> 3f
                normalizedName.contains(normalizedQuery) -> 2f
                else -> 1f - levenshteinDistance(normalizedQuery, normalizedName).toFloat() /
                    maxOf(normalizedQuery.length, normalizedName.length)
            }
        }.thenBy { it.name },
    )
}

private fun String.normalizedResidenceText(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{M}+".toRegex(), "")
        .lowercase(Locale.ROOT)
        .trim()

private fun levenshteinDistance(first: String, second: String): Int {
    var previous = IntArray(second.length + 1) { it }
    first.forEachIndexed { firstIndex, firstCharacter ->
        val current = IntArray(second.length + 1)
        current[0] = firstIndex + 1
        second.forEachIndexed { secondIndex, secondCharacter ->
            current[secondIndex + 1] = minOf(
                current[secondIndex] + 1,
                previous[secondIndex + 1] + 1,
                previous[secondIndex] + if (firstCharacter == secondCharacter) 0 else 1,
            )
        }
        previous = current
    }
    return previous.last()
}
