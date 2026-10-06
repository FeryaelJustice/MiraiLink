package com.feryaeljustice.mirailink.ui.components.capsule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkIconButton
import com.feryaeljustice.mirailink.ui.error.asString
import com.feryaeljustice.mirailink.ui.screens.home.search.CapsuleModeViewModel
import org.koin.compose.viewmodel.koinViewModel

/** Toolbar entry point; selections use the existing confirmed, reactive preference flow. */
@Composable
fun DiscoveryModeAction(viewModel: CapsuleModeViewModel = koinViewModel()) {
    val mode by viewModel.mode.collectAsStateWithLifecycle()
    val available by viewModel.available.collectAsStateWithLifecycle()
    val availabilityLoading by viewModel.availabilityLoading.collectAsStateWithLifecycle()
    val busy by viewModel.busy.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    var open by rememberSaveable { mutableStateOf(false) }
    var pendingMode by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(mode, busy, pendingMode) {
        if (pendingMode != null && mode == pendingMode && !busy) {
            open = false
            pendingMode = null
        }
    }

    MiraiLinkIconButton(onClick = {
        pendingMode = null
        viewModel.refresh()
        open = true
    }) {
        Icon(
            painter = painterResource(R.drawable.ic_discovery_modes),
            contentDescription = stringResource(R.string.discovery_mode_title),
            tint = if (mode == "capsule") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
    if (open) AlertDialog(
        onDismissRequest = { open = false; pendingMode = null },
        icon = { Icon(painterResource(R.drawable.ic_discovery_modes), contentDescription = null) },
        title = { Text(stringResource(R.string.discovery_mode_title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(Modifier.selectableGroup()) {
                    listOf("classic" to R.string.capsule_normal, "capsule" to R.string.capsule_title).forEach { (value, label) ->
                        val optionEnabled = !busy && (value == "classic" || (available && !availabilityLoading))
                        Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).selectable(selected = mode == value,
                            enabled = optionEnabled, role = Role.RadioButton,
                            onClick = {
                                if (mode == value) open = false
                                else {
                                    pendingMode = value
                                    viewModel.select(value)
                                }
                            }).padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = mode == value, onClick = null, enabled = optionEnabled)
                            Text(stringResource(label), Modifier.padding(start = 12.dp),
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (optionEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                Text(stringResource(R.string.capsule_intro), style = MaterialTheme.typography.bodySmall)
                if (!availabilityLoading && !available) {
                    Text(stringResource(R.string.capsule_unavailable), style = MaterialTheme.typography.bodySmall)
                }
                if (busy || availabilityLoading) CircularProgressIndicator(Modifier.size(24.dp))
                error?.let { Text(it.message.asString(), color = MaterialTheme.colorScheme.error) }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = { open = false; pendingMode = null }) { Text(stringResource(R.string.cancel)) } },
    )
}
