package com.feryaeljustice.mirailink.data.util

/**
 * Endpoint context used to interpret HTTP failures without leaking transport details.
 * A 401 in [LOGIN] differs from a 401 in [AUTHENTICATED] work.
 */
enum class NetworkOperation {
    PUBLIC,
    LOGIN,
    REGISTER,
    AUTHENTICATED,
    VERIFICATION,
    TWO_FACTOR,
}
