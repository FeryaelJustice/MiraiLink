package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.ui.viewentries.user.MinimalUserInfoViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.user.UserViewEntry

fun UserViewEntry.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun MinimalUserInfoViewEntry.nicknameElseUsername(): String {
    return this.nickname.ifBlank { this.username }
}

fun calculateAge(birthdateIso: String?): Int? {
    if (birthdateIso.isNullOrBlank()) return null
    return try {
        val birthDate = java.time.LocalDate.parse(birthdateIso)
        val now = java.time.LocalDate.now()
        java.time.Period.between(birthDate, now).years
    } catch (_: Exception) {
        null
    }
}

fun isAtLeast16YearsOld(birthdateIso: String?): Boolean {
    val age = calculateAge(birthdateIso) ?: return false
    return age >= 16
}