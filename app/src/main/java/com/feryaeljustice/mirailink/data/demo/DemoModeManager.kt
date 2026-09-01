package com.feryaeljustice.mirailink.data.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemoModeManager(
    private val seeder: DemoDataSeeder,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {
    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    fun isDemoActive(): Boolean = _isDemoMode.value

    fun enableDemoMode(onComplete: (() -> Unit)? = null): Job {
        _isDemoMode.value = true
        return scope.launch {
            seeder.seedInitialDataIfEmpty()
            onComplete?.invoke()
        }
    }

    fun disableDemoMode() {
        _isDemoMode.value = false
    }

    fun resetDemoData(onComplete: (() -> Unit)? = null): Job {
        return scope.launch {
            seeder.resetDemoData()
            onComplete?.invoke()
        }
    }
}
