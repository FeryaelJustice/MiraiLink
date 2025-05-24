package com.feryaeljustice.mirailink.domain.enums

enum class ChatRole(val role: String) {
    ADMIN(role = "admin"),
    MEMBER(role = "member");

    companion object {
        fun fromString(value: String): ChatRole = entries.find { it.role == value }
            ?: throw IllegalArgumentException("Invalid ChatRole: $value")
    }
}