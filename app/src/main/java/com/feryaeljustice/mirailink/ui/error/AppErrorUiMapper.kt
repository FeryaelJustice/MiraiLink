package com.feryaeljustice.mirailink.ui.error

import androidx.annotation.StringRes
import com.feryaeljustice.mirailink.R
import com.feryaeljustice.mirailink.domain.error.AppError
import com.feryaeljustice.mirailink.domain.error.AuthError
import com.feryaeljustice.mirailink.domain.error.DataError
import com.feryaeljustice.mirailink.domain.error.UnknownError
import com.feryaeljustice.mirailink.domain.error.ValidationError
import com.feryaeljustice.mirailink.domain.error.LocationError
import com.feryaeljustice.mirailink.domain.error.SubscriptionError

/** Convierte errores de dominio en texto localizado, etiqueta de acción y semántica de recuperación. */
fun AppError.toUiError(): UiError {
    val recovery =
        when (this) {
            AuthError.SESSION_EXPIRED -> ErrorRecovery.SIGN_IN_AGAIN
            AuthError.INVALID_CREDENTIALS,
            AuthError.INVALID_VERIFICATION_CODE,
            AuthError.INVALID_TWO_FACTOR_CODE,
            ValidationError.INVALID_INPUT,
            ValidationError.INVALID_MEDIA,
            ValidationError.MISSING_REQUIRED_VALUE,
            LocationError.LOCATION_REQUIRED,
            LocationError.RESIDENCE_COUNTRY_REQUIRED,
            SubscriptionError.DAILY_LIKES_LIMIT_REACHED,
            SubscriptionError.DAILY_UNDO_LIMIT_REACHED,
            -> ErrorRecovery.REVIEW_INPUT
            else -> ErrorRecovery.RETRY
        }
    val action =
        when (recovery) {
            ErrorRecovery.RETRY -> R.string.action_retry
            ErrorRecovery.SIGN_IN_AGAIN -> R.string.action_sign_in_again
            ErrorRecovery.REVIEW_INPUT -> R.string.action_review
        }
    return UiError(
        message = UiText.Resource(messageResource()),
        actionLabel = UiText.Resource(action),
        recovery = recovery,
    )
}

@StringRes
private fun AppError.messageResource(): Int =
    when (this) {
        is DataError.Network ->
            when (this) {
                DataError.Network.NO_CONNECTION -> R.string.error_no_connection
                DataError.Network.TIMEOUT -> R.string.error_timeout
                DataError.Network.BAD_REQUEST -> R.string.error_bad_request
                DataError.Network.FORBIDDEN -> R.string.error_forbidden
                DataError.Network.NOT_FOUND -> R.string.error_not_found
                DataError.Network.CONFLICT -> R.string.error_conflict
                DataError.Network.RATE_LIMITED -> R.string.error_rate_limited
                DataError.Network.PAYLOAD_TOO_LARGE -> R.string.error_payload_too_large
                DataError.Network.SERVER -> R.string.error_server
                DataError.Network.SERVICE_UNAVAILABLE -> R.string.error_service_unavailable
                DataError.Network.SERIALIZATION -> R.string.error_serialization
                DataError.Network.UNKNOWN -> R.string.error_network_unknown
            }
        is DataError.Local ->
            when (this) {
                DataError.Local.NOT_FOUND -> R.string.error_local_not_found
                DataError.Local.STORAGE_FULL -> R.string.error_storage_full
                DataError.Local.CORRUPTED -> R.string.error_local_corrupted
                DataError.Local.ACCESS_DENIED -> R.string.error_local_access
                DataError.Local.UNKNOWN -> R.string.error_local_unknown
            }
        is AuthError ->
            when (this) {
                AuthError.INVALID_CREDENTIALS -> R.string.error_invalid_credentials
                AuthError.SESSION_EXPIRED -> R.string.error_session_expired
                AuthError.VERIFICATION_REQUIRED -> R.string.error_verification_required
                AuthError.INVALID_VERIFICATION_CODE -> R.string.error_invalid_verification_code
                AuthError.INVALID_TWO_FACTOR_CODE -> R.string.error_invalid_two_factor_code
            }
        is ValidationError ->
            when (this) {
                ValidationError.INVALID_INPUT -> R.string.error_invalid_input
                ValidationError.INVALID_MEDIA -> R.string.error_invalid_media
                ValidationError.MISSING_REQUIRED_VALUE -> R.string.error_missing_value
                ValidationError.UNDERAGE -> R.string.error_underage
            }
        is LocationError ->
            when (this) {
                LocationError.LOCATION_REQUIRED -> R.string.error_location_required_for_radius
                LocationError.RESIDENCE_COUNTRY_REQUIRED -> R.string.error_residence_country_required
            }
        is SubscriptionError ->
            when (this) {
                SubscriptionError.DAILY_LIKES_LIMIT_REACHED -> R.string.error_daily_likes_limit_reached
                SubscriptionError.DAILY_UNDO_LIMIT_REACHED -> R.string.error_daily_undo_limit_reached
                SubscriptionError.PREMIUM_RADIUS_REQUIRED -> R.string.error_premium_radius_required
                SubscriptionError.PREMIUM_PASSPORT_REQUIRED -> R.string.error_premium_passport_required
            }
        is com.feryaeljustice.mirailink.domain.error.CapsuleError -> when(this) {
            com.feryaeljustice.mirailink.domain.error.CapsuleError.UNAVAILABLE -> R.string.capsule_unavailable
            com.feryaeljustice.mirailink.domain.error.CapsuleError.MODE_CHANGED -> R.string.capsule_mode_changed
            com.feryaeljustice.mirailink.domain.error.CapsuleError.LIKE_MODE_CONFLICT -> R.string.capsule_mode_conflict
            com.feryaeljustice.mirailink.domain.error.CapsuleError.STATE_CHANGED -> R.string.capsule_state_changed
            com.feryaeljustice.mirailink.domain.error.CapsuleError.INVALID_ACTION -> R.string.capsule_invalid_action
        }
        is com.feryaeljustice.mirailink.domain.error.AffinityError -> when (this) {
            com.feryaeljustice.mirailink.domain.error.AffinityError.PLAN_REQUIRED -> R.string.affinity_plan_required
            com.feryaeljustice.mirailink.domain.error.AffinityError.UNAVAILABLE -> R.string.affinity_unavailable
            com.feryaeljustice.mirailink.domain.error.AffinityError.QUOTA -> R.string.affinity_quota
            com.feryaeljustice.mirailink.domain.error.AffinityError.PENDING -> R.string.affinity_pending_error
            com.feryaeljustice.mirailink.domain.error.AffinityError.BLOCKED -> R.string.affinity_contact_blocked
            com.feryaeljustice.mirailink.domain.error.AffinityError.PLAY -> R.string.affinity_play_error
        }
        UnknownError -> R.string.error_unknown
    }
