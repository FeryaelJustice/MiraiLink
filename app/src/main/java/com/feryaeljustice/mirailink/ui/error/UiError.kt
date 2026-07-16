package com.feryaeljustice.mirailink.ui.error

import androidx.annotation.StringRes

/** Localizable text that does not retain Android context in a ViewModel. */
sealed interface UiText {
    /** String resource plus optional formatting arguments. */
    data class Resource(
        @StringRes val id: Int,
        val args: List<Any> = emptyList(),
    ) : UiText
}

/** Recovery behavior communicated to UI and tests. */
enum class ErrorRecovery {
    RETRY,
    SIGN_IN_AGAIN,
    REVIEW_INPUT,
}

/** Visible, localized and actionable error state with no callback or technical cause. */
data class UiError(
    val message: UiText,
    val actionLabel: UiText,
    val recovery: ErrorRecovery,
)
