package com.feryaeljustice.mirailink.ui.components.capsule

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun CapsulePanel(state: CrystalCapsule, ownId: String?, busy: Boolean, isDemo: Boolean,
    onAction: (String, String?, String?) -> Unit) {
    var expanded by rememberSaveable(state.id) { mutableStateOf(false) }
    val language = LocalConfiguration.current.locales[0].language
    var answer by rememberSaveable(state.question?.instanceId) { mutableStateOf("") }
    val titles = listOf(R.string.capsule_level_0, R.string.capsule_level_1, R.string.capsule_level_2, R.string.capsule_level_3, R.string.capsule_level_4)
    Surface(color = MaterialTheme.colorScheme.secondaryContainer, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f)) {
                Text(stringResource(titles[state.level.coerceIn(0,4)]), style = MaterialTheme.typography.labelLarge)
                val statusLabel = when(state.status) {
                    "paused" -> R.string.capsule_status_paused
                    "left" -> R.string.capsule_status_left
                    "cancelled" -> R.string.capsule_status_cancelled
                    "revealed" -> R.string.capsule_status_revealed
                    else -> R.string.capsule_status_active
                }
                Text(stringResource(R.string.capsule_progress, state.progress) + " · " + stringResource(statusLabel),
                    style = MaterialTheme.typography.labelSmall)
                LinearProgressIndicator(progress = { state.progress / 8f }, modifier = Modifier.fillMaxWidth())
            }
            TextButton(onClick = { expanded = true }, colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer)) { Text(stringResource(R.string.capsule_missions)) }
        }
    }
    if(expanded) AlertDialog(onDismissRequest = { expanded = false }, title = { Text(stringResource(R.string.capsule_title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.capsule_help))
                if(isDemo) Text(stringResource(R.string.capsule_demo))
                state.question?.let { question ->
                    Text(if(language == "es") question.es else question.en)
                    if(state.status == "active" && !question.completed && ownId !in question.answeredBy) {
                        OutlinedTextField(value = answer, onValueChange = { answer = it.take(4000) }, label = { Text(stringResource(R.string.capsule_answer)) })
                        TextButton(enabled = !busy && answer.isNotBlank(), onClick = { onAction("answer", null, answer) }) { Text(stringResource(R.string.send)) }
                    } else Text(stringResource(if(question.completed) R.string.capsule_mission_complete else R.string.capsule_waiting))
                }
                if(state.status == "active") {
                    val categories = listOf("anime", "gaming", "hobbies", "everyday", "ideal_date", "projects", "relationships", "family")
                    val labels = listOf(R.string.capsule_anime, R.string.capsule_gaming, R.string.capsule_hobbies, R.string.capsule_everyday,
                        R.string.capsule_ideal_date, R.string.capsule_projects, R.string.capsule_relationships, R.string.capsule_family)
                    Text(stringResource(R.string.capsule_choose_category))
                    categories.forEachIndexed { index, category ->
                        TextButton(enabled = !busy, onClick = { onAction("question", category, null) }) { Text(stringResource(labels[index])) }
                    }
                    TextButton(enabled = !busy, onClick = { onAction("pause", null, null) }) { Text(stringResource(R.string.capsule_pause)) }
                } else if(state.status in listOf("paused", "left")) {
                    TextButton(enabled = !busy && ownId !in state.resumeAccepted, onClick = { onAction("resume", null, null) }) { Text(stringResource(R.string.capsule_resume)) }
                    if(ownId in state.resumeAccepted) Text(stringResource(R.string.capsule_waiting))
                }
                if(state.status != "revealed") {
                    when(state.revealRequestedBy) {
                        null -> TextButton(enabled = !busy, onClick = { onAction("request_reveal", null, null) }) { Text(stringResource(R.string.capsule_request_reveal)) }
                        ownId -> TextButton(enabled = !busy, onClick = { onAction("cancel_reveal", null, null) }) { Text(stringResource(R.string.capsule_cancel_reveal)) }
                        else -> {
                            Text(stringResource(R.string.capsule_reveal_offer))
                            TextButton(enabled = !busy, onClick = { onAction("accept_reveal", null, null) }) { Text(stringResource(R.string.capsule_accept_reveal)) }
                            TextButton(enabled = !busy, onClick = { onAction("decline_reveal", null, null) }) { Text(stringResource(R.string.capsule_decline)) }
                        }
                    }
                    TextButton(enabled = !busy && state.status != "cancelled", onClick = { onAction("leave", null, null) }) { Text(stringResource(R.string.capsule_leave)) }
                }
            }
        }, confirmButton = { TextButton(onClick = { expanded = false }) { Text(stringResource(R.string.close)) } })
}
