package com.feryaeljustice.mirailink.ui.mappers

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.ui.viewentries.catalog.AnimeViewEntry
import com.feryaeljustice.mirailink.ui.viewentries.catalog.GameViewEntry

fun Anime.toAnimeViewEntry(): AnimeViewEntry = AnimeViewEntry(
    id = id,
    name = name,
    imageUrl = imageUrl
)

fun Game.toGameViewEntry(): GameViewEntry = GameViewEntry(
    id = id,
    name = name,
    imageUrl = imageUrl
)