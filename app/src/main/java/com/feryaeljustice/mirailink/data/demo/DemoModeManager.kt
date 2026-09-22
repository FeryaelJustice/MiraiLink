package com.feryaeljustice.mirailink.data.demo

import com.feryaeljustice.mirailink.data.local.demo.DemoDataSeeder
import com.feryaeljustice.mirailink.data.datastore.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DemoModeManager(
    private val seeder: DemoDataSeeder,
    private val sessionManager: SessionManager,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {
    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    init {
        // El modo demo debe sobrevivir a un relanzamiento solo cuando la sesion
        // persistida contiene el token demo. Evita tratarla como sesion real.
        scope.launch {
            sessionManager.tokenFlow.collect { token ->
                if (token == "DEMO_TOKEN") {
                    _isDemoMode.value = true
                } else if (!_isDemoMode.value) {
                    _isDemoMode.value = false
                }
            }
        }
    }

    fun isDemoActive(): Boolean = _isDemoMode.value || sessionManager.getCurrentTokenSync() == "DEMO_TOKEN"

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
            onComplete?.let { callback ->
                withContext(Dispatchers.Main.immediate) {
                    callback()
                }
            }
        }
    }
}
