package com.feryaeljustice.mirailink.ui.error

import androidx.lifecycle.ViewModel

/**
 * Mantiene el callback de recuperación del [UiError] visible.
 * El callback permanece fuera del estado UI inmutable y se limpia con el ciclo de vida.
 */
abstract class RetryableViewModel : ViewModel() {
    private var recoveryAction: (() -> Unit)? = null

    /** Registra la operación concreta de reintento o recuperación del error actual. */
    protected fun setRecoveryAction(action: () -> Unit) {
        recoveryAction = action
    }

    /** Ejecuta la recuperación elegida por el ViewModel al activar la acción. */
    fun performErrorAction() {
        recoveryAction?.invoke()
    }

    /** Drops any captured parameters or callbacks when this ViewModel is destroyed. */
    override fun onCleared() {
        recoveryAction = null
    }
}
