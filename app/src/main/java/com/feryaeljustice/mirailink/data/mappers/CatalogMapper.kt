package com.feryaeljustice.mirailink.data.mappers

import com.feryaeljustice.mirailink.data.model.AnimeDto
import com.feryaeljustice.mirailink.data.model.GameDto
import com.feryaeljustice.mirailink.domain.model.Anime
import com.feryaeljustice.mirailink.domain.model.Game

fun AnimeDto.toDomain() = Anime(id, name, imageUrl)
fun GameDto.toDomain() = Game(id, name, imageUrl)