package com.feryaeljustice.mirailink.ui.components.capsule

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R

@Composable
fun DiscoveryModeSelector(mode: String, available: Boolean, enabled: Boolean = true, onSelect: (String) -> Unit) {
    var showHelp by remember { mutableStateOf(false) }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(selected = mode == "classic", onClick = { onSelect("classic") }, enabled = enabled,
            label = { Text(stringResource(R.string.capsule_normal)) })
        FilterChip(selected = mode == "capsule", onClick = { showHelp = true }, enabled = enabled,
            label = { Text(stringResource(R.string.capsule_title)) })
    }
    if(showHelp) AlertDialog(onDismissRequest = { showHelp = false }, title = { Text(stringResource(R.string.capsule_title)) },
        text = { Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(R.string.capsule_intro))
            if (!available) Text(stringResource(R.string.capsule_unavailable))
        } },
        confirmButton = { TextButton(enabled = available, onClick = { showHelp = false; onSelect("capsule") }) { Text(stringResource(R.string.capsule_enter)) } },
        dismissButton = { TextButton(onClick = { showHelp = false }) { Text(stringResource(R.string.cancel)) } })
}
