package com.feryaeljustice.mirailink.ui.utils.extensions

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Suppress("EffectKeys")
@Composable
fun Modifier.debounceClickable(
    onClick: () -> Unit,
    enabled: Boolean = true,
    debounceTime: Long = 600L,
): Modifier {
    var isClickable by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(debounceTime)
        isClickable = true
    }

    return this.then(
        Modifier.clickable(enabled = enabled && isClickable) {
            if (!isClickable) return@clickable
            isClickable = false
            onClick()
        },
    )
}
