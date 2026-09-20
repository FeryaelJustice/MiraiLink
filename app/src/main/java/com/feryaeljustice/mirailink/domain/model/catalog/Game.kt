package com.feryaeljustice.mirailink.domain.model.catalog

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val catalogKey: String = "",
    val biography: String = "",
)
