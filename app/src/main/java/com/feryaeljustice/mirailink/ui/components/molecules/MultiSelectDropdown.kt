package com.feryaeljustice.mirailink.ui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedTextField
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

data class MultiSelectOption(val id: String, val label: String)

@Suppress("ktlint:standard:function-naming")
@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<MultiSelectOption>,
    selected: List<String>,
    onSelectionChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    var columnSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    columnSize = layoutCoordinates.size.toSize()
                },
    ) {
        MiraiLinkOutlinedTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
            value = options.filter { it.id in selected }.joinToString(", ") { it.label },
            onValueChange = {},
            label = label,
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.expand),
                    modifier =
                        Modifier.clickable {
                            expanded = true
                        },
                )
            },
        )

        // Chips debajo (más usabilidad)
        if (selected.isNotEmpty()) {
            FlowRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                options.filter { it.id in selected }.forEach { option ->
                    AssistChip(
                        onClick = {
                            onSelectionChange(selected - option.id)
                        },
                        label = { MiraiLinkText(text = option.label) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 8.dp, bottom = 4.dp),
                        colors =
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                    )
                }
            }
        }

        // Dropdown de selección
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier =
                Modifier
                    .width(
                        with(LocalDensity.current) {
                            columnSize.width.toDp()
                        },
                    ).fillMaxHeight(fraction = 0.6f),
        ) {
            options.forEach { option ->
                val isSelected = option.id in selected
                DropdownMenuItem(
                    text = {
                        MiraiLinkText(
                            text = option.label,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    onClick = {
                        val newSelection =
                            if (isSelected) {
                                selected - option.id
                            } else {
                                selected + option.id
                            }
                        onSelectionChange(newSelection)
                    },
                    trailingIcon = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(R.string.selectedd),
                            )
                        }
                    },
                )
            }
        }
    }
}
