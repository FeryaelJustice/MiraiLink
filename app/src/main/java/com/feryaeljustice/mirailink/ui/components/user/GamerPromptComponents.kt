package com.feryaeljustice.mirailink.ui.components.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.model.response.catalog.CatalogItemOptionDto
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkOutlinedButton
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.viewentries.user.GamerPromptAnswerViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

@Composable
fun GamerPromptCard(
    prompt: GamerPromptAnswerViewEntry,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            MiraiLinkText(
                text = prompt.question,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(6.dp))
            MiraiLinkText(
                text = prompt.answer,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipFlowRow(items: List<String>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ) {
                MiraiLinkText(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                )
            }
        }
    }
}

fun resolveLanguageFlag(language: String): String {
    val clean = language.trim().lowercase()
    return when {
        clean.contains("español") || clean.contains("spanish") || clean == "es" -> "🇪🇸"
        clean.contains("inglés") || clean.contains("ingles") || clean.contains("english") || clean == "en" -> "🇬🇧"
        clean.contains("japonés") || clean.contains("japones") || clean.contains("japanese") || clean == "ja" -> "🇯🇵"
        clean.contains("francés") || clean.contains("frances") || clean.contains("french") || clean == "fr" -> "🇫🇷"
        clean.contains("alemán") || clean.contains("aleman") || clean.contains("german") || clean == "de" -> "🇩🇪"
        clean.contains("italiano") || clean.contains("italian") || clean == "it" -> "🇮🇹"
        clean.contains("portugués") || clean.contains("portugues") || clean.contains("portuguese") || clean == "pt" -> "🇵🇹"
        clean.contains("chino") || clean.contains("chinese") || clean == "zh" -> "🇨🇳"
        clean.contains("coreano") || clean.contains("korean") || clean == "ko" -> "🇰🇷"
        clean.contains("ruso") || clean.contains("russian") || clean == "ru" -> "🇷🇺"
        else -> "🗣️"
    }
}

data class PersonalCategoryData(
    val title: String,
    val iconEmoji: String,
    val items: List<String>,
)

fun buildCategorizedPersonalInfo(user: UserViewEntry): List<PersonalCategoryData> {
    val categories = mutableListOf<PersonalCategoryData>()

    // 1. Idiomas con banderas
    if (user.spokenLanguages.isNotEmpty()) {
        val languageChips = user.spokenLanguages.map { lang ->
            val flag = resolveLanguageFlag(lang)
            "$flag $lang"
        }
        categories.add(
            PersonalCategoryData(
                title = "Idiomas",
                iconEmoji = "🌐",
                items = languageChips,
            ),
        )
    }

    // 2. Qué busco y planes de familia
    val goalChips = mutableListOf<String>()
    goalChips.addAll(user.relationshipGoals.map { "🎯 $it" })
    goalChips.addAll(user.familyOptions.map { "👨‍👩‍👧 $it" })
    if (goalChips.isNotEmpty()) {
        categories.add(
            PersonalCategoryData(
                title = "Qué busco y planes",
                iconEmoji = "🎯",
                items = goalChips,
            ),
        )
    }

    // 3. Laboral y estudios
    val workEduChips = mutableListOf<String>()
    user.profession?.takeIf { it.isNotBlank() }?.let { workEduChips.add("💼 $it") }
    user.educationLevel?.takeIf { it.isNotBlank() }?.let { workEduChips.add("🎓 $it") }
    if (workEduChips.isNotEmpty()) {
        categories.add(
            PersonalCategoryData(
                title = "Laboral y estudios",
                iconEmoji = "💼",
                items = workEduChips,
            ),
        )
    }

    // 4. Estilo de vida
    val lifestyleChips = mutableListOf<String>()
    user.smokingHabit?.takeIf { it.isNotBlank() }?.let { lifestyleChips.add("🚭 $it") }
    user.drinkingHabit?.takeIf { it.isNotBlank() }?.let { lifestyleChips.add("🍷 $it") }
    if (lifestyleChips.isNotEmpty()) {
        categories.add(
            PersonalCategoryData(
                title = "Estilo de vida",
                iconEmoji = "🌿",
                items = lifestyleChips,
            ),
        )
    }

    // 5. Identidad y creencias
    val identityChips = mutableListOf<String>()
    user.zodiacSign?.takeIf { it.isNotBlank() }?.let { identityChips.add("✨ $it") }
    user.religion?.takeIf { it.isNotBlank() }?.let { identityChips.add("🕊️ $it") }
    user.politicalStance?.takeIf { it.isNotBlank() }?.let { identityChips.add("⚖️ $it") }
    user.sexualOrientation?.takeIf { it.isNotBlank() }?.let { identityChips.add("🌈 $it") }
    if (identityChips.isNotEmpty()) {
        categories.add(
            PersonalCategoryData(
                title = "Identidad y creencias",
                iconEmoji = "✨",
                items = identityChips,
            ),
        )
    }

    return categories
}

fun buildPersonalChips(user: UserViewEntry): List<String> {
    return buildCategorizedPersonalInfo(user).flatMap { it.items }
}

@Composable
fun CategorizedPersonalInfoSection(
    user: UserViewEntry,
    modifier: Modifier = Modifier,
    headerColor: androidx.compose.ui.graphics.Color? = null,
) {
    val categories = remember(user) { buildCategorizedPersonalInfo(user) }
    if (categories.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        categories.forEach { category ->
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    MiraiLinkText(
                        text = "${category.iconEmoji} ${category.title}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = headerColor ?: MaterialTheme.colorScheme.onSurface,
                    )
                }
                ChipFlowRow(items = category.items)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamerPromptEditSection(
    prompts: List<GamerPromptAnswerViewEntry>,
    availableCatalogPrompts: List<CatalogItemOptionDto>,
    onAddOrUpdatePrompt: (promptId: String, question: String, answer: String) -> Unit,
    onChangePromptQuestion: (oldPromptId: String, newPromptId: String, newQuestion: String) -> Unit,
    onRemovePrompt: (promptId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showAddPicker by remember { mutableStateOf(false) }
    var promptToChange by remember { mutableStateOf<GamerPromptAnswerViewEntry?>(null) }

    val usedPromptIds = remember(prompts) { prompts.map { it.promptId }.toSet() }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        prompts.forEach { promptEntry ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        MiraiLinkText(
                            text = promptEntry.question,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f),
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextButton(onClick = { promptToChange = promptEntry }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                MiraiLinkText(
                                    text = stringResource(R.string.profile_edit_change_question),
                                    style = MaterialTheme.typography.labelMedium,
                                )
                            }
                            IconButton(onClick = { onRemovePrompt(promptEntry.promptId) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = promptEntry.answer,
                        onValueChange = { newText ->
                            if (newText.length <= 300) {
                                onAddOrUpdatePrompt(promptEntry.promptId, promptEntry.question, newText)
                            }
                        },
                        placeholder = {
                            MiraiLinkText(
                                text = stringResource(R.string.profile_edit_prompt_answer_hint),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        supportingText = {
                            MiraiLinkText(
                                text = "${promptEntry.answer.length}/300",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (promptEntry.answer.length >= 300) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        minLines = 2,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        if (prompts.size < 3) {
            MiraiLinkOutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showAddPicker = true },
            ) {
                MiraiLinkText(text = stringResource(R.string.profile_edit_add_prompt))
            }
        }
    }

    // Modal to add a new prompt question
    if (showAddPicker) {
        val remainingQuestions = availableCatalogPrompts.filter { it.id !in usedPromptIds }
        ModalBottomSheet(
            onDismissRequest = { showAddPicker = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                        text = stringResource(R.string.profile_edit_prompt_select_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { showAddPicker = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(remainingQuestions, key = { it.id }) { option ->
                        val question = option.question ?: option.label ?: option.code
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAddOrUpdatePrompt(option.id, question, "")
                                    showAddPicker = false
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ) {
                            MiraiLinkText(
                                text = question,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    // Modal to change prompt question
    promptToChange?.let { currentPrompt ->
        val candidateQuestions = availableCatalogPrompts.filter { it.id !in usedPromptIds || it.id == currentPrompt.promptId }
        ModalBottomSheet(
            onDismissRequest = { promptToChange = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
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
                        text = stringResource(R.string.profile_edit_prompt_select_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { promptToChange = null }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(candidateQuestions, key = { it.id }) { option ->
                        val question = option.question ?: option.label ?: option.code
                        val isCurrent = option.id == currentPrompt.promptId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!isCurrent) {
                                        onChangePromptQuestion(currentPrompt.promptId, option.id, question)
                                    }
                                    promptToChange = null
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                        ) {
                            MiraiLinkText(
                                text = question,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
