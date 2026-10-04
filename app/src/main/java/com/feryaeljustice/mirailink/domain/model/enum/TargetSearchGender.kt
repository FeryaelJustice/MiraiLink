package com.feryaeljustice.mirailink.domain.model.enum

import kotlinx.serialization.Serializable

@Serializable
enum class TargetSearchGender(val wireValue: String) {
    FEMALE("female"),
    MALE("male"),
    ALL("all");

    override fun toString(): String = wireValue

    companion object {
        fun fromWireValue(value: String?): TargetSearchGender =
            when (value?.trim()?.lowercase()) {
                "female" -> FEMALE
                "male" -> MALE
                "all" -> ALL
                else -> ALL
            }

        fun defaultFor(userGender: String?): TargetSearchGender = ALL

        fun defaultFor(userGender: Gender?): TargetSearchGender = ALL
    }
}
