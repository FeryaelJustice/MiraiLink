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
import java.util.Locale

private data class CountryOption(val code: String, val name: String)

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
    modifier: Modifier = Modifier,
) {
    val countries = remember { countryOptions() }
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_country),
            value = country,
            enabled = true,
            options = countries.filter { it.name.contains(country, ignoreCase = true) },
            optionLabel = { it.name },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_COUNTRY, it) },
            onSelect = { option -> onValueChange(TextFieldType.RESIDENCE_COUNTRY, option.name) },
        )

        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_region),
            value = region,
            enabled = countryCode.isNotBlank(),
            options = geocoderSuggestions(region, country, "region"),
            optionLabel = { it },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_REGION, it) },
            onSelect = {},
        )

        ResidenceAutocompleteField(
            label = stringResource(R.string.profile_residence_city),
            value = city,
            enabled = countryCode.isNotBlank() && region.isNotBlank(),
            options = geocoderSuggestions(city, "$region, $country", "city"),
            optionLabel = { it },
            onValueChange = { onValueChange(TextFieldType.RESIDENCE_CITY, it) },
            onSelect = {},
        )
    }
}

@Composable
private fun geocoderSuggestions(query: String, contextText: String, kind: String): List<String> {
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
                            if (kind == "region") address.adminArea else address.locality
                        }
                        ?.filter { it.isNotBlank() }
                        ?.distinct()
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
    val visibleOptions = options.take(8)

    ExposedDropdownMenuBox(
        expanded = enabled && expanded && visibleOptions.isNotEmpty(),
        onExpandedChange = { if (enabled) expanded = !expanded },
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
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
        )
        ExposedDropdownMenu(
            expanded = enabled && expanded && visibleOptions.isNotEmpty(),
            onDismissRequest = { expanded = false },
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
