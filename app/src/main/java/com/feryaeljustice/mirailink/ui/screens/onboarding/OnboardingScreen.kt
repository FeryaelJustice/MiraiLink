package com.feryaeljustice.mirailink.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkImage
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkText
import com.feryaeljustice.mirailink.ui.components.atoms.MiraiLinkTextButton
import com.feryaeljustice.mirailink.ui.utils.DeviceConfiguration
import com.feryaeljustice.mirailink.ui.utils.requiresDisplayCutoutPadding
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val pagerState =
        rememberPagerState(
            pageCount = { 3 },
        )
    val scope = rememberCoroutineScope()

    val pages =
        listOf(
            R.drawable.onboarding_1,
            R.drawable.onboarding_2,
            R.drawable.onboarding_3,
        )
    val pageTexts =
        listOf(
            R.string.onboarding_1,
            R.string.onboarding_2,
            R.string.onboarding_3,
        )

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(8.dp)
                .then(
                    if (deviceConfiguration.requiresDisplayCutoutPadding()) {
                        Modifier.windowInsetsPadding(WindowInsets.displayCutout)
                    } else {
                        Modifier
                    },
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logomirailink),
            contentDescription = stringResource(R.string.content_description_settings_screen_img_logo),
            modifier =
                Modifier
                    .weight(0.1f)
                    .padding(8.dp),
        )
        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .weight(0.8f)
                    .verticalScroll(rememberScrollState()),
        ) { page ->
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MiraiLinkImage(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.6f)
                            .fillMaxSize(0.4f),
                    painterId = pages[page],
                    contentScale = ContentScale.Fit,
                    hasBorder = true,
                )
                Spacer(modifier = Modifier.height(16.dp))
                MiraiLinkText(
                    text = stringResource(id = pageTexts[page]),
                    textAlign = TextAlign.Justify,
                )
            }
        }

        Row(
            modifier =
                Modifier
                    .weight(0.1f)
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            horizontalArrangement = if (pagerState.currentPage == 0) Arrangement.End else Arrangement.SpaceBetween,
        ) {
            if (pagerState.currentPage > 0) {
                MiraiLinkTextButton(
                    text = stringResource(R.string.previous),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    isTransparentBackground = false,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                )
            }

            MiraiLinkTextButton(
                text =
                    if (pagerState.currentPage == pages.lastIndex) {
                        stringResource(R.string.start)
                    } else {
                        stringResource(
                            R.string.next,
                        )
                    },
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage == pages.lastIndex) {
                            onFinish()
                        } else {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                isTransparentBackground = false,
                contentColor =
                    if (pagerState.currentPage ==
                        pages.lastIndex
                    ) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.tertiary
                    },
                containerColor =
                    if (pagerState.currentPage ==
                        pages.lastIndex
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.tertiaryContainer
                    },
            )
        }
    }
}
