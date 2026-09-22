package com.feryaeljustice.mirailink.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GeographicPlaceDto(
    val id: String,
    val name: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val aliases: List<String> = emptyList(),
)
