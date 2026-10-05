package com.feryaeljustice.mirailink.ui.holo

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.data.repository.FaqRepositoryImpl
import com.feryaeljustice.mirailink.domain.model.faq.FaqCategory
import com.feryaeljustice.mirailink.domain.usecase.faq.GetFaqItemsUseCase
import com.feryaeljustice.mirailink.ui.screens.settings.components.HoloProfileSetting
import com.feryaeljustice.mirailink.ui.screens.settings.faq.FaqScreen
import com.feryaeljustice.mirailink.ui.screens.settings.faq.FaqViewModel
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import kotlinx.coroutines.Dispatchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

/** Recurso y pantalla real de FAQ en dos idiomas, sin red ni textos obtenidos de Context. */
@RunWith(AndroidJUnit4::class)
class HoloLocalizationTest {
    @get:Rule val rule = createAndroidComposeRule<ComponentActivity>()

    @Test fun spanishFaqIncludesFiveHoloAnswers() = checkFaq("es")
    @Test fun englishFaqIncludesFiveHoloAnswers() = checkFaq("en")

    private fun checkFaq(language: String) {
        val viewModel = FaqViewModel(GetFaqItemsUseCase(FaqRepositoryImpl()), Dispatchers.Default)
        viewModel.selectCategory(FaqCategory.CARDS_AND_MATCHING)
        val questions = listOf(R.string.faq_holo_what_q, R.string.faq_holo_disable_q,
            R.string.faq_holo_fallback_q, R.string.faq_holo_gestures_q, R.string.faq_holo_privacy_q)
        val answers = listOf(R.string.faq_holo_what_a, R.string.faq_holo_disable_a,
            R.string.faq_holo_fallback_a, R.string.faq_holo_gestures_a, R.string.faq_holo_privacy_a)
        var localizedQuestions = emptyList<String>()
        var localizedAnswers = emptyList<String>()
        rule.setContent {
            val configuration = remember {
                Configuration(rule.activity.resources.configuration).apply { setLocale(Locale.forLanguageTag(language)) }
            }
            val context = remember { rule.activity.createConfigurationContext(configuration) }
            CompositionLocalProvider(LocalContext provides context, LocalConfiguration provides configuration,
                LocalResources provides context.resources) {
                localizedQuestions = questions.map { stringResource(it) }
                localizedAnswers = answers.map { stringResource(it) }
                MiraiLinkTheme(dynamicColor = false) { FaqScreen(onBackClick = {}, viewModel = viewModel) }
            }
        }
        rule.waitUntil(5_000) { viewModel.faqItems.value.size == 30 }
        localizedQuestions.forEachIndexed { index, question ->
            rule.onNode(hasScrollAction()).performScrollToNode(hasText(question))
            rule.onNodeWithText(question).performClick()
            rule.onNodeWithText(localizedAnswers[index]).assertExists()
        }
    }

    @Test fun settingUpdatesLanguageAndAccessibleState() {
        val locale = mutableStateOf("es")
        val checked = mutableStateOf(true)
        rule.setContent {
            val configuration = remember(locale.value) {
                Configuration(rule.activity.resources.configuration).apply { setLocale(Locale.forLanguageTag(locale.value)) }
            }
            val context = remember(configuration) { rule.activity.createConfigurationContext(configuration) }
            CompositionLocalProvider(LocalContext provides context, LocalConfiguration provides configuration,
                LocalResources provides context.resources) {
                MiraiLinkTheme(dynamicColor = false) { HoloProfileSetting(checked.value, { checked.value = it }) }
            }
        }
        rule.onNodeWithText("Perfil Holo-3D").assertExists()
        rule.onNode(isToggleable()).assertIsOn().performClick().assertIsOff()
        rule.runOnIdle { locale.value = "en" }
        rule.onNodeWithText("Holo-3D profile").assertExists()
        rule.onNode(isToggleable()).assertIsOff()
    }
}
