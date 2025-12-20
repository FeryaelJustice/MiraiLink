package com.feryaeljustice.mirailink.domain.usecase.catalog

import com.feryaeljustice.mirailink.domain.model.catalog.Game
import com.feryaeljustice.mirailink.domain.repository.CatalogRepository
import com.feryaeljustice.mirailink.domain.util.MiraiLinkResult

class GetGamesUseCase(
    private val repository: CatalogRepository,
) {
    suspend operator fun invoke(): MiraiLinkResult<List<Game>> =
        try {
            repository.getGames()
        } catch (e: Exception) {
            MiraiLinkResult.Error("An error occurred while getting the games", e)
        }
}
