package com.feryaeljustice.mirailink.domain.mappers

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game

fun AnimeDto.toDomain() = Anime(id, name, imageUrl)
fun GameDto.toDomain() = Game(id, name, imageUrl)

fun Anime.toModel() = AnimeDto(id, name, imageUrl)
fun Game.toModel() = GameDto(id, name, imageUrl)