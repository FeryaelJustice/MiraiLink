package com.feryaeljustice.mirailink.ui.error

import androidx.annotation.StringRes

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

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

/** Resolves a [UiText] while a Compose resource context is available. */
@Composable
fun UiText.asString(): String = when (this) {
    is UiText.Resource -> stringResource(id, *args.toTypedArray())
}

/** Resolves a [UiError] message while a Compose resource context is available. */
@Composable
fun UiError.asString(): String = message.asString()

fun UiText.asString(context: android.content.Context): String = when (this) {
    is UiText.Resource -> if (args.isEmpty()) context.getString(id) else context.getString(id, *args.toTypedArray())
}

fun UiError.asString(context: android.content.Context): String = message.asString(context)

