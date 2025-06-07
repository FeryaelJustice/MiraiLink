package com.feryaeljustice.mirailink.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Anime(val id: String, val name: String, val imageUrl: String?)
