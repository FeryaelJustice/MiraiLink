package com.feryaeljustice.mirailink.ui.components.capsule

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.model.capsule.CrystalCapsule
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

private enum class CapsuleModalStep {
    MAIN,
    NEW_QUESTION,
    HISTORY,
}

private data class CatalogQuestionItem(
    val id: String,
    val category: String,
    val textEs: String,
    val textEn: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrystalCapsuleModal(
    state: CrystalCapsule,
    ownId: String?,
    busy: Boolean,
    isDemo: Boolean,
    onDismiss: () -> Unit,
    onAction: (type: String, category: String?, questionId: String?, text: String?, answer: String?, isCustom: Boolean) -> Unit,
) {
    var currentStep by rememberSaveable { mutableStateOf(CapsuleModalStep.MAIN) }
    var submittedRevision by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(state.revision) {
        if (submittedRevision?.let { state.revision > it } == true) {
            currentStep = CapsuleModalStep.MAIN
            submittedRevision = null
        }
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val resources = LocalResources.current
    val isSpanish = LocalConfiguration.current.locales[0].language == "es"

    val catalogQuestions = remember(resources) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val raw = resources.openRawResource(R.raw.crystal_capsule_catalog)
                .bufferedReader().use { it.readText() }
            val arr = json.parseToJsonElement(raw).jsonObject["questions"]?.jsonArray
            arr?.map {
                val obj = it.jsonObject
                CatalogQuestionItem(
                    id = obj["id"]?.jsonPrimitive?.content ?: "",
                    category = obj["category"]?.jsonPrimitive?.content ?: "",
                    textEs = obj["textEs"]?.jsonPrimitive?.content ?: "",
                    textEn = obj["textEn"]?.jsonPrimitive?.content ?: "",
                )
            } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header del Modal: Título y botón cerrar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentStep != CapsuleModalStep.MAIN) {
                    IconButton(onClick = { currentStep = CapsuleModalStep.MAIN }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                }
                Text(
                    text = when (currentStep) {
                        CapsuleModalStep.MAIN -> stringResource(R.string.capsule_title)
                        CapsuleModalStep.NEW_QUESTION -> stringResource(R.string.capsule_btn_new_question)
                        CapsuleModalStep.HISTORY -> stringResource(R.string.capsule_btn_history)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                    )
                }
            }

            // Barra de Progreso Lineal Estilizada
            val animatedProgress by animateFloatAsState(
                targetValue = state.progress / 4f,
                label = "capsule_progress_bar",
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.capsule_progress, state.progress),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    val levelTitles = listOf(
                        R.string.capsule_level_0,
                        R.string.capsule_level_1,
                        R.string.capsule_level_2,
                        R.string.capsule_level_3,
                        R.string.capsule_level_4,
                    )
                    Text(
                        text = stringResource(levelTitles[state.level.coerceIn(0, 4)]),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (isDemo) {
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.capsule_demo),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }

            when (currentStep) {
                CapsuleModalStep.MAIN -> {
                    CapsuleMainStepView(
                        state = state,
                        ownId = ownId,
                        isSpanish = isSpanish,
                        busy = busy,
                        onNavigateToHistory = { currentStep = CapsuleModalStep.HISTORY },
                        onNavigateToNewQuestion = { currentStep = CapsuleModalStep.NEW_QUESTION },
                        onAction = onAction,
                    )
                }
                CapsuleModalStep.NEW_QUESTION -> {
                    CapsuleNewQuestionStepView(
                        catalogQuestions = catalogQuestions,
                        isSpanish = isSpanish,
                        busy = busy,
                        onSend = { category, questionId, questionText, answerText, isCustom ->
                            submittedRevision = state.revision
                            onAction("question", category, questionId, questionText, answerText, isCustom)
                        },
                    )
                }
                CapsuleModalStep.HISTORY -> {
                    CapsuleHistoryStepView(
                        state = state,
                        ownId = ownId,
                        isSpanish = isSpanish,
                        busy = busy,
                        onAnswerPending = { answerText ->
                            submittedRevision = state.revision
                            onAction("answer", null, null, null, answerText, false)
                        },
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CapsuleMainStepView(
    state: CrystalCapsule,
    ownId: String?,
    isSpanish: Boolean,
    busy: Boolean,
    onNavigateToHistory: () -> Unit,
    onNavigateToNewQuestion: () -> Unit,
    onAction: (type: String, category: String?, questionId: String?, text: String?, answer: String?, isCustom: Boolean) -> Unit,
) {
    val isRevealed = state.status == "revealed"
    val hasActiveQuestion = state.question != null && !state.question.completed
    val isWaitingForMe = hasActiveQuestion && state.question.authorId != ownId
    val isWaitingForPeer = hasActiveQuestion && state.question.authorId == ownId

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (isRevealed) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.capsule_completed_congrats),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            if (isWaitingForMe) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = stringResource(R.string.capsule_pending_questions),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Button(onClick = onNavigateToHistory) {
                                Text(stringResource(R.string.capsule_reply))
                            }
                        }
                        state.question?.let { q ->
                            Text(
                                text = q.displayText(isSpanish),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            } else if (isWaitingForPeer) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.capsule_waiting_peer_answer),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        state.question?.let { q ->
                            Text(
                                text = q.displayText(isSpanish),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                            )
                            q.authorAnswer?.let { ans ->
                                Text(
                                    text = "${stringResource(R.string.capsule_your_answer_label)} $ans",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }

        // Acciones Centrales
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = onNavigateToHistory,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = stringResource(R.string.capsule_btn_history) +
                        if (state.completedQuestions.isNotEmpty()) " (${state.completedQuestions.size})" else "",
                )
            }

            if (!isRevealed) {
                Button(
                    onClick = onNavigateToNewQuestion,
                    enabled = !hasActiveQuestion && state.status == "active" && !busy,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                ) {
                    Text(stringResource(R.string.capsule_btn_new_question))
                }
                if (hasActiveQuestion) {
                    Text(
                        text = stringResource(R.string.capsule_waiting_turn_warning),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        // Footer: Opciones del modo
        if (!isRevealed) {
            Text(
                text = stringResource(R.string.capsule_mode_options),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.status == "active") {
                    CapsuleActionCard(
                        title = stringResource(R.string.capsule_pause),
                        description = stringResource(R.string.capsule_pause_desc),
                        icon = painterResource(id = R.drawable.ic_bolt),
                        enabled = !busy,
                        onClick = { onAction("pause", null, null, null, null, false) },
                    )
                } else if (state.status in listOf("paused", "left")) {
                    CapsuleActionCard(
                        title = stringResource(R.string.capsule_resume),
                        description = stringResource(R.string.capsule_resume_desc),
                        icon = painterResource(id = R.drawable.ic_bolt),
                        enabled = !busy && ownId !in state.resumeAccepted,
                        onClick = { onAction("resume", null, null, null, null, false) },
                    )
                }

                when (state.revealRequestedBy) {
                    null -> {
                        CapsuleActionCard(
                            title = stringResource(R.string.capsule_request_reveal),
                            description = stringResource(R.string.capsule_request_reveal_desc),
                            icon = painterResource(id = R.drawable.ic_visibility),
                            enabled = !busy,
                            onClick = { onAction("request_reveal", null, null, null, null, false) },
                        )
                    }
                    ownId -> {
                        CapsuleActionCard(
                            title = stringResource(R.string.capsule_cancel_reveal),
                            description = stringResource(R.string.capsule_cancel_reveal_desc),
                            icon = painterResource(id = R.drawable.ic_visibility_off),
                            enabled = !busy,
                            onClick = { onAction("cancel_reveal", null, null, null, null, false) },
                        )
                    }
                    else -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(stringResource(R.string.capsule_reveal_offer), style = MaterialTheme.typography.bodySmall)
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                                    Button(onClick = { onAction("accept_reveal", null, null, null, null, false) }) {
                                        Text(stringResource(R.string.capsule_accept_reveal))
                                    }
                                    OutlinedButton(onClick = { onAction("decline_reveal", null, null, null, null, false) }) {
                                        Text(stringResource(R.string.capsule_decline))
                                    }
                                }
                            }
                        }
                    }
                }

                if (state.status != "cancelled") {
                    CapsuleActionCard(
                        title = stringResource(R.string.capsule_leave),
                        description = stringResource(R.string.capsule_leave_desc),
                        icon = painterResource(id = R.drawable.ic_report),
                        isDestructive = true,
                        enabled = !busy,
                        onClick = { onAction("leave", null, null, null, null, false) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CapsuleActionCard(
    title: String,
    description: String,
    icon: Painter,
    enabled: Boolean,
    isDestructive: Boolean = false,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDestructive) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            },
        ),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun CapsuleNewQuestionStepView(
    catalogQuestions: List<CatalogQuestionItem>,
    isSpanish: Boolean,
    busy: Boolean,
    onSend: (category: String, questionId: String?, questionText: String, answerText: String, isCustom: Boolean) -> Unit,
) {
    val categories = listOf(
        "anime" to R.string.capsule_anime,
        "gaming" to R.string.capsule_gaming,
        "hobbies" to R.string.capsule_hobbies,
        "everyday" to R.string.capsule_everyday,
        "ideal_date" to R.string.capsule_ideal_date,
        "projects" to R.string.capsule_projects,
        "relationships" to R.string.capsule_relationships,
        "family" to R.string.capsule_family,
    )
    var selectedCategory by rememberSaveable { mutableStateOf("anime") }
    var selectedQuestionId by rememberSaveable { mutableStateOf("") }
    var selectedQuestionText by rememberSaveable { mutableStateOf("") }
    var isCustomSelected by rememberSaveable { mutableStateOf(false) }
    var customQuestionInput by rememberSaveable { mutableStateOf("") }
    var answerInput by rememberSaveable { mutableStateOf("") }

    val categoryQuestions = remember(selectedCategory, catalogQuestions) {
        catalogQuestions.filter { it.category == selectedCategory }.take(3)
    }

    LaunchedEffect(selectedCategory) {
        if (!isCustomSelected && categoryQuestions.isNotEmpty()) {
            val first = categoryQuestions.first()
            selectedQuestionId = first.id
            selectedQuestionText = if (isSpanish) first.textEs else first.textEn
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(R.string.capsule_choose_category),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )

        // Carrusel Horizontal de Categorías
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            categories.forEach { (catKey, catRes) ->
                FilterChip(
                    selected = selectedCategory == catKey,
                    onClick = {
                        selectedCategory = catKey
                        isCustomSelected = false
                    },
                    label = { Text(stringResource(catRes)) },
                )
            }
        }

        Text(
            text = stringResource(R.string.capsule_suggested_questions),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
        )

        // 3 Preguntas sugeridas
        categoryQuestions.forEach { qItem ->
            val text = if (isSpanish) qItem.textEs else qItem.textEn
            val isSelected = !isCustomSelected && selectedQuestionText == text
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isCustomSelected = false
                        selectedQuestionId = qItem.id
                        selectedQuestionText = text
                    },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    },
                ),
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }

        // 4ª Opción: Crear pregunta propia
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isCustomSelected = true },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCustomSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                },
            ),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Text(
                    text = stringResource(R.string.capsule_custom_question),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isCustomSelected) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }

        if (isCustomSelected) {
            OutlinedTextField(
                value = customQuestionInput,
                onValueChange = { customQuestionInput = it.take(120) },
                label = { Text(stringResource(R.string.capsule_custom_question_hint)) },
                supportingText = { Text("${customQuestionInput.length}/120") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Campo obligatorio para la respuesta del proponente
        OutlinedTextField(
            value = answerInput,
            onValueChange = { answerInput = it.take(300) },
            label = { Text(stringResource(R.string.capsule_answer_hint)) },
            supportingText = { Text("${answerInput.length}/300") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4,
        )

        val finalQuestionText = if (isCustomSelected) customQuestionInput.trim() else selectedQuestionText.trim()
        val finalQuestionId = if (isCustomSelected) null else selectedQuestionId.takeIf { it.isNotBlank() }
        val canSend = finalQuestionText.isNotBlank() && answerInput.isNotBlank() && !busy

        Button(
            onClick = {
                onSend(selectedCategory, finalQuestionId, finalQuestionText, answerInput.trim(), isCustomSelected)
            },
            enabled = canSend,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(stringResource(R.string.capsule_send_question))
        }
    }
}

@Composable
private fun CapsuleHistoryStepView(
    state: CrystalCapsule,
    ownId: String?,
    isSpanish: Boolean,
    busy: Boolean,
    onAnswerPending: (String) -> Unit,
) {
    val activeQ = state.question
    var pendingAnswerInput by rememberSaveable { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Sección de pregunta activa
        if (activeQ != null && !activeQ.completed) {
            if (activeQ.authorId != ownId) {
                // Pregunta pendiente por responder por mí
                Text(
                    text = stringResource(R.string.capsule_pending_questions),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = activeQ.displayText(isSpanish),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        OutlinedTextField(
                            value = pendingAnswerInput,
                            onValueChange = { pendingAnswerInput = it.take(300) },
                            label = { Text(stringResource(R.string.capsule_answer_hint)) },
                            supportingText = { Text("${pendingAnswerInput.length}/300") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2,
                        )
                        Button(
                            onClick = {
                                val text = pendingAnswerInput.trim()
                                pendingAnswerInput = ""
                                onAnswerPending(text)
                            },
                            enabled = pendingAnswerInput.isNotBlank() && !busy,
                            modifier = Modifier.align(Alignment.End),
                        ) {
                            Text(stringResource(R.string.capsule_send_answer))
                        }
                    }
                }
            } else {
                // Esperando la respuesta del compañero
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = stringResource(R.string.capsule_waiting_peer_answer),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            text = activeQ.displayText(isSpanish),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        activeQ.authorAnswer?.let { ans ->
                            Text(
                                text = "${stringResource(R.string.capsule_your_answer_label)} $ans",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
        }

        // Sección: Preguntas respondidas por ambos (Historial completo)
        Text(
            text = stringResource(R.string.capsule_answered_by_both),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        if (state.completedQuestions.isEmpty()) {
            Text(
                text = stringResource(R.string.capsule_history_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.completedQuestions.forEach { completed ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                text = completed.displayText(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            val isMeAuthor = !completed.authorId.isNullOrBlank() && completed.authorId == ownId
                            val myAnswer = if (isMeAuthor) completed.authorAnswer else completed.peerAnswer
                            val theirAnswer = if (isMeAuthor) completed.peerAnswer else completed.authorAnswer

                            if (myAnswer.isNotBlank()) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.capsule_your_answer_label)} $myAnswer",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                            }
                            if (theirAnswer.isNotBlank()) {
                                Surface(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = "${stringResource(R.string.capsule_peer_answer_label)} $theirAnswer",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
