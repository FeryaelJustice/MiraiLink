package com.feryaeljustice.mirailink.domain.error

/** Stable failure contract that never contains server prose or technical exceptions. */
sealed interface AppError

/** Failure caused by a remote or local data source. */
sealed interface DataError : AppError {
    /** Network category independent from HTTP libraries and status codes. */
    enum class Network : DataError {
        NO_CONNECTION,
        TIMEOUT,
        BAD_REQUEST,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        RATE_LIMITED,
        PAYLOAD_TOO_LARGE,
        SERVER,
        SERVICE_UNAVAILABLE,
        SERIALIZATION,
        UNKNOWN,
    }

    /** Local category for files, DataStore, caches and serialization. */
    enum class Local : DataError {
        NOT_FOUND,
        STORAGE_FULL,
        CORRUPTED,
        ACCESS_DENIED,
        UNKNOWN,
    }
}

/** Authentication failure with explicit recovery semantics. */
enum class AuthError : AppError {
    INVALID_CREDENTIALS,
    SESSION_EXPIRED,
    VERIFICATION_REQUIRED,
    INVALID_VERIFICATION_CODE,
    INVALID_TWO_FACTOR_CODE,
}

/** Failure detected while preparing or validating application input. */
enum class ValidationError : AppError {
    INVALID_INPUT,
    INVALID_MEDIA,
    MISSING_REQUIRED_VALUE,
}

/** Last resort used only when no safer stable classification is available. */
data object UnknownError : AppError
