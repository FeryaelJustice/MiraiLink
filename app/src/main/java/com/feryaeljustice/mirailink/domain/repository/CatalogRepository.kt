package com.feryaeljustice.mirailink.domain.repository

import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

interface CatalogRepository {
    suspend fun getAnimes(): MiraiLinkResult<List<Anime>>
    suspend fun getGames(): MiraiLinkResult<List<Game>>
}