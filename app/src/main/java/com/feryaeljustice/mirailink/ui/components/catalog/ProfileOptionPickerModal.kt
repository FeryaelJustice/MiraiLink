package com.feryaeljustice.mirailink.ui.components.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSingleOptionPickerModal(
    title: String,
    options: List<CatalogItemOptionDto>,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState? = null,
    showSearch: Boolean = false,
) {
    val actualSheetState = sheetState ?: rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var searchQuery by remember { mutableStateOf("") }

    val filteredOptions = remember(searchQuery, options) {
        if (searchQuery.isBlank()) {
            options
        } else {
            val query = searchQuery.trim().lowercase()
            options.filter {
                val text = it.label ?: it.question ?: it.code
                text.lowercase().contains(query)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = actualSheetState,
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.85f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkText(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                    )
                }
            }

            if (showSearch || options.size >= 5) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { MiraiLinkText(text = stringResource(R.string.interest_picker_search_hint)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.interest_picker_clear_search),
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                // Clear selection option
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(null)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedId == null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = selectedId == null,
                                onClick = {
                                    onSelect(null)
                                    onDismiss()
                                },
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            MiraiLinkText(
                                text = stringResource(R.string.profile_option_not_specified),
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (selectedId == null) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }

                items(filteredOptions, key = { it.id }) { option ->
                    val isSelected = option.id == selectedId
                    val optionLabel = option.label ?: option.question ?: option.code
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelect(option.id)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onSelect(option.id)
                                    onDismiss()
                                },
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            MiraiLinkText(
                                text = optionLabel,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileMultiOptionPickerModal(
    title: String,
    options: List<CatalogItemOptionDto>,
    selectedIds: List<String>,
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState? = null,
    showSearch: Boolean = false,
) {
    val actualSheetState = sheetState ?: rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val localSelected = remember { mutableStateListOf(*selectedIds.toTypedArray()) }
    fun isIndeterminateOption(opt: CatalogItemOptionDto): Boolean {
        val codeLower = opt.code.lowercase()
        val labelLower = (opt.label ?: opt.question ?: "").lowercase()
        return codeLower.contains("not_sure") ||
            codeLower.contains("undecided") ||
            codeLower == "none" ||
            labelLower.contains("no lo tengo claro") ||
            labelLower.contains("no sé si quiero") ||
            labelLower.contains("no se si quiero") ||
            labelLower.contains("todavía no lo sé") ||
            labelLower.contains("todavia no lo se")
    }

    var searchQuery by remember { mutableStateOf("") }

    fun handleToggleOption(option: CatalogItemOptionDto) {
        val isCurrentlyChecked = localSelected.contains(option.id)
        if (isCurrentlyChecked) {
            localSelected.remove(option.id)
        } else {
            if (isIndeterminateOption(option)) {
                // Si marcamos "No lo tengo claro aún", desmarcamos todas las demás opciones
                localSelected.clear()
                localSelected.add(option.id)
            } else {
                // Si marcamos una opción concreta, quitamos cualquier opción indeterminada ("No lo tengo claro")
                val indeterminateIds = options.filter { isIndeterminateOption(it) }.map { it.id }.toSet()
                localSelected.removeAll(indeterminateIds)
                localSelected.add(option.id)
            }
        }
    }

    val filteredOptions = remember(searchQuery, options) {
        if (searchQuery.isBlank()) {
            options
        } else {
            val query = searchQuery.trim().lowercase()
            options.filter {
                val text = it.label ?: it.question ?: it.code
                text.lowercase().contains(query)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = actualSheetState,
        dragHandle = null,
        modifier = Modifier.fillMaxHeight(0.85f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MiraiLinkText(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                    )
                }
            }

            if (showSearch || options.size >= 5) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { MiraiLinkText(text = stringResource(R.string.interest_picker_search_hint)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.interest_picker_clear_search),
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(filteredOptions, key = { it.id }) { option ->
                    val isChecked = localSelected.contains(option.id)
                    val optionLabel = option.label ?: option.question ?: option.code
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                handleToggleOption(option)
                            },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isChecked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { _ ->
                                    handleToggleOption(option)
                                },
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            MiraiLinkText(
                                text = optionLabel,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                color = if (isChecked) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (localSelected.isNotEmpty()) {
                    MiraiLinkOutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { localSelected.clear() },
                    ) {
                        MiraiLinkText(text = stringResource(R.string.profile_option_clear))
                    }
                }
                MiraiLinkButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onConfirm(localSelected.toList())
                        onDismiss()
                    },
                ) {
                    MiraiLinkText(text = stringResource(R.string.interest_picker_confirm))
                }
            }
        }
    }
}
