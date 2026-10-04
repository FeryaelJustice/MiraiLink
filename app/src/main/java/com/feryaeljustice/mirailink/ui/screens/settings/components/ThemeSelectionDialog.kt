package com.feryaeljustice.mirailink.ui.screens.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.settings.ThemePreference
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText

@Composable
fun ThemeSelectionDialog(
    selectedTheme: ThemePreference,
    onThemeSelected: (ThemePreference) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = {
            MiraiLinkText(
                text = stringResource(R.string.settings_theme_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                ThemePreference.entries.forEach { theme ->
                    val isSelected = theme == selectedTheme
                    val label = when (theme) {
                        ThemePreference.SYSTEM -> stringResource(R.string.settings_theme_system)
                        ThemePreference.LIGHT -> stringResource(R.string.settings_theme_light)
                        ThemePreference.DARK -> stringResource(R.string.settings_theme_dark)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = {
                                    onThemeSelected(theme)
                                    onDismissRequest()
                                },
                                role = Role.RadioButton,
                            )
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null,
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        MiraiLinkText(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                MiraiLinkText(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
    )
}
