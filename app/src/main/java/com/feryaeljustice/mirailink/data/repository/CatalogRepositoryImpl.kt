package com.feryaeljustice.mirailink.data.repository

import com.feryaeljustice.mirailink.data.datasource.CatalogRemoteDataSource
import com.feryaeljustice.mirailink.data.mappers.toDomain
import com.feryaeljustice.mirailink.domain.model.catalog.Anime
import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class CatalogRepositoryImpl(
    private val remote: CatalogRemoteDataSource,
) : CatalogRepository {
    override suspend fun getAnimes(): MiraiLinkResult<List<Anime>> =
        when (val result = remote.getAnimes()) {
            is MiraiLinkResult.Success -> {
                val animes =
                    result.data.map { anime ->
                        anime.toDomain()
                    }
                MiraiLinkResult.Success(animes)
            }

            is MiraiLinkResult.Error -> result
        }

    override suspend fun getGames(): MiraiLinkResult<List<Game>> =
        when (val result = remote.getGames()) {
            is MiraiLinkResult.Success -> {
                val games =
                    result.data.map { game ->
                        game.toDomain()
                    }
                MiraiLinkResult.Success(games)
            }

            is MiraiLinkResult.Error -> result
        }
}
