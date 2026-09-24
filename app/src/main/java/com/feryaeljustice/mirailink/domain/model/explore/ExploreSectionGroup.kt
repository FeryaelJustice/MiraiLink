package com.feryaeljustice.mirailink.domain.model.explore

enum class ExploreSectionGroup(val rawValue: String) {
    OTAKU("otaku"),
    GAMING("gaming"),
    CONNECTIONS("connections");

    companion object {
        fun fromRaw(raw: String): ExploreSectionGroup =
            entries.find { it.rawValue.equals(raw, ignoreCase = true) } ?: CONNECTIONS
    }
}
