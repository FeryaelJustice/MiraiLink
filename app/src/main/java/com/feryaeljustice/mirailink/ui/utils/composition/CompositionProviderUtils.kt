package com.feryaeljustice.mirailink.ui.utils.composition

import androidx.compose.runtime.staticCompositionLocalOf

val LocalShowSnackbar = staticCompositionLocalOf<(String) -> Unit> { { } }