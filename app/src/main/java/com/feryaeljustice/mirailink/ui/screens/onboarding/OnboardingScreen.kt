package com.feryaeljustice.mirailink.ui.screens.onboarding

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingChatIllustration
import com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingEventsIllustration
import com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingPillIndicator
import com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingProfileIllustration
import com.feryaeljustice.mirailink.ui.screens.onboarding.components.OnboardingRadarConnectionIllustration
import com.feryaeljustice.mirailink.ui.theme.MiraiLinkTheme
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import kotlinx.coroutines.launch

private data class OnboardingStep(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
)

private val onboardingSteps = listOf(
    OnboardingStep(
        titleRes = R.string.onboarding_title_1,
        descriptionRes = R.string.onboarding_desc_1,
    ),
    OnboardingStep(
        titleRes = R.string.onboarding_title_2,
        descriptionRes = R.string.onboarding_desc_2,
    ),
    OnboardingStep(
        titleRes = R.string.onboarding_title_3,
        descriptionRes = R.string.onboarding_desc_3,
    ),
    OnboardingStep(
        titleRes = R.string.onboarding_title_4,
        descriptionRes = R.string.onboarding_desc_4,
    ),
)

@Suppress("ktlint:standard:function-naming")
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val pagerState = rememberPagerState(pageCount = { onboardingSteps.size })
    val scope = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == onboardingSteps.lastIndex

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .then(
                if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                    Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                } else {
                    Modifier
                },
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Barra Superior: Logo a la izquierda + Botón "Omitir" a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logomirailink),
                contentDescription = stringResource(R.string.content_description_settings_screen_img_logo),
                modifier = Modifier.size(36.dp),
            )

            AnimatedVisibility(
                visible = !isLastPage,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                TextButton(
                    onClick = onFinish,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_skip),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Contenedor Central Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) { page ->
            val step = onboardingSteps[page]

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Ilustración vectorial nativa según el paso
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    when (page) {
                        0 -> OnboardingRadarConnectionIllustration()
                        1 -> OnboardingProfileIllustration()
                        2 -> OnboardingChatIllustration()
                        3 -> OnboardingEventsIllustration()
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Titular estilizado
                Text(
                    text = stringResource(id = step.titleRes),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción concisa
                Text(
                    text = stringResource(id = step.descriptionRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.25,
                    modifier = Modifier.padding(horizontal = 12.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Barra Inferior: Indicador de Páginas + Botones de Acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Indicador animado de píldora
            OnboardingPillIndicator(
                pageCount = onboardingSteps.size,
                currentPage = pagerState.currentPage,
            )

            // Controles de navegación
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.size(48.dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.previous),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Button(
                    onClick = {
                        scope.launch {
                            if (isLastPage) {
                                onFinish()
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLastPage) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                        contentColor = if (isLastPage) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        },
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier.height(48.dp),
                ) {
                    Text(
                        text = if (isLastPage) {
                            stringResource(R.string.onboarding_start_adventure)
                        } else {
                            stringResource(R.string.next)
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = if (isLastPage) {
                            Icons.Default.Check
                        } else {
                            Icons.AutoMirrored.Filled.ArrowForward
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }
    }
}

@Preview(name = "Onboarding Screen - Light", showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    MiraiLinkTheme(darkTheme = false) {
        OnboardingScreen(onFinish = {})
    }
}

@Preview(name = "Onboarding Screen - Dark", showBackground = true)
@Composable
private fun OnboardingScreenDarkPreview() {
    MiraiLinkTheme(darkTheme = true) {
        OnboardingScreen(onFinish = {})
    }
}
