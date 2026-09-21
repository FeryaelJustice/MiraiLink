package com.feryaeljustice.mirailink.domain.model.geography

data class GeographicPlace(
    val id: String,
    val name: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val aliases: List<String> = emptyList(),
)
