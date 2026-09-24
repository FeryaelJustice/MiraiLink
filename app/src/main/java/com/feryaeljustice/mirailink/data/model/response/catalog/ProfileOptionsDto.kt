package com.feryaeljustice.mirailink.data.model.response.catalog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogItemOptionDto(
    @SerialName("id")
    val id: String,
    @SerialName("code")
    val code: String,
    @SerialName("label")
    val label: String? = null,
    @SerialName("question")
    val question: String? = null,
)

@Serializable
data class ProfileOptionsResponseDto(
    @SerialName("relationship_goals")
    val relationshipGoals: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("family_options")
    val familyOptions: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("religions")
    val religions: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("zodiac_signs")
    val zodiacSigns: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("political_stances")
    val politicalStances: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("smoking_habits")
    val smokingHabits: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("drinking_habits")
    val drinkingHabits: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("sexual_orientations")
    val sexualOrientations: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("education_levels")
    val educationLevels: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("spoken_languages")
    val spokenLanguages: List<CatalogItemOptionDto> = emptyList(),
    @SerialName("prompts")
    val prompts: List<CatalogItemOptionDto> = emptyList(),
)
