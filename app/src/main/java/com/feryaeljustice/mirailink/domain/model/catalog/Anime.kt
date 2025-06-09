package com.feryaeljustice.mirailink.domain.model.catalog

import kotlinx.serialization.Serializable

@Serializable
data class Anime(val id: String, val name: String, val imageUrl: String?)
