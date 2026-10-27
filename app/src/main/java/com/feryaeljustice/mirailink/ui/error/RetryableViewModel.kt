package com.feryaeljustice.mirailink.ui.error

import androidx.lifecycle.ViewModel
import com.feryaeljustice.mirailink.domain.error.AppError
import java.lang.ref.WeakReference
import com.feryaeljustice.mirailink.ui.error.toUiError as mapUiError

/** Recovery belongs to the displayed error, not the last operation started. */
abstract class RetryableViewModel : ViewModel() {
    private data class Recovery(val error: WeakReference<UiError>, val action: () -> Unit)
    private val recoveries = mutableListOf<Recovery>()
    private var recoveryAction: (() -> Unit)? = null

    protected fun setRecoveryAction(action: () -> Unit) {
        synchronized(recoveries) { recoveryAction = action }
    }

    protected fun errorFor(error: AppError, action: () -> Unit): UiError =
        rememberError(error.mapUiError(), action)

    protected fun rememberError(error: UiError, action: () -> Unit): UiError {
        synchronized(recoveries) {
            recoveries.removeAll { it.error.get() == null }
            recoveries.add(Recovery(WeakReference(error), action))
            recoveryAction = action
        }
        return error
    }

    /** Binds existing synchronous failure handlers to their exact emitted error. */
    protected fun AppError.toUiError(): UiError {
        val error = mapUiError()
        val action = synchronized(recoveries) { recoveryAction }
        return if (action != null) rememberError(error, action) else error
    }

    fun recoveryFor(error: UiError): () -> Unit = { performErrorAction(error) }

    fun performErrorAction(error: UiError) {
        val action = synchronized(recoveries) {
            recoveries.firstOrNull { it.error.get() === error }?.action
        }
        action?.invoke()
    }

    /** Compatibility entry point for explicit pending-operation actions and tests. */
    fun performErrorAction() {
        synchronized(recoveries) { recoveryAction }?.invoke()
    }

    protected fun clearRecoveries() {
        synchronized(recoveries) {
            recoveryAction = null
            recoveries.clear()
        }
    }

    override fun onCleared() {
        clearRecoveries()
    }
}
