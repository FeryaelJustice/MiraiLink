package com.feryaeljustice.mirailink.ui.utils.composition

import androidx.compose.runtime.staticCompositionLocalOf
import com.feryaeljustice.mirailink.ui.components.molecules.MiraiLinkSnackbarRequest

val LocalShowSnackbar = staticCompositionLocalOf<(MiraiLinkSnackbarRequest) -> Unit> { { } }

// val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }
//val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
