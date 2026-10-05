package com.feryaeljustice.mirailink.ui.screens.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R

/** Un solo objetivo tactil y una sola semantica para el interruptor de apariencia. */
@Composable
fun HoloProfileSetting(enabled: Boolean, onEnabledChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val state = stringResource(if (enabled) R.string.holo_state_enabled else R.string.holo_state_disabled)
    Card(modifier.fillMaxWidth().padding(horizontal = 20.dp).testTag("holoProfileSetting")) {
        Row(
            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp)
                .toggleable(enabled, role = Role.Switch, onValueChange = onEnabledChange)
                .semantics { stateDescription = state }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(R.string.settings_holo_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.settings_holo_subtitle), style = MaterialTheme.typography.bodyMedium)
            }
            Switch(checked = enabled, onCheckedChange = null)
        }
    }
}
