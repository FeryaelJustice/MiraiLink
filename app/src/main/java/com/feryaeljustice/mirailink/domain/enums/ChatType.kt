package com.feryaeljustice.mirailink.domain.enums

enum class ChatType(val nameStr: String) {
    PRIVATE(nameStr = "private"),
    GROUP(nameStr = "group");

    companion object {
        fun fromString(value: String): ChatType = entries.find { it.nameStr == value }
            ?: throw IllegalArgumentException("Invalid ChatType: $value")
    }
}