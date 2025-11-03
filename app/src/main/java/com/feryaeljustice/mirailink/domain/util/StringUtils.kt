package com.feryaeljustice.mirailink.domain.util

import android.util.Patterns
import com.feryaeljustice.mirailink.domain.constants.TEMPORAL_PLACEHOLDER_PICTURE_URL
import com.feryaeljustice.mirailink.domain.constants.URL_REGEX

// Pre-compile the regex for better performance on repeated calls.
private val EMAIL_REGEX = Patterns.EMAIL_ADDRESS.toRegex()
private val PHONE_REGEX = Patterns.PHONE.toRegex()

fun String.isValidUrl(): Boolean = this.trim().matches(URL_REGEX) && this.isNotBlank() && this.isSafeSqlInput()

fun String?.getFormattedUrl(): String = if (this == null || !this.isValidUrl()) TEMPORAL_PLACEHOLDER_PICTURE_URL else this

fun String.superCapitalize(): String = this.replaceFirstChar { firstChar -> firstChar.uppercase() }

/**
 * Validates if the string is a well-formed email address.
 */
fun String.isEmailValid(): Boolean = EMAIL_REGEX.matches(this) && this.isSafeSqlInput()

/**
 * Validates if the string is a well-formed phone address.
 */
fun String.isPhoneNumberValid(): Boolean = PHONE_REGEX.matches(this) && this.isSafeSqlInput()

fun String.isPasswordValid(): Boolean {
    /*
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
    return passwordRegex.toRegex().matches(this) && this.isSafeSqlInput()*/
    return this.length >= 4 && this.isSafeSqlInput()
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
