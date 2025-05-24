package com.feryaeljustice.mirailink.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
fun rememberInitializedStateFlow(
    isInitializedFlow: StateFlow<Boolean>,
    timeoutMillis: Long = 5000L
): Boolean {
    val isInitialized by isInitializedFlow.collectAsState()
    var show by remember { mutableStateOf(false) }

    LaunchedEffect(isInitialized) {
        if (isInitialized) {
            show = true
        } else {
            delay(timeoutMillis) // previene quedarse colgado si falla
            show = true // fallback (opcional)
        }
    }

    return show
}
