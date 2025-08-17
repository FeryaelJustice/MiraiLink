package com.feryaeljustice.mirailink.domain.model.enum

enum class Gender(val realValue: String) {
    Male("male"),
    Female("female"),
    Other("other");

    override fun toString(): String = realValue

    companion object {
        fun fromRealValue(value: String?): Gender? =
            entries.find { it.realValue == value }
    }
}