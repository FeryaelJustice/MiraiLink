package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.util.backendDateToMillis
import com.feryaeljustice.mirailink.domain.util.millisToBackendDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdateField(
    birthdateIso: String,
    onChange: (String) -> Unit
) {
    val initialMillis = birthdateIso.takeIf { it.isNotBlank() }?.let { backendDateToMillis(it) }
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )
    var open by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = birthdateIso,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(R.string.birthdate)) },
        trailingIcon = {
            IconButton(onClick = { open = true }) { Icon(Icons.Default.DateRange, null) }
        }
    )

    if (open) {
        DatePickerDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onChange(millisToBackendDate(millis)) // 👈 devuelve "yyyy-MM-dd"
                    }
                    open = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = {
                    open = false
                }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}