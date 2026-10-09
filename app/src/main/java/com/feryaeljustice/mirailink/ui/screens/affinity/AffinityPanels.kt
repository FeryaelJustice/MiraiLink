package com.feryaeljustice.mirailink.ui.screens.affinity

import androidx.compose.foundation.clickable
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Info
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.feryaeljustice.mirailink.BuildConfig
import com.feryaeljustice.mirailink.domain.util.resolvePhotoUrl
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LifecycleResumeEffect
import coil.compose.AsyncImage
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.affinity.AffinityPerson
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkErrorContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun AffinityAvatar(person: AffinityPerson?, size: androidx.compose.ui.unit.Dp = 44.dp) {
    Surface(Modifier.size(size), shape = CircleShape, color = MaterialTheme.colorScheme.secondaryContainer) {
        if (person != null) AsyncImage(
            model = person.avatarUrl?.takeIf { it.isNotBlank() }?.let { resolvePhotoUrl(BuildConfig.MIRAILINK_BASE_URL, it) }, contentDescription = null,
            placeholder = painterResource(R.drawable.logomirailink),
            fallback = painterResource(R.drawable.logomirailink),
            error = painterResource(R.drawable.logomirailink),
            contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize().clip(CircleShape),
        ) else Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun AffinityPersonCard(person: AffinityPerson?, onDetail: (String) -> Unit, content: @Composable ColumnScope.() -> Unit) {
    OutlinedCard(Modifier.width(236.dp), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                Modifier.fillMaxWidth().then(if (person != null) Modifier.clickable { onDetail(person.username) } else Modifier).heightIn(min = 48.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                AffinityAvatar(person)
                Text(
                    text = person?.nickname?.ifBlank { person.username } ?: stringResource(R.string.affinity_anonymous),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
            content()
        }
    }
}

@Composable
fun AffinitySection(onDetail: (String) -> Unit, onPaywall: () -> Unit, viewModel: AffinityViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFaqDialog by rememberSaveable { mutableStateOf(false) }
    LifecycleResumeEffect(viewModel) { viewModel.refresh(); onPauseOrDispose {} }
    Column(Modifier.fillMaxWidth().animateContentSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (state.feed.enabled || state.likes.isNotEmpty() || state.requests.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(stringResource(R.string.affinity_title), style = MaterialTheme.typography.titleLarge)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (state.isDemo) {
                        IconButton(onClick = viewModel::resetDemo) {
                            Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.affinity_reset_demo))
                        }
                    }
                    IconButton(onClick = { showFaqDialog = true }) {
                        Icon(Icons.Outlined.Info, contentDescription = stringResource(R.string.affinity_info_title))
                    }
                }
            }
            if (state.feed.enabled || state.requests.any { !it.incoming }) {
                Text(stringResource(R.string.affinity_discover_title), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.affinity_explanation), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (state.feed.enabled && state.feed.items.isEmpty()) Text(stringResource(if (state.feed.participating) R.string.affinity_empty else R.string.affinity_disabled), style = MaterialTheme.typography.bodySmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.feed.items, key = { it.id }) { row ->
                        AffinityPersonCard(row.person, onDetail) {
                            Text(row.commonInterests.joinToString(" · "), style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                            if (row.person == null) Button(onClick = onPaywall, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.affinity_unlock)) }
                            else Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                FilledTonalIconButton(onClick = { viewModel.like(row.id) }, enabled = !state.busy && row.state != "liked") {
                                    Icon(Icons.Default.Favorite, contentDescription = stringResource(if (row.state == "liked") R.string.affinity_like_sent else R.string.affinity_like))
                                }
                                OutlinedButton(onClick = { viewModel.select(row) }, enabled = !state.busy, modifier = Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 8.dp)) { Text(stringResource(R.string.affinity_invite)) }
                                IconButton(onClick = { viewModel.dismiss(row.id) }, enabled = !state.busy) { Icon(Icons.Default.Close, contentDescription = stringResource(R.string.affinity_dismiss)) }
                            }
                        }
                    }
                }
                AffinitySentRequestsPanel(onDetail = onDetail, viewModel = viewModel)
            }
            if (state.likes.isNotEmpty() || state.requests.any { it.incoming }) {
                HorizontalDivider()
                Text(stringResource(R.string.affinity_likes_title), style = MaterialTheme.typography.titleMedium)
                if (state.likes.isNotEmpty()) {
                    Text(stringResource(R.string.affinity_likes_explanation), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.likes, key = { it.id }) { row ->
                            AffinityPersonCard(row.person, onDetail) {
                                if (row.person == null) Button(onClick = onPaywall, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.affinity_unlock)) }
                                else FilledTonalButton(onClick = { viewModel.returnLike(row.id) }, enabled = !state.busy, modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(Modifier.width(6.dp)); Text(stringResource(R.string.received_likes_match_button))
                                }
                            }
                        }
                    }
                }
                AffinityRequestsPanel(onDetail = onDetail, onPaywall = onPaywall, viewModel = viewModel, incoming = true)
            }
        }
        if (state.canLoadMore) TextButton(onClick = viewModel::moreRequests, enabled = !state.busy) { Text(stringResource(R.string.affinity_more)) }
        state.error?.let { MiraiLinkErrorContent(error = it, onAction = viewModel::refresh) }
        if (state.loading || state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
    }
    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            title = { Text(stringResource(R.string.affinity_info_title)) },
            text = {
                Text(
                    stringResource(R.string.affinity_info_desc),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                TextButton(onClick = { showFaqDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            },
        )
    }
    AffinityReportDialog(viewModel)
    state.selected?.let { selected ->
        AlertDialog(
            modifier = Modifier.imePadding(),
            onDismissRequest = { if (!state.busy) viewModel.select(null) },
            title = { Text(stringResource(R.string.affinity_invite)) },
            text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(selected.person?.nickname.orEmpty())
                Text(stringResource(R.string.affinity_invitation_warning))
                OutlinedTextField(state.draft, viewModel::editDraft, enabled = !state.busy, label = { Text(stringResource(R.string.affinity_first_message)) }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                state.error?.let { MiraiLinkErrorContent(error = it, onAction = viewModel::send) }
            } },
            confirmButton = { TextButton(onClick = viewModel::send, enabled = !state.busy && state.draft.isNotBlank() && state.draft.length <= 4000) { Text(stringResource(R.string.affinity_send)) } },
            dismissButton = { TextButton(onClick = { viewModel.select(null) }, enabled = !state.busy) { Text(stringResource(R.string.cancel)) } },
        )
    }
}

@Composable
private fun AffinitySentInvitationCard(
    request: com.feryaeljustice.mirailink.domain.model.affinity.AffinityRequest,
    onDetail: (String) -> Unit,
) {
    OutlinedCard(
        modifier = Modifier
            .width(184.dp)
            .clickable { onDetail(request.person.username) },
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            AffinityAvatar(request.person, size = 48.dp)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = request.person.nickname.ifBlank { request.person.username },
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val isMessage = request.text.isNotBlank() && request.text != "🎮"
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = if (isMessage) Icons.Default.Email else Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp),
                        )
                        Text(
                            text = stringResource(if (isMessage) R.string.affinity_method_message else R.string.affinity_method_like),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AffinitySentRequestsPanel(onDetail: (String) -> Unit, viewModel: AffinityViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val requests = state.requests.filter { !it.incoming }
    if (requests.isNotEmpty()) Column(Modifier.fillMaxWidth().animateContentSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.affinity_requests_sent), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(requests, key = { it.id }) { request ->
                AffinitySentInvitationCard(request = request, onDetail = onDetail)
            }
        }
    }
}

@Composable
private fun AffinityRequestsPanel(onDetail: (String) -> Unit, onPaywall: () -> Unit, viewModel: AffinityViewModel, incoming: Boolean) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val requests = state.requests.filter { it.incoming == incoming }
    if (requests.isNotEmpty()) Column(Modifier.fillMaxWidth().animateContentSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(stringResource(if (incoming) R.string.affinity_requests_title else R.string.affinity_requests_sent), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        for (request in requests) key(request.id) {
            var menu by remember { mutableStateOf(false) }
            var expanded by rememberSaveable(request.id) { mutableStateOf(false) }
            val isLocked = incoming && !state.isPlus
            OutlinedCard(Modifier.fillMaxWidth().animateContentSize(), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        AffinityAvatar(if (isLocked) null else request.person, 40.dp)
                        Text(
                            text = if (isLocked) stringResource(R.string.affinity_anonymous) else request.person.nickname.ifBlank { request.person.username },
                            modifier = Modifier
                                .weight(1f)
                                .then(if (!isLocked) Modifier.clickable { onDetail(request.person.username) } else Modifier)
                                .heightIn(min = 48.dp),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        if (request.incoming && !isLocked) Box {
                            IconButton(onClick = { menu = true }, enabled = !state.busy) { Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.affinity_actions)) }
                            DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                                DropMenuItem(text = stringResource(R.string.affinity_block), onClick = { menu = false; viewModel.block(request.person.id) })
                                DropMenuItem(text = stringResource(R.string.report), onClick = { menu = false; viewModel.openReport(request.person.id) })
                            }
                        }
                    }
                    if (isLocked) {
                        Text(stringResource(R.string.affinity_conversation_invitation), style = MaterialTheme.typography.bodyMedium)
                        Button(onClick = onPaywall, modifier = Modifier.fillMaxWidth()) {
                            Text(stringResource(R.string.affinity_unlock))
                        }
                    } else {
                        Text(request.text, style = MaterialTheme.typography.bodyMedium, maxLines = if (expanded) Int.MAX_VALUE else 3, overflow = TextOverflow.Ellipsis)
                        if (request.text.length > 120 || request.text.count { it == '\n' } > 2) TextButton(onClick = { expanded = !expanded }, contentPadding = PaddingValues(horizontal = 0.dp)) {
                            Text(stringResource(if (expanded) R.string.affinity_message_less else R.string.affinity_message_more))
                        }
                        Text(stringResource(if (request.incoming) R.string.affinity_incoming else R.string.affinity_pending), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (request.incoming) Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(onClick = { viewModel.respond(request.id, true) }, enabled = !state.busy, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.affinity_accept)) }
                            TextButton(onClick = { viewModel.respond(request.id, false) }, enabled = !state.busy, modifier = Modifier.weight(1f)) { Text(stringResource(R.string.affinity_reject)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DropMenuItem(text: String, onClick: () -> Unit) {
    DropdownMenuItem(text = { Text(text) }, onClick = onClick)
}

@Composable
private fun AffinityReportDialog(viewModel: AffinityViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    if (state.reportPeer != null) AlertDialog(onDismissRequest = viewModel::closeReport,
        title = { Text(stringResource(R.string.report)) }, text = { OutlinedTextField(state.reportReason, viewModel::editReport, label = { Text(stringResource(R.string.specify_reason)) }) },
        confirmButton = { TextButton(onClick = viewModel::report, enabled = !state.busy && state.reportReason.isNotBlank()) { Text(stringResource(R.string.report)) } },
        dismissButton = { TextButton(onClick = viewModel::closeReport) { Text(stringResource(R.string.cancel)) } })
}

@Composable
fun AffinityChatBanner(peerId: String, viewModel: AffinityContactViewModel = koinViewModel()) {
    val contact by viewModel.contact.collectAsStateWithLifecycle()
    var dismissed by rememberSaveable(peerId) { mutableStateOf(false) }
    LifecycleResumeEffect(peerId) { viewModel.load(peerId); onPauseOrDispose {} }
    if (contact.origin == "affinity" && !dismissed) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.affinity_chat_origin),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    if (contact.matched) {
                        Text(
                            stringResource(R.string.affinity_now_matched),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
                IconButton(
                    onClick = { dismissed = true },
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.affinity_dismiss),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}
