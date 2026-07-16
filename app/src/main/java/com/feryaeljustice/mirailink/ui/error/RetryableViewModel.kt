package com.feryaeljustice.mirailink.ui.error

import androidx.lifecycle.ViewModel

/**
 * Owns the recovery callback for the currently visible [UiError].
 * The callback stays outside immutable UI state and is cleared with the lifecycle.
 */
abstract class RetryableViewModel : ViewModel() {
    private var recoveryAction: (() -> Unit)? = null

    /** Registers the exact retry or recovery operation for the current error. */
    protected fun setRecoveryAction(action: () -> Unit) {
        recoveryAction = action
    }

    /** Runs the recovery chosen by the ViewModel when the action is activated. */
    fun performErrorAction() {
        recoveryAction?.invoke()
    }

    /** Drops any captured parameters or callbacks when this ViewModel is destroyed. */
    override fun onCleared() {
        recoveryAction = null
    }
}
