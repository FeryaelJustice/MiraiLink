package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.constants.URL_REGEX

// Pre-compile the regex for better performance on repeated calls.
private val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()

fun String.isValidUrl(): Boolean = this.trim().matches(URL_REGEX) && this.isNotBlank() && this.isSafeSqlInput()

fun String?.getFormattedUrl(): String = if (this == null || !this.isValidUrl()) TEMPORAL_PLACEHOLDER_PICTURE_URL else this

fun String.superCapitalize(): String = this.replaceFirstChar { firstChar -> firstChar.uppercase() }

/**
 * Validates if the string is a well-formed email address.
 */
fun String.isEmailValid(): Boolean = EMAIL_REGEX.matches(this) && this.isSafeSqlInput()

fun String.isPhoneNumberValid(): Boolean = this.isNotBlank() && this.isSafeSqlInput()

/**
 * Validates ISO 3166-1 alpha-2 country codes (2 uppercase letters: e.g. ES, JP, US).
 */
private val COUNTRY_CODE_REGEX = "^[A-Z]{2}$".toRegex()

fun String.isCountryCodeValid(): Boolean = COUNTRY_CODE_REGEX.matches(this.trim())

fun String.isPasswordValid(): Boolean {
    return this.length >= 8 && this.isSafeSqlInput() && this.isNotTrivialPassword()
}

/**
 * Checks that the password does not consist of repeated characters or trivial sequential patterns.
 */
fun String.isNotTrivialPassword(): Boolean {
    if (this.isBlank()) return false
    // Reject repeated single character (e.g. 11111111, aaaaaaaa)
    if (this.all { it == this[0] }) return false

    val sequences = listOf(
        "01234567890123456789",
        "98765432109876543210",
        "abcdefghijklmnopqrstuvwxyz",
        "zyxwvutsrqponmlkjihgfedcba",
        "qwertyuiop",
        "asdfghjkl",
    )
    val lower = this.lowercase()
    for (seq in sequences) {
        if (seq.contains(lower)) return false
    }
    return true
}

fun String?.isStringNotEmpty(): Boolean = !this.isNullOrEmpty()

/**
 * Comprueba si la cadena es segura (sin patrones típicos de SQL Injection).
 * Devuelve true si NO parece contener intento de inyección SQL.
 */
fun String.isSafeSqlInput(): Boolean {
    // Normaliza: elimina espacios repetidos y pasa a minúsculas
    val normalized = this.trim().lowercase()

    // Palabras y símbolos sospechosos
    val suspiciousPatterns =
        listOf(
            "select ",
            "insert ",
            "update ",
            "delete ",
            "drop ",
            "truncate ",
            "alter ",
            "exec ",
            "union ",
            " or ",
            " and ",
            "--",
            ";--",
            ";",
            "/*",
            "*/",
            "@@",
            "char(",
            "nchar(",
            "varchar(",
            "cast(",
            "convert(",
        )

    // Comprueba si contiene alguna palabra o símbolo peligroso
    return suspiciousPatterns.none { normalized.contains(it) }
}
