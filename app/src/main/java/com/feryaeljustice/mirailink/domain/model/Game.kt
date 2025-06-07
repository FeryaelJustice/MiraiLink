package com.feryaeljustice.mirailink.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Game(val id: String, val name: String, val imageUrl: String?)
